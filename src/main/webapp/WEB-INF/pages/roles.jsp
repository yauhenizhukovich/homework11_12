<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<body>
<table>
    <caption>Roles</caption>
    <thead>
    <tr>
        <td>Name</td>
        <td>Description</td>
    </tr>
    </thead>
    <c:forEach var="role" items="${roles}">
        <tr>
            <td><c:out value="${role.name}"/></td>
            <td><c:out value="${role.description}"/></td>
        </tr>
    </c:forEach>
</table>
<br>
</body>
</html>
