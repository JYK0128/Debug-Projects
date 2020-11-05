<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h2>게시글 수정</h2>
	<div class="container">
		<form action="/update/${board.bno}" method="POST" enctype="multipart/form-data">
			<div class="form-group">
				<label for="title">제목</label>
				<input type="text" class="form-control" name="title" value="${board.title}" placeholder="제목을 입력하세요.">
			</div>
			<div class="form-group">
				<label for="writer">작성자</label>
				<input type="text" class="form-control" name="writer" value="${board.writer}" placeholder="내용을 입력하세요.">
			</div>
			<div class="form-group">
				<label for="contents">내용</label>
				<textarea class="form-control" name="contents" rows="3">${board.contents}</textarea>
			</div>
			<div class="form-group">
				<label>첨부파일</label>
				<c:forEach var="file" items="${list}" varStatus="status">
					<p>
						<a href="/download/${board.bno}/${file.UUID}/${file.name}">${file.name}</a>
						<input type="checkbox" class="close-check" id="${file.UUID}" />
						<label class="close-label" for="${file.UUID}">X</label>

						<input type="hidden" name="fileList[${status.index}].bno" value="${file.bno}">
						<input type="hidden" name="fileList[${status.index}].name" value="${file.name}">
						<input type="hidden" name="fileList[${status.index}].UUID" value="${file.UUID}">
					</p>
				</c:forEach>
			</div>
			<div class="form-group">
				<input multiple="multiple" type="file" name="files">
			</div>

			<button type="reset" class="btn btn-primary">초기화</button>
			<button type="button" class="btn btn-primary" onclick="location.href='/detail/${board.bno}'">취소</button>
			<button type="submit" class="btn btn-primary">변경</button>
		</form>
	</div>
	<%@ include file="bootstrap.jsp"%>
	<script>
		var check = document.getElementsByClassName("close-check");

		for (var i = 0; i < check.length; i++) {
			check[i].style.display = "none";
			check[i].addEventListener("change", function() {
				if (this.checked == true) {
					this.parentElement.style.display = "none";
					this.parentElement.innerHTML="";
				}
			})
		}
	</script>
</body>
</html>