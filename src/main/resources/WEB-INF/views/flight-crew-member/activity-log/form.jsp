<%@page%>

<%@taglib prefix ="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textarea
	code="flightCrewMember.activityLog.form.label.typeOfIncident" path="typeOfIncident"/>
	<acme:input-textarea
	code="flightCrewMember.activityLog.form.label.description" path="description"/>
	<acme:input-moment
	code="flightCrewMember.activityLog.form.label.registrationMoment" path="registrationMoment"/>
	<acme:input-integer
	code="flightCrewMember.activityLog.form.label.severity" path="severity"/>
	<acme:input-select
	code="flightCrewMember.activityLog.form.label.flightAssignment" path="flightAssignment" choices="${assignmentChoices}"/>
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command,'show|update|delete|publish') && draftMode==true}">
			<acme:submit code="flightCrewMember.activityLog.form.button.update"
			action="/flight-crew-member/activity-log/update"/>
			<acme:submit code="flightCrewMember.activityLog.form.button.delete"
			action="/flight-crew-member/activity-log/delete"/>
			<acme:submit code="flightCrewMember.activityLog.form.button.publish"
			action="/flight-crew-member/activity-log/publish"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="flightCrewMember.activityLog.form.button.create"
			action="/flight-crew-member/activity-log/create"/>
		</jstl:when>
	</jstl:choose>
</acme:form>