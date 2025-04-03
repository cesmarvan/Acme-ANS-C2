<%--
- list-column.tag
-
- Copyright (C) 2012-2025 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@tag 
	import="java.util.Map, java.util.LinkedHashMap, acme.client.helpers.JspHelper"
	body-content="empty"
%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<%@attribute name="path" required="true" type="java.lang.String"%>
<%@attribute name="code" required="true" type="java.lang.String"%>
<%@attribute name="format" required="false" type="java.lang.String"%>
<%@attribute name="sortable" required="false" type="java.lang.Boolean"%>
<%@attribute name="width" required="false" type="java.lang.String"%>

<jstl:if test="${format == null}">
	<jstl:set var="format" value="{0}"/>
</jstl:if>

<jstl:if test="${sortable == null}">
	<jstl:set var="sortable" value="${true}"/>
</jstl:if>

<jstl:if test="${width == null}">
	<jstl:set var="width" value="inherited"/>
</jstl:if>

<%
	Map<String, Object> column;
	Boolean sortable;

	column = new LinkedHashMap<String, Object>();
	column.put("type", "data");	
	column.put("path", jspContext.getAttribute("path"));
	column.put("code", jspContext.getAttribute("code"));
	column.put("format", jspContext.getAttribute("format"));
	column.put("sortable", jspContext.getAttribute("sortable"));
	column.put("width", jspContext.getAttribute("width"));
	column.put("visible", true);

	JspHelper.updateDatatableColumns(request, column);
%>


