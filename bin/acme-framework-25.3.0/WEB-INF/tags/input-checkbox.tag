<%--
- input-checkbox.tag
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
<%@attribute name="readonly" required="false" type="java.lang.Boolean"%>

<jstl:if test="${readonly == null}">
	<jstl:set var="readonly" value="false"/>
</jstl:if>

<div class="form-group">
	<div class="form-check">
		<input id="${path}$proxy" name="${path}$proxy" type="checkbox" class="form-check-input"
			<jstl:if test="${requestScope[path] == 'true'}">
				checked
			</jstl:if>
			<jstl:if test="${readonly}">
				disabled
			</jstl:if>
		/> 
		<input id="${path}" name="${path}" type="hidden" value="<jstl:out value="${requestScope[path] ? true : false}"/>"/> 
		<label for="${path}$proxy"> 
			<acme:print code="${code}"/>
		</label>
	</div>
	<acme:show-errors path="${path}"/>
</div>

<script type="text/javascript">
	$(document).ready(function() {		
		$("#${path}\\\\$proxy").on("change", function(e) {
			if ($(this).prop('checked'))
				$(this).next().val("true");
			else
				$(this).next().val("false");
		});
	})
</script>
