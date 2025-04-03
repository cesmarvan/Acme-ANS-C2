<%--
- hidden-data.tag
-
- Copyright (C) 2012-2025 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@tag body-content="empty"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<%@attribute name="path" required="true" type="java.lang.String"%>
<%@attribute name="value" required="false" type="java.lang.String"%>

<jstl:if test="${value == null}">
	<jstl:set var="value" value="${requestScope[path]}"/>
</jstl:if>

<input 
	id="${path}" 
	name="${path}"
	value="<acme:print value="${value}"/>"
	type="hidden" 
/>
