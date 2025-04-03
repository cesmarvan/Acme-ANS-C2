<%--
- list-payload.tag
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

<%
	Map<String, Object> column;
	Boolean sortable;

	column = new LinkedHashMap<String, Object>();
	column.put("type", "payload");
	column.put("path", jspContext.getAttribute("path"));
	column.put("code", null);
	column.put("format", "{0}");
	column.put("sortable", false);
	column.put("width", "0px");
	column.put("visible", false);	
		
	JspHelper.updateDatatableColumns(request, column);
%>


