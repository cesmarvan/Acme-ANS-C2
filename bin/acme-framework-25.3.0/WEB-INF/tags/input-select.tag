<%--
- input-select.tag
-
- Copyright (C) 2012-2025 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@tag import="java.util.Collection, acme.client.helpers.JspHelper" body-content="empty"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>
 
<%@attribute name="path" required="true" type="java.lang.String"%>
<%@attribute name="code" required="true" type="java.lang.String"%>
<%@attribute name="choices" required="true" type="acme.client.components.views.SelectChoices"%>
<%@attribute name="readonly" required="false" type="java.lang.Boolean"%>

<jstl:if test="${readonly == null}">
	<jstl:set var="readonly" value="false"/>
</jstl:if>

<div class="form-group">
	<input id="${path}" name="${path}" type="hidden"/>
	<label for="${path}$proxy">
		<acme:print code="${code}"/>
	</label>	
	<select id="${path}$proxy" name="${path}$proxy" class="form-control selectpicker show-tick" 
		<jstl:if test="${readonly}">disabled</jstl:if>
		<jstl:if test="${!readonly}">	
			onfocus="javascript: this.size = (this.options.length < 5 ? this.options.length : 5)" 
			onblur="javascript: this.size = 0" 
			onchange="javascript: this.size=1; this.blur(); $('#${path}').attr('value', $(this).val()); $('#${path}').attr('data-label', $(this).find(':selected').text().trim());">
		</jstl:if>	
	>
		<jstl:forEach var="choice" items="${choices.iterator()}">		
			<acme:input-option 
				value="${choice.getKey()}" 
				code="${choice.getLabel()}" 
				selected="${choice.isSelected()}"/>
		</jstl:forEach>
	</select>
	<acme:show-errors path="${path}"/>
</div>

<script type="text/javascript">
	$(document).ready(function() {
		var option, input;
		var key, label;
		
		option = $(this).find("#${path}\\\\$proxy :selected");
		key = option.val();
		label = option.text().trim();
		input = $("#${path}");
		input.attr("value", key);
		input.attr("data-label", label);
	})
</script>
