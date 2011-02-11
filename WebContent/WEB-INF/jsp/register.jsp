<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
	<head>
		<title>GaGrOr</title>
	</head>
	<body>
		<h1>Register New User</h1>
		<form method="post">
			<spring:bind path="userRegistrationForm">
				<c:if test="${status.error}">
					<font color="red">
						Global errors:
						<c:forEach var="error" items="${status.errorMessages}">
							<c:out value="${error}"/> :
						</c:forEach>
					</font>
				</c:if>
			</spring:bind>
			Username:
			<spring:bind path="userRegistrationForm.username">
				<input type="text" name="username" value="<c:out value="${userRegistrationForm.username}"/>" />
				<c:if test="${status.error}">
					<font color="red">
						errors:
						<c:forEach var="error" items="${status.errorMessages}">
							<c:out value="${error}"/> :
						</c:forEach>
					</font>
				</c:if>
			</spring:bind>
			Password:
			<spring:bind path="userRegistrationForm.password">
				<input type="password" name="password" value="<c:out value="${userRegistrationForm.password}"/>" />
				<c:if test="${status.error}">
					<font color="red">
						errors:
						<c:forEach var="error" items="${status.errorMessages}">
							<c:out value="${error}"/> :
						</c:forEach>
					</font>
				</c:if>
			</spring:bind>
			Repeat password:
			<spring:bind path="userRegistrationForm.repeatPassword">
				<input type="password" name="repeatPassword" value="<c:out value="${userRegistrationForm.repeatPassword}"/>" />
				<c:if test="${status.error}">
					<font color="red">
						errors:
						<c:forEach var="error" items="${status.errorMessages}">
							<c:out value="${error}"/> :
						</c:forEach>
					</font>
				</c:if>
			</spring:bind>
			<input type="submit" value="Register" />
		</form>
		<p>Or visit the <a href="index.html">front page</a></p>
	</body>
</html>
