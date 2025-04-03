<%--
- print.tag
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
	import="
		java.util.Locale, java.text.MessageFormat, 
		acme.client.helpers.JspHelper, acme.client.helpers.ConversionHelper"
	body-content="empty" 
	trimDirectiveWhitespaces="true"
%>

<%-- HINT: Consult Oracle's official documentation regarding formats:
  -- HINT+ https://docs.oracle.com/javase/7/docs/api/java/text/MessageFormat.html 
  --%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<%@attribute name="var" required="false" type="java.lang.String"%>
<%@attribute name="value" required="false" type="java.lang.Object"%>
<%@attribute name="format" required="false" type="java.lang.Object"%>
<%@attribute name="code" required="false" type="java.lang.String"%>
<%@attribute name="arguments" required="false" type="java.lang.String[]"%>

<jstl:if test="${code != null}">
	<jstl:choose>
		<jstl:when test="${var == null && JspHelper.isBlank(code)}">		
		</jstl:when>		
		<jstl:when test="${var == null && !JspHelper.isBlank(code)}">
			<spring:message code="${code}" arguments="${arguments}" htmlEscape="true"/>
		</jstl:when>		
		<jstl:when test="${var != null && JspHelper.isBlank(code)}">
			<%-- HINT: <jst:set var="${var}" value=""/> won't work because "var" can't be set to an expression. 
			  -- HINT+ Thus, this ugly patch is required. 
			--%>
			<jstl:set var="$$VAR$$" value="${var}" scope="page"/>
			<%  jspContext.setAttribute((String)jspContext.getAttribute("$$VAR$$"), ""); %> --%>		
		</jstl:when>
		<jstl:when test="${var != null && !JspHelper.isBlank(code)}">
			<spring:message code="${code}" arguments="${arguments}" htmlEscape="true" var="${var}" scope="request"/>
		</jstl:when>
	</jstl:choose>
</jstl:if>

<jstl:if test="${code == null}">
	<%
		String variable;
		Object value;	
		String format;
		Locale locale;
		MessageFormat formatter;
		String text;
			
		variable = (String)jspContext.getAttribute("var");
		value = jspContext.getAttribute("value");
		format = (String) jspContext.getAttribute("format");

		if (format == null) 
			text = ConversionHelper.convert(value, String.class);
		else {
			locale = response.getLocale();
			formatter = new MessageFormat(format, locale);
			text = formatter.format(new Object[] {value});		
		}
		
		text = (text == null ? "" : text.trim());
		
		jspContext.setAttribute("text", text);
		if (variable != null)
			request.setAttribute(variable, text);
	%>

	<jstl:if test="${var == null}">
		<jstl:out value="${text}"/>
	</jstl:if>
</jstl:if>

