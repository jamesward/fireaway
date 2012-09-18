# todo: use AMD to load jQuery ?
# todo: display url to tag page (how do I get this url?)
# todo: subscribe to tag stream
# todo: display questions in div with id of 'fireaway'

# todo: parameterize
tag = "asdf"

$ ->
  
  $("#fireaway").bind "cometMessage", (event, data) ->
    console.log data
    $("#questions").append $("<li>").text(data)
    
  # Add a holder for the list of questions
  $("#fireaway").append $("<ul>").attr("id", "questions")

  # Add the cometMessage callback handler
  $("#fireaway").append $("<script>").text "function cometMessage(data) { $('#fireaway').trigger('cometMessage', data) }"
  
  # Add the iframe for the comet connection
  # todo: fully-qualified-url needed?
  $("#fireaway").append $("<iframe>").attr("src", "/comet/" + tag).css("display", "none")
