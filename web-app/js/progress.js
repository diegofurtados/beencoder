if ($.browser.msie) {
	$(location).attr('href', "http://abetterbrowser.org");
}

var progressPrecisionDigits = 3;
var inProcess = "processing";
var finished = "finished";

var progressPrecisionDigits = 3;
var inProcess = "processing";
var finished = "finished";

$(document)
		.ready(
				function() {
					var ajaxRequest = null;
					var requestsWithErrors = 0;
					var timeIntervalCall = 2000;
					var maxErrorRequestsPermitted = 3;
					var ajaxCall = function() {
						var jobId = $("#jobId").val();
						ajaxRequest = $.ajax({
							url : "/beencoder/beeVideo/progress/" + jobId,
							type : "GET",
							dataType : "json"
						});

						ajaxRequest
								.done(function(json) {
									requestsWithErrors = 0;
									if (json.state == inProcess) {
										updateProgressControl(
												$("#input_event"),
												$("#input_progress"),
												json.input);
										updateProgressControl(
												$("#output_event"),
												$("#output_progress"),
												json.outputs[0]);
										$("#total_progress")
												.text(
														json.progress
																.toPrecision(progressPrecisionDigits));
									} else if (json.state == finished) {
										finishProgressControls(intervalCall,
												json);
										attachVideoToPage(json.outputs[0].id);
									} else if (json.state == "failed") {
										clearInterval(intervalCall);
										$("#errors")
												.add("li")
												.text(
														"Ocorreu um erro ao codificar o seu vídeo.");
									}
								});

						ajaxRequest
								.fail(function(json) {
									requestsWithErrors++;
									if (requestsWithErrors > maxErrorRequestsPermitted) {
										console
												.log("More than 3 sequential request errors. Aborting.");
										clearInterval(intervalCall);
									}
								});
					};

					var intervalCall = setInterval(ajaxCall, timeIntervalCall);
				});

function updateProgressControl(eventElement, progressElement, jsonElement) {
	eventElement.text(jsonElement.state);
	if (jsonElement.state == inProcess && jsonElement.progress) {
		progressElement.text(jsonElement.progress
				.toPrecision(progressPrecisionDigits));
	}
}

function finishProgressControls(intervalId, json) {
	clearInterval(intervalId);
	console.log("terminado!");
	$("#input_event").text(json.input.state);
	$("#output_event").text(json.outputs[0].state);
	$("#input_progress").text("100.0");
	$("#output_progress").text("100.0");
	$("#total_progress").text("100.0");
}

function attachVideoToPage() {
	$("#beeVideo").add("source").attr('src', $("#url").val());
	// $("#beeVideo").add("source").attr('src',
	// "http://upload.wikimedia.org/wikipedia/commons/a/a8/Ivy_Mike_test.ogg");
}