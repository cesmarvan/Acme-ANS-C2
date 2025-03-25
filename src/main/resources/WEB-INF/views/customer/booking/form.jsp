<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-textbox code="customer.booking.form.label.locatorCode" path="locatorCode"/>
	<acme:input-checkbox code="customer.booking.form.label.purchaseMoment" path="purchaseMoment"/>
	<acme:input-money code="customer.booking.form.label.travelClass" path="travelClass"/>
	<acme:input-textarea code="customer.booking.form.label.price" path="price"/>
	<acme:input-checkbox code="customer.booking.form.label.lastCreditCardNibble" path="lastCreditCardNibble"/>


	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish')}">
			<acme:submit code="customer.booking.form.button.update" action="/customer/booking/update"/>
			<acme:submit code="customer.booking.form.button.delete" action="/customer/booking/delete"/>
			<acme:submit code="customer.booking.form.button.publish" action="/customer/booking/publish"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="customer.booking.form.button.create" action="/customer/booking/create"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>