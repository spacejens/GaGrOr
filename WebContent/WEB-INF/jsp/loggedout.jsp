<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
	<head>
		<title>GaGrOr</title>
	</head>
	<body>
		<h1>GaGrOr - Gaming Group Organizer</h1>
		<p>You were logged out.</p>
		<form method="post">
			<spring:bind path="loginForm">
				<core:if test="${status.error}">
					<font color="red">
						Global errors:
						<core:forEach var="error" items="${status.errorMessages}">
							<core:out value="${error}"/> :
						</core:forEach>
					</font>
				</core:if>
			</spring:bind>
			Username:
			<spring:bind path="loginForm.username">
				<input type="text" name="username" value="<core:out value="${loginForm.username}"/>" />
				<core:if test="${status.error}">
					<font color="red">
						errors:
						<core:forEach var="error" items="${status.errorMessages}">
							<core:out value="${error}"/> :
						</core:forEach>
					</font>
				</core:if>
			</spring:bind>
			Password:
			<spring:bind path="loginForm.password">
				<input type="password" name="password" value="<core:out value="${loginForm.password}"/>" />
				<core:if test="${status.error}">
					<font color="red">
						errors:
						<core:forEach var="error" items="${status.errorMessages}">
							<core:out value="${error}"/> :
						</core:forEach>
					</font>
				</core:if>
			</spring:bind>
			<input type="submit" value="Log in" />
		</form>
	</body>
</html>
