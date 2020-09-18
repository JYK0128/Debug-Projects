<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Insert title here</title>
</head>
<body>
<h1>JSTL 예제</h1>
<table>
    <tr>
        <th>number</th>
        <th>uid</th>
        <th>name</th>
    </tr>
    <c:forEach var="user" items="${test}" varStatus="status">
        <tr>
            <td>${status.index}</td>
            <td>${user.uid}</td>
            <td>${user.name}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>