<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Article: ${board.title}</title>
</head>
<body>
	<div class="container">
		<form action="/update/${board.bno}" method="get">
			<div class="form-group">
				<label>제목</label>
				<p>${board.title}</p>
			</div>
			<div class="form-group">
				<label>작성자</label>
				<p>${board.writer}</p>
			</div>
			<div class="form-group">
				<label>작성날짜</label>
				<p>${board.reg_date}</p>
			</div>
			<div class="form-group">
				<label>첨부파일</label>
				<c:forEach var="file" items="${list}">
					<p>
						<a href="/download/${board.bno}/${file.UUID}/${file.name}">${file.name}</a>
					</p>
				</c:forEach>
			</div>
			<div class="form-group">
				<label>내용</label>
				<p>${board.contents}</p>
			</div>
			<button type="button" class="btn btn-primary" onclick="location.href='/delete/${board.bno}'">삭제</button>
			<button type="submit" class="btn btn-primary">수정</button>
			<button type="button" class="btn btn-primary" style="float: right" onclick="location.href='/list'">목록</button>
		</form>
	</div>
	<br></br>	
	<div class="container">
		<label for="content">comment</label>
		<form name="commentInsertForm">
			<input type="hidden" name="bno" value="${board.bno}" />
			<div class="form-row">
				<div class="form-group col-md-9">
					<input type="text" class="form-control" id="content" name="content" placeholder="내용을 입력하세요.">
				</div>
				<div class="form-group col-md-2">
					<input type="text" class="form-control" id="writer" style="display: inline" name="writer" placeholder="작성자">
				</div>
			</div>
			<span class="input-group-btn">
				<button class="btn btn-default" type="button" name="commentInsertBtn">등록</button>
			</span>
		</form>
	</div>

	<div class="container">
		<div class="commentList"></div>
	</div>
	<%@ include file="comment.jsp"%>
	<%@ include file="bootstrap.jsp"%>
</body>
</html>