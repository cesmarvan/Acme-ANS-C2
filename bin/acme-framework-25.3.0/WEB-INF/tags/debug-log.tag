<%--
- debug-log.tag
-
- Copyright (C) 2012-2025 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@tag import="org.apache.commons.logging.Log, org.apache.commons.logging.LogFactory" body-content="empty"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<%@attribute name="level" required="false" type="java.lang.String"%>
<%@attribute name="message" required="true" type="java.lang.String"%>

<jstl:if test="${level == null}">
	<jstl:set var="level" value="info"/>
</jstl:if>

<%
	Log log;
	String level, message;

	level = (String)jspContext.getAttribute("level");
	message = (String)jspContext.getAttribute("message");
	log = LogFactory.getLog(getClass());
	switch (level) {
		case "trace": log.trace(message); break;
		case "debug": log.debug(message); break;
		case "info": log.info(message); break;
		case "warn": log.warn(message); break;
		case "error": log.error(message); break;
		case "faltal": log.fatal(message); break;
		default: log.info(message); break;		
	}
%>
