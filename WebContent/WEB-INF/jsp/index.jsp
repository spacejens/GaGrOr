<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
	<head>
		<title>GaGrOr</title>
	</head>
	<body>
		<h1>GaGrOr - Gaming Group Organizer</h1>
		<h2>Log In</h2>
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
		<p><a href="register.html">Register a new user</a></p>
		<h2>System Description</h2>
		<p>To go to another page, <a href="welcome/initial.html">click here</a></p>
	</body>
</html>
