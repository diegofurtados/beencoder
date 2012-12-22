
<%@ page import="com.beencoder.BeeVideo" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<title><g:message code="default.show.label" args="[entityName]" /></title>
		<g:javascript library="jquery" plugin="jquery"/>
		<r:require module="jquery" />
	</head>
	<body>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="encode"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-beeVideo" class="content scaffold-show" role="main">
			<h1>BeeVideo</h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			<g:hiddenField name="jobId" value="${beeVideoInstance?.beeVideo?.id}"/>
			<g:hiddenField name="url" value="${beeVideoInstance?.beeVideo?.output[0].url}"/>
			<div>
				<ul id="errorList" class="errors" role="alert" style="visibility: hidden;"></ul>
			</div>
			<div class="fieldcontain">
				<span class="property-label">BeeVideo:</span> 
				<span id="beeVideoName" class="property-value">${beeVideoInstance?.beeVideo?.output[0].label}</span>
			</div>
			
			<div id="progress_controls" class="fieldcontain">
				<span class="property-label">Input Event:</span>
				<span id="input_event" class="property-value"></span>
				<br />
				<span class="property-label">Input Progress:</span>
				<span class="property-value"><span id="input_progress">0</span>%</span>
				<br /><br />
				
				<span class="property-label">Output Event:</span>
				<span id="output_event" class="property-value"></span>
				<br />
				<span class="property-label">Output Progress:</span>
				<span class="property-value"><span id="output_progress">0</span>%</span>
				<br />
				
				<br />
				<span class="property-label">Total Progress:</span>
				<span class="property-value"><span id="total_progress">0</span>%</span>
			</div>
		</div>
		<div align="center" style="margin-top: 100px;">
			<span id="showVideo">
				<video id="beeVideo" width="700px" height="500px" controls autoplay></video>
			</span>			
		</div>
		<g:javascript src="progress.js" />
	</body>
</html>
