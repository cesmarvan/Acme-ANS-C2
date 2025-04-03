<%--
- debug-panel.tag
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
		acme.client.helpers.JspHelper, acme.client.helpers.StringHelper, 
		acme.internals.components.adts.ModelKeyComparator"
	body-content="empty" 
%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<%@attribute name="full" required="false" type="java.lang.Boolean"%>

<jstl:if test="${full == null}">
	<jstl:set var="full" value="${false}"/>
</jstl:if>

<%
	Cookie cookie;
	String serverName, debug;
	boolean showPanel;
	ModelKeyComparator comparator;
	Map<String, Object[]> highlightData, userPlainData, userIndexedData, errorData, supportData, internalData, workAreaData, otherData;	
	int scope;
	Enumeration<?> keys;	
	
	serverName = request.getServerName();
	debug = request.getParameter("debug");
	if (debug == null) {
		cookie = WebUtils.getCookie(request, "debug");
		debug = cookie != null ? cookie.getValue() : "true";
	}
	debug = StringHelper.anyOf(debug, "true|false") ? debug : "true";
	showPanel = (debug.equals("true"));
	jspContext.setAttribute("showPanel", showPanel);
		
	if (showPanel) {
		comparator = new ModelKeyComparator();
		
		highlightData = JspHelper.computeHighlightData(request, response);
		userPlainData = new TreeMap<String, Object[]>(comparator);
		userIndexedData = new TreeMap<String, Object[]>(comparator);
		errorData = new TreeMap<String, Object[]>(comparator);
		supportData = new TreeMap<String, Object[]>(comparator);
		internalData = new TreeMap<String, Object[]>(comparator);
		workAreaData = new LinkedHashMap<String, Object[]>();
		otherData = new TreeMap<String, Object[]>(comparator);
						
		scope = PageContext.REQUEST_SCOPE;
		keys = jspContext.getAttributeNamesInScope(scope);
				
		while (keys.hasMoreElements()) {
			String name;
			Object value;
			boolean selected;			
			Object[] pair;
			
			name = (String) keys.nextElement();
			value = jspContext.getAttribute(name, scope);
			
			if (JspHelper.isSpecialName(name)) 
				JspHelper.appendDebugPanelData(otherData, name, value);
			else {
				if (StringHelper.anyOf(name, "$request|$buffer|$response"))
					JspHelper.appendDebugPanelData(workAreaData, name, value);
				else if (name.startsWith("_"))
					JspHelper.appendDebugPanelData(supportData, name, value);
				else if (name.endsWith("$error"))
					JspHelper.appendDebugPanelData(errorData, name, value);
				else if (name.contains("$"))
					JspHelper.appendDebugPanelData(internalData, name, value);
				else if (!name.contains("["))
					JspHelper.appendDebugPanelData(userPlainData, name, value);
				else
					JspHelper.appendDebugPanelData(userIndexedData, name, value);
			}
		}
		
		jspContext.setAttribute("highlightData", highlightData);
		jspContext.setAttribute("userPlainData", userPlainData);
		jspContext.setAttribute("userIndexedData", userIndexedData);
		jspContext.setAttribute("errorData", errorData);
		jspContext.setAttribute("supportData", supportData);
		jspContext.setAttribute("internalData", internalData);
		jspContext.setAttribute("workAreaData", workAreaData);
		jspContext.setAttribute("otherData", otherData);		
	}
%>

<jstl:if test="${showPanel}">
	<div class="panel" style="word-wrap: break-word; font-family: monospace; font-size: small; background-color: LightGray; margin-top: 1em; padding: 1em; border-radius: 0.25rem;">
		<div class="panel-body">
			<h1 class="alert alert-warning">Summary</h1>
			<div class="alert alert-info">
				<dl>		
					<jstl:forEach var="entry" items="${highlightData.entrySet()}">
						<dt><jstl:out value="${entry.key}: ${entry.value[1]}"/></dt>
						<dd><pre><jstl:out value="${entry.value[0]}"/></pre></dd>
					</jstl:forEach>
				</dl>
			</div>
			<h1 class="alert alert-warning">Model data</h1>		
			<div class="alert alert-info">
				<h2>User data</h2>
				<dl>		
					<jstl:forEach var="entry" items="${userPlainData.entrySet()}">
						<dt><jstl:out value="${entry.key}: ${entry.value[1]}"/></dt>
						<dd><pre><jstl:out value="${entry.value[0]}"/></pre></dd>
					</jstl:forEach>			
					<jstl:forEach var="entry" items="${userIndexedData.entrySet()}">
						<dt><jstl:out value="${entry.key}: ${entry.value[1]}"/></dt>
						<dd><pre><jstl:out value="${entry.value[0]}"/></pre></dd>
					</jstl:forEach>				
				</dl>
			</div>
			<div class="alert alert-info">
				<h2>Error data</h2>
				<dl>		
					<jstl:forEach var="entry" items="${errorData.entrySet()}">
						<dt><jstl:out value="${entry.key}: ${entry.value[1]}"/></dt>
						<dd><pre><jstl:out value="${entry.value[0]}"/></pre></dd>
					</jstl:forEach>
				</dl>
			</div>
			<div class="alert alert-info">
				<h2>Support data</h2>
				<dl>		
					<jstl:forEach var="entry" items="${supportData.entrySet()}">
						<dt><jstl:out value="${entry.key}: ${entry.value[1]}"/></dt>
						<dd><pre><jstl:out value="${entry.value[0]}"/></pre></dd>
					</jstl:forEach>				
				</dl>
			</div>
			<div class="alert alert-info">
				<h2>Internal data</h2>
				<dl>		
					<jstl:forEach var="entry" items="${internalData.entrySet()}">
						<dt><jstl:out value="${entry.key}: ${entry.value[1]}"/></dt>
						<dd><pre><jstl:out value="${entry.value[0]}"/></pre></dd>
					</jstl:forEach>
				</dl>
			</div>
			<jstl:if test="${!workAreaData.isEmpty()}">
				<h1 class="alert alert-warning">Work areas</h1>			
				<jstl:forEach var="workarea" items="${workAreaData.entrySet()}">
					<div class="alert alert-info">
						<h2><jstl:out value="${workarea.key}: ${workarea.value[1]}"/></h2>					
						<jstl:forEach var="dataset" items="${workarea.value[0]}">
							<div><pre><jstl:out value="${dataset}"/></pre></div>							
						</jstl:forEach>					
					</div>
				</jstl:forEach>
			</jstl:if>
			<jstl:if test="${full == true}">
				<h1 class="alert alert-warning">Other data</h1>				
				<div class="alert alert-info">
					<dl>		
						<jstl:forEach var="entry" items="${otherData.entrySet()}">
							<dt><jstl:out value="${entry.key}: ${entry.value[1]}"/></dt>
							<dd><pre><jstl:out value="${entry.value[0]}"/></pre></dd>
						</jstl:forEach>
					</dl>
				</div>
			</jstl:if>
		</div>
	</div>
</jstl:if>
