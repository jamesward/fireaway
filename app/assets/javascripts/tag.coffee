$ ->
  $("#submitForm").bind "submit", (event) ->
    event.preventDefault()
    $.ajax
      type: "POST"
      url: $(this).attr("action")
      data: $("#questionInput").val(),
      dataType: "text",
      contentType: "text/plain"