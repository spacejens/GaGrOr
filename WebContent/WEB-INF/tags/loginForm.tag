<%-- Displays a login form --%>

<%@taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<form action="login.html" method="post">
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
