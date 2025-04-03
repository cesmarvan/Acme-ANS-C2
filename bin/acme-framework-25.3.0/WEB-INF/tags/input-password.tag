<%--
- input-password.tag
-
- Copyright (C) 2012-2025 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@tag body-content="empty"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<%@attribute name="path" required="true" type="java.lang.String"%>
<%@attribute name="code" required="true" type="java.lang.String"%>
<%@attribute name="placeholder" required="false" type="java.lang.String"%>
<%@attribute name="readonly" required="false" type="java.lang.Boolean"%>

<jstl:if test="${placeholder == null}">
	<jstl:set var="placeholder" value="acme.default.placeholder.password"/>	
</jstl:if>

<jstl:if test="${readonly == null}">
	<jstl:set var="readonly" value="false"/>
</jstl:if>

<jstl:if test="${readonly}">
	<acme:print var="$hint" value=""/>
</jstl:if>
<jstl:if test="${!readonly}">
	<acme:print var="$hint" code="${placeholder}"/>
</jstl:if>

<div class="form-group">
	<label for="${path}"> 
		<acme:print code="${code}"/>
	</label>
	<input 
		id="${path}" 
		name="${path}"
		value="<jstl:out value="${requestScope[path]}"/>"
		type="password" 
		class="form-control"
		placeHolder="${$hint}"
		<jstl:if test="${readonly}">readonly</jstl:if>
	/>
	<acme:show-errors path="${path}"/>	
</div>
