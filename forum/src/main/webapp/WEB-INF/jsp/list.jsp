<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>게시글 목록</title>
</head>
<body>
	<h3>게시글 목록</h3>
	<button class="btn btn-primary" style="float: right" onclick="location.href='/insert'">작성</button>
	<table class="table">
		<tr>
			<th>No</th>
			<th>제목</th>
			<th>작성자</th>
			<th>작성날짜</th>
			<th>조회수</th>
		</tr>
		<c:forEach var="board" items="${list}">
			<tr style="cursor: pointer" 
			onclick="location.href='/detail/${board.bno}'"
			 onMouseOver="this.style.backgroundColor='#FFF4E9'" 
			 onMouseOut="this.style.backgroundColor=''">
				<td>${board.bno}</td>
				<td>${board.title}</td>
				<td>${board.writer}</td>
				<td>
					<fmt:formatDate value="${board.reg_date}" pattern="YY/MM/dd HH:mm" />
				</td>
				<td>${board.view_cnt}</td>
			</tr>
		</c:forEach>
	</table>
	<%@ include file="bootstrap.jsp"%>
</body>
</html>
