<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<body>
<table>
    <caption>Users</caption>
    <thead>
    <tr>
        <td>Username</td>
        <td>Password</td>
        <td>Created By</td>
        <td>Role</td>
    </tr>
    </thead>
    <c:forEach var="user" items="${users}">
        <tr>
            <td><c:out value="${user.username}"/></td>
            <td><c:out value="${user.password}"/></td>
            <td><c:out value="${user.createdBy}"/></td>
            <td><c:out value="${user.userRole}"/></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
