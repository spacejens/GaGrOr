<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html>
	<head>
		<title>GaGrOr</title>
	</head>
	<body>
		<h1>Register New User</h1>
		<form method="post">
			Username:
			<spring:bind path="registrationform.username">
				<input type="text" name="username" value="<core:out value="${registrationform.username}"/>" />
			</spring:bind>
			Password:
			<spring:bind path="registrationform.password">
				<input type="password" name="password" value="" />
			</spring:bind>
			Repeat password:
			<spring:bind path="registrationform.repeatPassword">
				<input type="password" name="repeatPassword" value="" />
			</spring:bind>
			<input type="submit" value="Register" />
		</form>
	</body>
</html>
