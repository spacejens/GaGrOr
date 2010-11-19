<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
	<head>
		<title>GaGrOr</title>
	</head>
	<body>
		<h1>Register New User</h1>
		<form method="post">
			<spring:bind path="userRegistrationForm">
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
			<spring:bind path="userRegistrationForm.username">
				<input type="text" name="username" value="<core:out value="${userRegistrationForm.username}"/>" />
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
			<spring:bind path="userRegistrationForm.password">
				<input type="password" name="password" value="<core:out value="${userRegistrationForm.password}"/>" />
				<core:if test="${status.error}">
					<font color="red">
						errors:
						<core:forEach var="error" items="${status.errorMessages}">
							<core:out value="${error}"/> :
						</core:forEach>
					</font>
				</core:if>
			</spring:bind>
			Repeat password:
			<spring:bind path="userRegistrationForm.repeatPassword">
				<input type="password" name="repeatPassword" value="<core:out value="${userRegistrationForm.repeatPassword}"/>" />
				<core:if test="${status.error}">
					<font color="red">
						errors:
						<core:forEach var="error" items="${status.errorMessages}">
							<core:out value="${error}"/> :
						</core:forEach>
					</font>
				</core:if>
			</spring:bind>
			<input type="submit" value="Register" />
		</form>
	</body>
</html>
