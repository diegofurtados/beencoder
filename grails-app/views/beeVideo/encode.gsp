<%@ page import="com.beencoder.BeeVideo" %>
<!DOCTYPE html>
<html>
	
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'beeVideo.label', default: 'BeeVideo')}" />
		<title>Codificar Vídeo</title>
	</head>
	
	<body>
		
		<a href="#create-beeVideo" class="skip" tabindex="-1">
			<g:message code="default.link.skip.label" default="Skip to content&hellip;"/>
		</a>
		
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="list">Lista de Vídeos Codificados</g:link></li>
			</ul>
		</div>
		
		<div id="create-beeVideo" class="content scaffold-create" role="main">
			
			<h1>Codificar Vídeo</h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			
			<g:hasErrors bean="${beeVideoInstance}">
				<ul class="errors" role="alert">
					<g:eachError bean="${beeVideoInstance}" var="error">
					<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
					</g:eachError>
				</ul>
			</g:hasErrors>
			
			<g:uploadForm action="encode">
				<input type="file" id="beeMovie" name="beeMovie" /> 
				<fieldset class="buttons">
					<g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
				</fieldset>
			</g:uploadForm>
			
		</div>
	</body>
</html>
