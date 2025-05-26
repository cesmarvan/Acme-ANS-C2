
<%@page%>
 
 <%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
 <%@taglib prefix="acme" uri="http://acme-framework.org/"%>
 
 <acme:list>
 	<acme:list-column code="assistance-agent.claim.list.label.registrationMoment" path="registrationMoment" width="10%"/>
	<acme:list-column code="assistance-agent.claim.list.label.email" path="email" width="70%"/>
	<acme:list-column code="assistance-agent.claim.list.label.type" path="type" width="10%"/>
	<acme:list-column code="assistance-agent.claim.list.label.indicator" path="indicator" width="10%"/>
 </acme:list>
 
 <jstl:if test="${_command == 'list-pending'}">
  	<acme:button code="assistance-agent.claim.list-pending.button.create" action="/assistance-agent/claim/create"/>
 </jstl:if>