<%--
- debug-payload.tag
-
- Copyright (C) 2012-2025 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@tag import="
		java.util.Enumeration, java.util.Map, java.util.TreeMap, java.util.LinkedHashMap, 
		javax.servlet.jsp.PageContext, javax.servlet.http.Cookie,
		org.springframework.web.util.WebUtils,
		acme.client.helpers.JspHelper, acme.client.helpers.StringHelper, acme.internals.components.adts.ModelKeyComparator"
	body-content="empty" 
%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>


<%
	Cookie cookie;
	String serverName, debug;
	boolean showPanel;
	
	serverName = request.getServerName();
	debug = request.getParameter("debug");
	if (debug == null) {
		cookie = WebUtils.getCookie(request, "debug");
		debug = cookie != null ? cookie.getValue() : "true";
	}
	debug = StringHelper.anyOf(debug, "true|false") ? debug : "true";
	showPanel = (debug.equals("true"));
	jspContext.setAttribute("showPanel", showPanel);
%>

<jstl:if test="${showPanel}">
	<div style="display: none">				
		<script type="text/javascript">
			$(document).ready(function() {
				var payload;
				
				payload = $("meta[name='payload']").attr("content")
				$("#debug-payload").text(payload);
			});					
		</script>
		<pre id="debug-payload">[PLACEHOLDER]</pre>
	</div>						
</jstl:if>
