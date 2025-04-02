<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="customer.passenger.list.label.fullName" path="fullName" width="50%"/>
	<acme:list-column code="customer.passenger.list.label.email" path="email" width="50%"/>
</acme:list>

<acme:button code="customer.passenger.form.button.create" action="/customer/passenger/create"/>
