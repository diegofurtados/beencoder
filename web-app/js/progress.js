var progressPrecisionDigits = 3;
var inProcess = "processing";
var finished = "finished";

$(document).ready(function() {
	var ajaxRequest = null;
	var requestsWithErrors = 0;
	var timeIntervalCall = 1000;
	var maxErrorRequestsPermitted = 3;
	
	var ajaxCall = function() {
		var jobId = $("#jobId").val();
		ajaxRequest = $.ajax({
			url : "../progress/" + jobId
			,type : "GET"
			,dataType : "json"
		});

		ajaxRequest.done(function(json) {
			requestsWithErrors = 0;
			if (json.state == inProcess) {
				updateProgressControl("#input", json.input);
				updateProgressControl("#output", json.outputs[0]);
				$("#total_progress").text(json.progress.toPrecision(progressPrecisionDigits));
			} else if (json.state == finished) {
				finishProgressControls(intervalCall, json);
				attachVideoToPage(json.outputs[0].id);
			} else if (json.state == "failed") {
				addErrorAndClearInterval(intervalCall);
			}
		});

		ajaxRequest.fail(function(json) {
			requestsWithErrors++;
			if (requestsWithErrors > maxErrorRequestsPermitted) {
				console.log("More than 3 sequential request errors. Aborting.");
				addErrorAndClearInterval(intervalCall);
			}
		});
	};

	var intervalCall = setInterval(ajaxCall, timeIntervalCall);
});

function updateProgressControl(elementPrefix, jsonElement) {
	var eventElement = $(elementPrefix + "_event");
	var progressElement = $(elementPrefix + "_progress");
	var spinnerElement = $(elementPrefix + "_spinner");
		
	eventElement.text(jsonElement.state);
	if (jsonElement.state == finished){
		progressElement.text("100.0");
		spinnerElement.hide();
	} else if (jsonElement.state == inProcess && jsonElement.progress) {
		progressElement.text(jsonElement.progress.toPrecision(progressPrecisionDigits));
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
	$("#input_spinner").hide();
	$("#output_spinner").hide();
	$("#total_spinner").hide();
}

function addErrorAndClearInterval(intervalCall){
	clearInterval(intervalCall);
	$("#errorList").empty();
	$("#errorList").append("<li>PAM! Ocorreu um erro ao codificar o seu vídeo.</li>");
	$("#errorList").append("<li>Na minha máquina estava funcionando...</li>");
	$("#errorList").removeAttr("style");
}

function attachVideoToPage() {
	$("#beeVideo").add("source").attr('src', $("#url").val());
}