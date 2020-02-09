<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<body>
<h2>Log in</h2>
<form action="${pageContext.request.contextPath}/login" method="post">
    Username: <input name="username" type="text" maxlength="40" autofocus><br>
    Password: <input name="password" type="password" maxlength="40"><br>
    <input type="submit" value="Log in">
</form>
</body>
</html>
