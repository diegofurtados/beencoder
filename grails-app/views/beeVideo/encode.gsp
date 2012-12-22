<%@ page import="com.beencoder.BeeVideo" %>
<!DOCTYPE html>
<html>
	
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'beeVideo.label', default: 'BeeVideo')}" />
		<title>Codificar Vídeo</title>
		<g:javascript library="jquery" plugin="jquery"/>
		<r:require module="jquery" />
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
			
			<div class="fieldcontain">
				<span class="property-label">Tipo de upload:</span> 
				<span class="property-value">
					<input type="radio" id="uploadFileType" name="uploadType" value="0" checked="checked"/> Upload file
					<input type="radio" id="uploadUrlType" name="uploadType" value="1" /> URL file
				</span>
			</div>
			<div id="uploadVideoForm">
				<g:uploadForm id="uploadVideoForm" action="encode">
					<div class="fieldcontain">
						<span class="property-label">Selecione o vídeo:</span> 
						<span id="beeVideoName" class="property-value">
							<input type="file" id="beeMovie" name="beeMovie" />
						</span>
					</div>
					<fieldset class="buttons">
						<g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
					</fieldset>
				</g:uploadForm>
			</div>
			<div id="urlVideoForm">
				<g:form action="encode">
					<div class="fieldcontain">
						<span class="property-label">Vídeo Url:</span> 
						<span id="beeVideoName" class="property-value">
							<input type="text" id="beeMovieUrl" name="beeMovieUrl" size="70">
						</span>
					</div>
					<fieldset class="buttons">
						<g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
					</fieldset>	
				</g:form>
			</div>
		</div>
		<script type="text/javascript">
			$(document).ready(function (){
				$("#urlVideoForm").hide();
				$("input[name=uploadType]").click(function (){
					if ($('input[name=uploadType]:checked').val() == 0){
						$("#urlVideoForm").hide();
						$("#uploadVideoForm").show();
					} else {
						$("#urlVideoForm").show();
						$("#uploadVideoForm").hide();
					}
				});
			});
		</script>
	</body>
</html>
