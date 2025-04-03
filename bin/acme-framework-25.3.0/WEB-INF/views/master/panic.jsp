<%--
- panic.jsp
-
- Copyright (C) 2012-2025 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page 
	isErrorPage="true"
	import="javax.servlet.jsp.ErrorData, org.apache.commons.logging.Log, org.apache.commons.logging.LogFactory,
		    org.springframework.web.util.WebUtils, org.springframework.http.HttpStatus, acme.client.helpers.JspHelper,
		    acme.client.helpers.StringHelper"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<%
	Log log;
	Throwable oops;
	ErrorData errorData;
	String debug, reason;	
	Cookie cookie;
	
	oops = (Throwable)pageContext.getAttribute("_oops", 2);
	errorData = pageContext.getErrorData();
	if (oops == null && errorData != null)
		oops = errorData.getThrowable();
	reason = HttpStatus.resolve(response.getStatus()) != null ? HttpStatus.valueOf(response.getStatus()).getReasonPhrase() : "Unknown HTTP status";
			
	log = LogFactory.getLog(getClass());
	log.error(String.format("HTTP %s %s %s", request.getMethod(), JspHelper.getRequestUrl(request), request.getProtocol()));
	log.error(String.format("Status: %d %s", response.getStatus(), reason));
	if (oops != null)
		log.error(String.format("Exception: %s", JspHelper.format(oops)));
	
	debug = request.getParameter("debug");
	if (debug == null) {
		cookie = WebUtils.getCookie(request, "debug");
		debug = cookie != null ? cookie.getValue() : "true";
	}
	debug = StringHelper.anyOf(debug, "true|false") ? debug : "true";
		
	if (JspHelper.isLocalRequest(request) && debug.equals("true"))
		pageContext.setAttribute("_oops", oops, 2);
	else
		pageContext.setAttribute("_oops", null, 2);
	pageContext.setAttribute("method", request.getMethod());
	pageContext.setAttribute("url", JspHelper.getRequestUrl(request));
	pageContext.setAttribute("protocol", request.getProtocol());
	pageContext.setAttribute("status", response.getStatus());
	pageContext.setAttribute("reason", reason);
%>

<p>
	<acme:print code="master.panic.text"/>
	<acme:return/>		
</p>

<dl>
	<dt>
		<acme:print code="master.panic.label.request"/>
	</dt>
	<dd>
		<jstl:out value="${method}"/>
		&nbsp;
		<jstl:out value="${url}"/>
		&nbsp;
		<jstl:out value="${protocol}"/>
	</dd>
	
	<dt>
		<acme:print code="master.panic.label.status"/>
	</dt>
	<dd>
		<jstl:out value="${status}"/>
		&nbsp;
		<jstl:out value="${reason}"/> 
	</dd>	
	<jstl:if test="${_oops != null}">
		<dt>
			<acme:print code="master.panic.label.exceptions"/>
		</dt>
		<dd>
			<jstl:set var="current" value="${_oops}"/>			
			<jstl:forEach var="i" begin="1" end="100">
				<jstl:if test="${current != null}">
 					<jstl:set var="oopsTitle" value="${JspHelper.format(current.stackTrace[0].toString())}"/>
					<jstl:set var="oopsDescription" value="${JspHelper.format(current.toString())}"/> 
 					<div>
  						<div class="text-danger" style="word-wrap: break-word;"><strong><jstl:out value="${oopsTitle}"/></strong></div>
  						<div>
  							<div style="float: left;">&#x2937;</div>
  							<div class="text-info"><pre><jstl:out value="${oopsDescription}"/></pre></div>
  						</div>
					</div>
					<jstl:set var="current" value="${current.cause}"/>
				</jstl:if>
			</jstl:forEach>
			<jstl:if test="${current != null}">
				<acme:print code="master.panic.text.consult-log"/>
			</jstl:if>
		</dd>
	</jstl:if>
</dl>
