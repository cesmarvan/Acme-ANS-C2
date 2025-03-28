<%@page%>

<%@taglib prefix ="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox
	code="crewMember.flightAssignment.form.label.flightAssignment.flightAssignmentId" path="flightAssignmentId"/>
	<acme:input-checkbox
	code="crewMember.flightAssignment.form.label.duty" path="duty"/>
	<acme:input-checkbox
	code="crewMember.flightAssignment.form.label.lastUpdate" path="lastUpdate"/>
	<acme:input-textbox
	code="crewMember.flightAssignment.form.label.status" path="status"/>
	<acme:input-textarea
	code="crewMember.flightAssignment.form.label.remarks" path="remarks"/>
	<acme:input-checkbox
	code="crewMember.flightAssignment.form.label.leg.legId" path="legId"/>
	
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command,'show|update|delete|publish')}">
			<acme:submit code="crewMember.flightAssignment.form.button.update"
			action="/crewMember/flightAssignment/update"/>
			<acme:submit code="crewMember.flightAssignment.form.button.delete"
			action="/crewMember/flightAssignment/delete"/>
			<acme:submit code="crewMember.flightAssignment.form.button.publish"
			action="/crewmember/flightAssignment/publish"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="crewMember.flightAssignment.form.button.create"
			action="/crewMember/flightAssignment/create"/>
		</jstl:when>
	</jstl:choose>
</acme:form>