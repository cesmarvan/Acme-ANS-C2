<%--
- sign-in.jsp
-
- Copyright (C) 2012-2025 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<jstl:if test="${param.error != null}">
	<acme:alert-error>
		<acme:print code="master.sign-in.error.text"/>
	</acme:alert-error>
</jstl:if>

<acme:form>
	<acme:input-textbox code="master.sign-in.label.username" path="username"/>
	<acme:input-password code="master.sign-in.label.password" path="password"/>
	<acme:input-checkbox code="master.sign-in.label.remember-me" path="remember"/>

	<acme:submit code="master.sign-in.button.sign-in" action="/anonymous/system/sign-in"/>
</acme:form>
