<%@page%>


 <%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
 <%@taglib prefix="acme" uri="http://acme-framework.org/"%>
 
<acme:form>
	<acme:input-moment code="assistance-agent.claim.form.label.registrationMoment" path="registrationMoment" readonly="true"/>
	<acme:input-email code="assistance-agent.claim.form.label.email" path="email" readonly="${!draftMode}"/>
	<acme:input-textarea code="assistance-agent.claim.form.label.description" path="description" readonly="${!draftMode}"/>
	<acme:input-select code="assistance-agent.claim.form.label.type" path="type"  choices="${types}" readonly="${!draftMode}"/>
	<acme:input-textbox code="assistance-agent.claim.form.label.indicator" path="indicator" readonly="true"/>
	
	<jstl:if test="${pending == true || pending == null}">
		<acme:input-select code="assistance-agent.claim.form.label.leg" path="leg" choices="${legs}" readonly="${!draftMode}"/>
	</jstl:if>	
	
	<jstl:if test="${_command != 'create'}">
		<acme:submit code="assistance-agent.tracking-log.form.button.list" action="/assistance-agent/tracking-log/list?id=${id}" method="get"/>
	</jstl:if>
		
	<jstl:choose>	 
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
			<acme:submit code="assistance-agent.claim.form.button.update" action="/assistance-agent/claim/update"/>
			<acme:submit code="assistance-agent.claim.form.button.delete" action="/assistance-agent/claim/delete"/>
			<acme:submit code="assistance-agent.claim.form.button.publish" action="/assistance-agent/claim/publish" method="post"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="assistance-agent.claim.form.button.create" action="/assistance-agent/claim/create"/>
		</jstl:when>
	</jstl:choose>
		
		
</acme:form>