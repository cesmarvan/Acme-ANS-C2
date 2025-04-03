<%--
- list.tag
-
- Copyright (C) 2012-2025 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@tag import="java.util.Collection, java.util.ArrayList, java.util.Map, acme.client.helpers.JspHelper"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<%@attribute name="navigable" required="false" type="java.lang.Boolean"%>
<%@attribute name="show" required="false" type="java.lang.String"%>
<%@attribute name="fetch" required="false" type="java.lang.String"%>

<jstl:if test="${navigable == null}">
	<jstl:set var="navigable" value="${true}"/>
</jstl:if>

<jstl:if test="${show == null}">
	<jstl:set var="show" value="show"/>
</jstl:if>

<%-- HINT: fetch can be null --%>

<%
	Collection<Map<String, Object>> dataTableColumns;
	
	dataTableColumns = new ArrayList<Map<String, Object>>();
	request.setAttribute("$data$table$columns", dataTableColumns);
%>

<table id="list" class="table table-striped table-condensed table-hover display nowrap w-100">
	<jsp:doBody var="body"/>
	<thead>
		<tr>
			<!-- <th class="control"></th> --><%-- HINT: placeholder for the (+) button --%>
			<jstl:set var="aoColumns" value="'aoColumns': [ "/> <!-- {'data': function(r, t, s) {return '';}, 'bSortable': false, 'bSearchable': false, 'bVisible': false} -->
			<jstl:set var="comma" value=""/> 
			<jstl:forEach var="column" items="${$data$table$columns}">
				<th style="width: ${column.width}; text-align: left" data-format="${column.format}">
					<jstl:if test="${column.code != null}">
						<acme:print code="${column.code}"/>
					</jstl:if>
					<jstl:set var="aoColumns" value="${aoColumns}${comma} {'data': '${column.path}', 'bSortable': ${column.sortable}, 'bSearchable': true, 'bVisible': ${column.visible}}"/>
					<jstl:set var="comma" value=","/>
				</th>
			</jstl:forEach>
			<jstl:set var="aoColumns" value="${aoColumns} ]"/>
		</tr>
	</thead>
	<tbody>
		<jstl:if test="${fetch == null && $number$data != null && $number$data >= 1}">			
			<jstl:forEach var="index" begin="${0}" end="${$number$data - 1}">
				<jstl:set var="index_id" value="id[${index}]"/>				
				<tr data-item-id="${requestScope[index_id]}">
					<!-- <td class="control"></td> --><%-- HINT: placeholder for the (+) button --%>
					<jstl:forEach var="column" items="${$data$table$columns}">
						<jstl:set var="path" value="${column.path}"/> 
						<jstl:set var="format" value="${column.format}"/>
						<jstl:set var="index_path" value="${path}[${index}]"/>
						<jstl:set var="dataSort" value="${JspHelper.computeDataSort(requestScope[index_path])}"/>						
						<jstl:set var="dataText" value="${JspHelper.computeDataText(requestScope[index_path], format)}"/>
						<td ${dataSort}>
					 		<acme:print value="${dataText}"/>							
						</td>
					</jstl:forEach>
				</tr>
			</jstl:forEach>
		</jstl:if>
	</tbody>
</table>

<acme:return/>

<acme:print var="$i18n$File$Uri" code="acme.default.datatables.language"/>
<jstl:set var="language" value="'language': { 'url': getAbsoluteUrl('${$i18n$File$Uri}') }"/>

<script type="text/javascript">
	$(document).ready(function() {
		var fetchUrl, table;		

		<jstl:if test="${fetch != null}">
			fetchUrl = "<%= JspHelper.getUrlForCommand(request, (String)jspContext.getAttribute("fetch")) %>";
		</jstl:if>
		<jstl:if test="${fetch == null}">
			fetchUrl = null;
		</jstl:if>
		// HINT: the "retrieve" option prevents a reinitialisation exception that may happen 
		// HINT+ due to this event firing twice due to some unknown asynchrony problems.
		table = $("#list").
				dataTable({
					<jstl:out value="${aoColumns}" escapeXml="false"/>,
					<jstl:out value="${language}" escapeXml="false"/>,

	    			  "retrieve": true,		
    			     "stateSave": true,				 			  
 			  	    "lengthMenu": [5, 10, 25, 50, 75, 100],
			  	    "pageLength": 5,			  	
			  	    "pagingType": "numbers",
			  	        "paging": true,
 			  	           "dom": "<'row'<'col'f><'col'p>><'row'<'col'tr>><'row'<'col'l><'col'i>>",
	  		  	    "responsive": { "details": { "type": "inline"} },
				   	     "order": [ [0, "asc"] ],  			   	     
					
					"createdRow": function(row, data, index) {
						$(row).attr("data-item-id", data.id);
				   	},
				   
				<jstl:if test="${fetch == null}">
				   	"searching": { "regex": false, "caseInsensitive": true, "return": true },
					"processing": true,
				</jstl:if>
				   	
				<jstl:if test="${fetch != null}">
					 "searching": false,
				    "serverSide": true,
				  		  "ajax": { 
				  	     		"url": fetchUrl, 
				  	    	   "type": "get", 
				  	       "datatype": "json",
				  	    "contentType": "application/json; charset=utf-8",
				  	    /*
				  	           "data": function (data) { console.log("Ajax request: "); console.log(data); return data; },
						 "dataFilter": function (data, type) { console.log("Ajax result: "); console.log(JSON.parse(data)); return data; },
						      "error": function (xhr, status, error) { console.log(status + " : " + error); },
						*/
				  	      },
			  	</jstl:if>
	    });
	    	
		<jstl:if test="${navigable}">
		    $("#list tbody").on("click", "td", function () {
				var id;
		    	
		    	id = $(this).parent().attr("data-item-id")
			    if (typeof(id) != "undefined") {
			    	pushCurrentUrl();
			    	redirect("${show}?id={0}".format(id));
			    }
	    	});
		</jstl:if>
	});	
</script>
