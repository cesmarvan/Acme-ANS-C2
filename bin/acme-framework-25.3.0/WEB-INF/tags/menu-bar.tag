<%--
- menu-bar.tag
-
- Copyright (C) 2012-2025 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@tag%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<nav class="navbar navbar-expand-sm rounded sticky-top navbar-dark text-white bg-dark">
	<!-- Home button -->
	
	<a href="any/system/welcome" class="navbar-brand" onclick="javascript: clearReturnUrl();">
		<span class="fa fa-home"></span>
		<acme:print code="master.menu.home"/>
	</a>
	
	<!-- Toggle button -->
	
	<button class="navbar-toggler" type="button" data-toggle="collapse"	data-target="#mainMenu">
		<span class="fa fa-bars"></span>
	</button>

	<div id="mainMenu" class="collapse navbar-collapse">
		<jsp:doBody/>
	</div>
	
	<!-- Sign-in/up/out -->
	
	<ul class="navbar-nav ml-auto">
		<security:authorize access="isAnonymous()">
			<a href="javascript: clearReturnUrl(); redirect('/anonymous/user-account/create')" class="nav-link">
				<acme:print code="master.menu.sign-up"/> 
			</a>
		</security:authorize>		
		<security:authorize access="isAnonymous()">
			<a href="javascript: clearReturnUrl(); redirect('/anonymous/system/sign-in')" class="nav-link">
				<acme:print code="master.menu.sign-in"/> 
			</a>
		</security:authorize>			
		<security:authorize access="isAuthenticated()">
			<a href="javascript: clearReturnUrl(); redirect('/authenticated/system/sign-out')" class="nav-link">
				<acme:print code="master.menu.sign-out"/> 
			</a>
		</security:authorize>
	</ul>	
</nav>
