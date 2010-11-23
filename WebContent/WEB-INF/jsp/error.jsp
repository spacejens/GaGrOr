<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
	<head>
		<title>GaGrOr</title>
	</head>
	<body>
		<h1><core:out value="${headline}" /></h1>
		<p><core:out value="${message}" /></p>
		<p>Return to the the <a href="dashboard/index.html">dashboard</a></p>
	</body>
</html>
