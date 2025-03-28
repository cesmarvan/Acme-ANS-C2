<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="crewMember.flightAssignment.list.label.flightAssignmentId" path="flightAssignmentId" width="10%"/>
	<acme:list-column code="crewMember.flightAssignment.list.label.duty" path="duty" width="5%"/>
	<acme:list-column code="crewMember.flightAssignment.list.label.lastUpdate" path="lastUpdate" width="10%"/>
	<acme:list-column code="crewMember.flightAssignment.list.label.status" path="status" width="5%"/>
	<acme:list-column code="crewMember.flightAssignment.list.label.remarks" path="remarks" width="10%"/>
	<acme:list-column code="crewMember.flightAssignment.list.label.leg.legId" path="legId" width="10%"/>
</acme:list>

<jstl:if test="${_command == 'list'}">
	<acme:button code="crewMember.flightAssignment.list.button.create"
	action="/crewMember/flightAssignment/create"/>
</jstl:if>