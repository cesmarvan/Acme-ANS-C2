<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="flightCrewMember.flightAssignment.list.label.duty" path="duty" width="13%"/>
	<acme:list-column code="flightCrewMember.flightAssignment.list.label.status" path="status" width="13%"/>
	<acme:list-column code="flightCrewMember.flightAssignment.list.label.leg" path="leg.flightNumber" width="13%"/>
	<acme:list-column code="flightCrewMember.flightAssignment.list.label.flightCrewMember" path="flightCrewMember.employeeCode" width="13%"/>
	<acme:list-column code="flightCrewMember.flightAssignment.list.label.lastUpdate" path="lastUpdate" width="13%"/>
	<acme:list-column code="flightCrewMember.flightAssignment.list.label.remarks" path="remarks" width="13%"/>
	<acme:list-payload path="payload"/>
</acme:list>

<jstl:if test="${_command == 'list'}">
	<acme:button code="flightCrewMember.flightAssignment.list.button.create"
	action="/flight-crew-member/flight-assignment/create"/>
</jstl:if>