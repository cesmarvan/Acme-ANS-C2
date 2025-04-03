<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	
		<acme:input-select code="administrator.aircraft.list.label.airline" path="airline" choices="${airlines}"/>
		<acme:input-textbox code="administrator.aircraft.list.label.model" path="model"/>
		<acme:input-textbox code="administrator.aircraft.list.label.registrationNumber" path="registrationNumber"/>
		<acme:input-textbox code="administrator.aircraft.list.label.capacity" path="capacity"/>
		<acme:input-textbox code="administrator.aircraft.list.label.cargoWeight" path="cargoWeight"/>
		<acme:input-select code="administrator.aircraft.list.label.aircraftStatus" path="status" choices="${aircraftStatus}"/>
		<acme:input-textbox code="administrator.aircraft.list.label.details" path="details"/>
		<jstl:choose>
			<jstl:when test="${acme:anyOf(_command, 'show|update|disable|enable')}">
				<acme:input-checkbox code="administrator.aircraft.form.label.confirmation.update" path="confirmation"/>
				<acme:submit code="administrator.aircraft.form.button.update" action="/administrator/aircraft/update"/>
				<jstl:if test="${status == 'ACTIVE_SERVICE'}">
					<acme:submit code="administrator.aircraft.form.button.disable" action="/administrator/aircraft/disable"/>
				</jstl:if>
				<jstl:if test="${status == 'UNDER_MAINTENANCE'}">
					<acme:submit code="administrator.aircraft.form.button.enable" action="/administrator/aircraft/enable"/>
				</jstl:if>
			</jstl:when>
			<jstl:when test="${_command == 'create'}">
				<acme:input-checkbox code="administrator.aircraft.form.label.confirmation.create" path="confirmation"/>	
				<acme:submit code="administrator.aircraft.form.button.create" action="/administrator/aircraft/create"/>
			</jstl:when>		
	</jstl:choose>
</acme:form>