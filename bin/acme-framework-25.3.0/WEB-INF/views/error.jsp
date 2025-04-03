<%--
- error.jsp
-
- Copyright (C) 2012-2025 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page contentType="text/html; charset=UTF-8" isErrorPage="true"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "https://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Debug error page</title>
</head>
<body>
	<h1>Debug error page</h1>
	
	<p>If you happen to see this page, then there's a serious bug in your application, 
	which is not likely to be easy to track down and diagnose. Sorry, no more info is available.</p>
	
	<p>Status code: <%= response.getStatus() %>.</p>
	<p>Content type: <%= response.getContentType() %>.</p>
	<p>Char encoding: <%= response.getCharacterEncoding() %>.</p>
	<p>Headers: <%= response.getHeaderNames() %>.</p> 	 	
</body>
</html>