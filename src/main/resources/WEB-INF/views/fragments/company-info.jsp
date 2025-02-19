<%--
- info-company.jsp
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<h1><acme:print code="master.company-info.title"/></h1>

<p><acme:print code="master.company-info.text"/></p>

<address>
    <strong><acme:print code="master.company-info.name"/></strong> <br/>
    <span class="fas fa-map-marker"> &nbsp; </span><acme:print code="master.company-info.address"/> <br/>
    <span class="fa fa-phone"></span> &nbsp; <acme:print code="master.company-info.phone"/><br/>
    <span class="fa fa-at"></span> &nbsp; <a href="mailto:<acme:print code='master.company-info.email'/>"><acme:print code="master.company-info.email"/></a> <br/>
</address>


