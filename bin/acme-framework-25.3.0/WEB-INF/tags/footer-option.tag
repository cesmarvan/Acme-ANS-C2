<%--
- footer-option.tag
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
	import="java.util.Collection, java.util.ArrayList, java.util.Map, javax.servlet.jsp.tagext.JspFragment"
	body-content="empty"
%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<%@attribute name="icon" required="true" type="java.lang.String"%>
<%@attribute name="code" required="true" type="java.lang.String"%>
<%@attribute name="action" required="true" type="java.lang.String"%>
<%@attribute name="newTab" required="false" type="java.lang.Boolean"%>

<jstl:if test="${newTab == null}">
	<jstl:set var="newTab" value="${false}"/>
</jstl:if>

<li style="font-size: 80%">
	<span class="${icon}"></span>
	<jstl:choose>
		<jstl:when test="${newTab == false}">
			<a href="javascript: redirect('${action}')">
				<acme:print code="${code}"/>
			</a>
		</jstl:when>
		<jstl:otherwise>
			<a href="javascript: redirect('${action}', '_blank')">
				<acme:print code="${code}"/>
			</a>
		</jstl:otherwise>
	</jstl:choose>
</li>

