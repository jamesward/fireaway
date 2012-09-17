package actors

import akka.actor.{Props, Actor, ActorRef}
import collection.mutable.HashMap
import play.api.libs.iteratee.Concurrent.Channel

// Manages all tags (on this node)
// Each tag has it's own actor
class MasterActor extends Actor {
  import context._
  
  var tagActors: HashMap[String, ActorRef] = HashMap.empty[String, ActorRef]
  
  def receive = {

    case Join(tag, enumerator) =>
      // add the channel to a tag
      getOrCreateTagActor(tag) ! JoinTag(enumerator)

    
    case CreateQuestion(tag, question) =>
      // todo: save the question

      // broadcast to the tag
      getOrCreateTagActor(tag) ! Send(question)
    
  }
  
  def getOrCreateTagActor(tag: String) = {
    val tagActor = tagActors.getOrElse(tag, actorOf(Props[TagActor], tag))
    tagActors(tag) = tagActor
    tagActor
  }
  
}

// Manages channels for a single tag on this node
// Each user (a channel) has it's own actor
class TagActor extends Actor {
  import context._
  
  def receive = {
    case JoinTag(enumerator) =>
      // creates a new Actor for the channel
      actorOf(Props(new ChannelActor(enumerator)))

    case Send(question) =>
      // broadcast the question to all channels for this tag
      children.foreach(_ ! Send(question))
  }
}

// Manages a channel
class ChannelActor(enumerator: Channel[String]) extends Actor {
  def receive = {
    case Send(question) =>
        enumerator.push(question)
  }
}

case class Join(tag: String, enumerator: Channel[String])
case class JoinTag(enumerator: Channel[String])
case class CreateQuestion(tag: String, question: String)
case class Send(question: String)