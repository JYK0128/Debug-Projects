<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert Form</title>
</head>
<body>
	<h2>게시글 작성</h2>
	<div class="container">
		<form action="/insert" method="post" enctype="multipart/form-data">
			<div class="form-group">
				<label for="title">제목</label>
				<input type="text" class="form-control" name="title" placeholder="제목을 입력하세요.">
			</div>
			<div class="form-group">
				<label for="writer">작성자</label>
				<input type="text" class="form-control" name="writer" placeholder="내용을 입력하세요.">
			</div>
			<div class="form-group">
				<label for="contents">내용</label>
				<textarea class="form-control" name="contents" rows="3"></textarea>
			</div>
			<div class="form-group">
				<input multiple="multiple" type="file" name="files">
			</div>

			<button type="reset" class="btn btn-primary">초기화</button>
			<button type="button" class="btn btn-primary" onclick="location.href='/list'">취소</button>
			<button type="submit" class="btn btn-primary">작성</button>
		</form>
	</div>
	<%@ include file="bootstrap.jsp"%>
</body>
</html>