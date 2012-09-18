package controllers

import play.api.libs.Comet
import play.api.libs.concurrent.Akka
import play.api.libs.iteratee.{Concurrent}
import play.api.mvc.{Action, Controller}
import play.api.Play.current
import akka.actor.{ActorRef, Props}
import actors.{Join, CreateQuestion, MasterActor}

object Application extends Controller {
  
  var questionActor: ActorRef = Akka.system.actorOf(Props[MasterActor])
  
  // renders the default page
  def index = Action {
    // todo: Async fetch of tags
    Ok(views.html.index())
  }

  // adds the channel to the list of channels for the specified tag (on this node)
  def getQuestionsCometStream(tag: String) = Action {
    // listen
    val (enumerator, channel) = Concurrent.broadcast[String]
    questionActor ! Join(tag, channel)
    Ok.stream(enumerator &> Comet(callback = "parent.cometMessage"))
  }

  // renders the page for a specific tag
  def getQuestions(tag: String) = Action {
    Ok(views.html.tag(tag))
  }
  
  // broadcasts the question to the channels for the specified tag (on this node)
  def createQuestion(tag: String) = Action(parse.text) { request =>

    // get the question from request body
    val question = request.body
    
    // send the message
    // todo: Async for response handle?
    questionActor ! CreateQuestion(tag, question)
    
    Ok("")
  }
  
  // todo: vote up / down
  
  // todo: mark as answered

}