<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
	<head>
		<title>GaGrOr</title>
	</head>
	<body>
		<h1><c:out value="${headline}" /></h1>
		<p><c:out value="${message}" /></p>
		<gagror:loginForm />
		<p>Or visit the <a href="index.html">front page</a></p>
	</body>
</html>
