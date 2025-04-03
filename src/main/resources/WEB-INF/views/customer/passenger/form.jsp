<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
		<acme:input-textbox code="customer.passenger.list.label.fullName" path="fullName"/>
		<acme:input-email code="customer.passenger.list.label.email" path="email"/>
		<acme:input-textbox code="customer.passenger.list.label.passportNumber" path="passportNumber"/>
		<acme:input-moment code="customer.passenger.list.label.dateOfBirth" path="dateOfBirth"/>
		<acme:input-textbox code="customer.passenger.list.label.specialNeeds" path="specialNeeds"/>
		<acme:input-checkbox code="customer.passenger.list.label.isPublished" path="isPublished" readonly="true"/>
		<jstl:choose>
		
		<jstl:when test="${(_command == 'update' || _command == 'show' || _command == 'publish') && isPublished == false}">
				<acme:submit code="customer.passenger.form.button.update" action="/customer/passenger/update"/>
				<acme:submit code="customer.passenger.form.button.publish" action="/customer/passenger/publish"/>
			</jstl:when>
			
			<jstl:when test="${_command == 'create'}">
				<acme:submit code="customer.passenger.form.button.create" action="/customer/passenger/create"/>
			</jstl:when>
					
	</jstl:choose>	
	
		
</acme:form>