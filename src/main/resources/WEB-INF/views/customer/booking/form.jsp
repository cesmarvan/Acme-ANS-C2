<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
    <acme:input-select code="customer.booking.list.label.flight" path="flight" choices="${flights}"/>
    <acme:input-textarea code="customer.booking.list.label.locatorCode" path="locatorCode"/>
    <acme:input-textbox code="customer.booking.list.label.purchaseMoment" path="purchaseMoment" readonly="true"/>
    <acme:input-select code="customer.booking.list.label.travelClass" path="travelClass" choices="${travelClass}"/>
    <acme:input-textarea code="customer.booking.list.label.price" path="price" readonly="true"/>
    <acme:input-checkbox code="customer.booking.list.label.isPublished" path="isPublished" readonly="true"/>
    <acme:input-textarea code="customer.booking.list.label.lastCreditCardNibble" path="lastCreditCardNibble"/>
    <acme:input-textarea code="customer.booking.list.label.passenger" path="passengers" readonly="true"/>
    
    <!-- Mostrar el botón de publish solo si isPublished es falso y hay al menos un pasajero -->
    <jstl:choose>
        <jstl:when test="${(_command == 'update' || _command == 'show') && isPublished == false}">
            <acme:submit code="customer.booking.form.button.update" action="/customer/booking/update"/>
        </jstl:when>
        <jstl:when test="${_command == 'publish' && isPublished == false && passengers.size() > 0}">
            <acme:submit code="customer.booking.form.button.publish" action="/customer/booking/publish"/>
        </jstl:when>
        <jstl:when test="${_command == 'create'}">
            <acme:submit code="customer.booking.form.button.create" action="/customer/booking/create"/>
        </jstl:when>
    </jstl:choose>

    <!-- Mostrar el botón de pasajeros solo si hay pasajeros -->
    <jstl:if test="${passengers.size() > 0 && (_command == 'update' || _command == 'show' || _command == 'publish')}">
        <acme:button code="customer.booking.form.button.passenger" action="/customer/passenger/list?masterId=${id}"/>
    </jstl:if>

</acme:form>
