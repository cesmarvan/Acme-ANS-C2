<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
		<acme:input-select code="customer.booking-passenger.list.label.passenger" path="passenger" choices="${passengers}"/>
		<acme:input-select code="customer.booking-passenger.list.label.booking" path="booking" choices="${bookings}"/>
		
	<jstl:choose>
			
			<jstl:when test="${_command == 'create'}">
				<acme:submit code="customer.booking-passenger.form.button.create" action="/customer/booking-passenger/create"/>
			</jstl:when>
					
	</jstl:choose>	
		
</acme:form>