<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<link rel="stylesheet" href="/resources/cham.css" >
	<meta charset="UTF-8">
	<title>전적상세확인</title>
</head>
<body>
<div class="title-box">
	피파
</div>
<hr>
<h1>경기 결과</h1>
<hr>
<%
	String previousPage = (String) session.getAttribute("previousPage");
%>
<form id="previousPageForm" action="match" method="get">
	<!-- 세션에 저장된 이전 페이지의 URL을 폼 필드에 추가합니다. -->
	<input type="hidden" name="previousPage" value="${previousPage}">
	<input type="hidden" name="word" value="${word}">
	<!-- 필요한 경우 추가 데이터를 세션에 저장하여 전송할 수 있습니다. -->
	<input type="hidden" name="additionalData" value="value">
	<!-- 이전 화면으로 이동하는 버튼을 추가합니다. -->
	<button type="submit">이전화면으로 가기</button>
</form>
<hr>

<%-- tests 모델에 있는 JSON 데이터를 파싱하여 정보를 뽑아냅니다. --%>
<% String jsonData = (String) request.getAttribute("tests"); %>
<% com.google.gson.JsonParser parser = new com.google.gson.JsonParser(); %>
<% com.google.gson.JsonObject jsonObject = parser.parse(jsonData).getAsJsonObject(); %>

<%-- matchInfo 배열에서 각 객체의 정보를 반복하여 출력합니다. --%>
<% com.google.gson.JsonArray matchInfoArray = jsonObject.getAsJsonArray("matchInfo"); %>
<% for (com.google.gson.JsonElement matchInfoElement : matchInfoArray) { %>
<% com.google.gson.JsonObject matchInfoObject = matchInfoElement.getAsJsonObject(); %>

<%-- 각 객체의 정보를 뽑아냅니다. --%>
<% String ouid = matchInfoObject.get("ouid").getAsString(); %>
<% String nickname = matchInfoObject.get("nickname").getAsString(); %>
<% String matchResult = matchInfoObject.getAsJsonObject("matchDetail").get("matchResult").getAsString(); %>
<% int possession = matchInfoObject.getAsJsonObject("matchDetail").get("possession").getAsInt(); %>
<% int shootTotal = matchInfoObject.getAsJsonObject("shoot").get("shootTotal").getAsInt(); %>
<% int effectiveShootTotal = matchInfoObject.getAsJsonObject("shoot").get("effectiveShootTotal").getAsInt(); %>
<% int goalTotal = matchInfoObject.getAsJsonObject("shoot").get("goalTotal").getAsInt(); %>

<%-- 뽑아낸 정보를 HTML 형식으로 출력합니다. --%>
<div>
	<p>유저: <%= nickname %></p>
	<p>경기결과: <%= matchResult %></p>
	<p>점유율: <%= possession %></p>
	<p>총 슈팅수: <%= shootTotal %></p>
	<p>유효 슈팅수: <%= effectiveShootTotal %></p>
	<p>총 골수: <%= goalTotal %></p>
	<hr>
</div>
<% } %>
</body>
</html>