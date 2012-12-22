
<%@ page import="com.beencoder.BeeVideo" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'beeVideo.label', default: 'BeeVideo')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-beeVideo" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="encode"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-beeVideo" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
						<th>Zencoder ID</th>
						<th>Nome</th>
						<th>Tamanho</th>
						<th>Duração</th>
						<th>Estado</th>
					</tr>
				</thead>
				<tbody>
				<g:each in="${beeVideoInstanceList}" status="i" var="beeVideoJob">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
						<td><g:link action="show" id="${beeVideoJob.beeVideo.id}">${beeVideoJob.beeVideo.id}</g:link></td>
						<td>${beeVideoJob.beeVideo.output[0].label}</td>
						<td><g:formatNumber number="${beeVideoJob.beeVideo.output[0].size ? (beeVideoJob.beeVideo.output[0].size / 1024) : 0}" type="number" maxFractionDigits="2" /> KB</td>
						<td><g:formatNumber number="${beeVideoJob.beeVideo.output[0].duration ? (beeVideoJob.beeVideo.output[0].duration / 1000) : 0}" type="number" maxFractionDigits="0" /> Seg</td>
						<td>${beeVideoJob.beeVideo.output[0].state}</td>
					</tr>
				</g:each>
				</tbody>
			</table>
		</div>
	</body>
</html>
