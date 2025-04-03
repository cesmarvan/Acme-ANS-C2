<%--
- easter-egg.jsp
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

<script type="text/javascript">
"use strict";

function show(source, offset, target) {
	var image, link;
			
	image = document.createElement("img");		
	image.src = source;
	image.style.width = "400px";
	image.style.height = "400px";
	image.style.transition = "1s all";
	image.style.position = "fixed";
	image.style.left = offset;
	image.style.bottom = "0px";
	image.style.zIndex = 999999;
	
	link = document.createElement("a");
	link.href = target;
	link.style.border = "0px";
	link.target = "_blank";
	
	link.appendChild(image);
	document.body.appendChild(link);

	window.setTimeout(function () {
	  image.style.bottom = "0px";
	}, 30);
	
	window.setTimeout(function () {
	  image.style.bottom = "-600px";
	}, 4300);
	
	window.setTimeout(function () {
	  image.parentNode.removeChild(image);		  
	}, 5400);
};

// var key = [38, 38, 40, 40, 37, 39, 37, 39, 66, 65]; // Konami's code
var key = [37, 37, 39, 39, 38, 40, 49, 50, 51]; // Yenka's code 
var index = 0;

function listen(e) {		
	index += (e.which === key[index] ? 1 : 0);
	if (index == key.length) {
		show("https://i.imgur.com/9ILLXpb.gif", "0px", "https://www.youtube.com/watch?v=0kNPetjMTCQ");
		show("https://i.imgur.com/eEmxYXy.gif", "400px", "https://www.youtube.com/watch?v=0kNPetjMTCQ");
		// show("https://s6.ezgif.com/tmp/ezgif-6-87ca85c385.gif", "calc(100% - 400px)", "https://example.com")		
	  	index = 0;
	}
};
	
document.addEventListener("keyup", listen);
</script>

<div class="jumbotron">	
	<acme:print code="master.easter-egg.message"/>
</div>