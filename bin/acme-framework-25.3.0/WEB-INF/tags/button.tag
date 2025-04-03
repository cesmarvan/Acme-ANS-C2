<%--
- button.tag
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

<%@attribute name="code" required="true" type="java.lang.String"%>
<%@attribute name="action" required="true" type="java.lang.String"%>

<jstl:choose>
	<jstl:when test="${action == '#'}">
		<jstl:set var="script" value="redirectCurrent()"/>			
	</jstl:when>	
	<jstl:when test="${action == '##'}">
		<jstl:set var="script" value="redirectBackwards()"/>			
	</jstl:when>
	<jstl:otherwise>
		<jstl:set var="script" value="pushCurrentUrl(); redirect('${action}')"/>
	</jstl:otherwise>		
</jstl:choose>	
<button type="button" onclick="javascript: ${script}" class="btn btn-dark">
	<acme:print code="${code}"/>									
</button>
