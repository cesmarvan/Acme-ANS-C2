<%--
- form.tag
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
	import="java.util.Enumeration, acme.client.helpers.StringHelper"
	trimDirectiveWhitespaces="true"
%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<%@attribute name="readonly" required="false" type="java.lang.Boolean"%>

<jstl:if test="${readonly == null}">
	<jstl:set var="readonly" value="false"/>
</jstl:if>

<form id="form">
	<acme:hidden-data path="id"/>
	<acme:hidden-data path="version"/>
	<acme:hidden-data path="${_csrf.parameterName}" value="${_csrf.token}"/>	
	<acme:show-errors path="*"/>
	<jsp:doBody/>
	<acme:return/>
</form>

<script type="text/javascript">
	$(document).ready(function() {
		if (${readonly} || $("#form :input[type=submit]").length == 0) {
			$("#form :input"). //
				not(':input[type=button], :input[type=submit], :input[type=reset], :input[type=hidden]'). //
				attr("disabled", "disabled"). //
				attr("placeholder", "");
			$("#form :input[type=checkbox]"). //
				attr("disabled", "disabled");
			$("#form :input[type=select]"). //
				attr("disabled", "disabled");
		}		
	});
	$("#form").areYouSure();
</script>

<%
	Enumeration<?> keys;
	boolean isDirty;
	int requestScope;
	
	requestScope = 2; // HINT: 2 is REQUEST-SCOPE	
	keys = jspContext.getAttributeNamesInScope(requestScope); 
	isDirty = false;
	while (!isDirty && keys.hasMoreElements()) {
		String name;
	
		name = (String) keys.nextElement();
		isDirty = name.matches("^[^\\$]*\\$error$");
	}
	jspContext.setAttribute("isDirty", isDirty);	
%>

<jstl:if test="${isDirty}">
	<script type="text/javascript">
		$("#form").addClass("dirty");
	</script>
</jstl:if>

<script type="text/javascript">
	$("#form").on("keydown", ":input:not(textarea):not(:submit)", function(event) {
	    if (event.key == "Enter") {
	        event.preventDefault();
	    }
	});
</script>