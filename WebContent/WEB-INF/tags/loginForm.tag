<%-- Displays a login form --%>

<%@ include file="/WEB-INF/taglibs.jspf" %>

<form action="login.html" method="post">
	<spring:bind path="loginForm">
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
	<spring:bind path="loginForm.username">
		<input type="text" name="username" value="<c:out value="${loginForm.username}"/>" />
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
	<spring:bind path="loginForm.password">
		<input type="password" name="password" value="<c:out value="${loginForm.password}"/>" />
		<c:if test="${status.error}">
			<font color="red">
				errors:
				<c:forEach var="error" items="${status.errorMessages}">
					<c:out value="${error}"/> :
				</c:forEach>
			</font>
		</c:if>
	</spring:bind>
	<input type="submit" value="Log in" />
</form>
