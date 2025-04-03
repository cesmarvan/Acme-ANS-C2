<%@page%>

<%@taglib prefix ="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-select
	code="flightCrewMember.flightAssignment.form.label.duty" path="duty" choices="${duties}"/>
	<acme:input-select
	code="flightCrewMember.flightAssignment.form.label.status" path="status" choices="${statuses}"/>
	<acme:input-textarea
	code="flightCrewMember.flightAssignment.form.label.remarks" path="remarks"/>
	<acme:input-select
	code="flightCrewMember.flightAssignment.form.label.leg" path="leg" choices="${legs}"/>
	<acme:input-select 
	code="flightCrewMember.flightAssignment.list.label.flightCrewMember" path="flightCrewMember" choices="${flightCrewMembers}"/>
	
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command,'show|update|delete|publish') && draftMode==true}">
			<acme:submit code="flightCrewMember.flightAssignment.form.button.update"
			action="/flight-crew-member/flight-assignment/update"/>
			<acme:submit code="flightCrewMember.flightAssignment.form.button.delete"
			action="/flight-crew-member/flight-assignment/delete"/>
			<acme:submit code="flightCrewMember.flightAssignment.form.button.publish"
			action="/flight-crew-member/flight-assignment/publish"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="flightCrewMember.flightAssignment.form.button.create"
			action="/flight-crew-member/flight-assignment/create"/>
		</jstl:when>
	</jstl:choose>
</acme:form>