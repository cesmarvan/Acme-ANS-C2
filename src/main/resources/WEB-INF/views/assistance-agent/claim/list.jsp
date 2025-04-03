
<%@page%>
 
 <%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
 <%@taglib prefix="acme" uri="http://acme-framework.org/"%>
 
 <acme:list>
 	<acme:list-column code="assistance-agent.claim.list-complete.label.registrationMoment" path="registrationMoment" width="10%"/>
 	<acme:list-column code="assistance-agent.claim.list-complete.label.email" path="email" width="15%"/>
 	<acme:list-column code="assistance-agent.claim.list-complete.label.description" path="description" width="5%"/>
  	<acme:list-column code="assistance-agent.claim.list-complete.label.type" path="type" width="5%"/>
 	<acme:list-column code="assistance-agent.claim.list-complete.label.indicator" path="indicator" width="5%"/>
 	<acme:list-column code="assistance-agent.claim.list-complete.label.leg" path="leg" width="5%"/> 	 
 	<acme:list-payload path="payload"/>	
 </acme:list>
 
 <jstl:if test="${_command == 'list-complete'}">
 	<acme:button code="assistance-agent.claim.list-complete.button.create" action="/assistance-agent/claim/create"/>
 </jstl:if>
 
 <jstl:if test="${_command == 'list-pending'}">
  	<acme:button code="assistance-agent.claim.list-pending.button.create" action="/assistance-agent/claim/create"/>
 </jstl:if>