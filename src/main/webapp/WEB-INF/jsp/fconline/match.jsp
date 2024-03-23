<%@page import="com.google.gson.JsonElement"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="com.google.gson.JsonArray"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" href="/resources/cham.css" >
    <meta charset="UTF-8">
    <title>공식경기전적</title>
</head>
<body>
<div class="title-box">
    피파
</div>
<hr>
<h1>
    매치 경기
</h1>
<hr>
<a href="/">처음으로 가기</a>
<hr>
유저: ${serverName}
<form action="/fconline/match" method="get">
    <input name="word">
    <input type="submit" value="검색">
</form>
<hr>
최근 전적<br>
<script>
    // JSON 형식의 문자열 파싱
    var serverName = "${serverName}"; // JSP 변수를 JavaScript 변수로 가져오기
    var jsonData = ${matches}; // 모델에서 전달된 matches 변수 사용

    // 리스트 형식으로 데이터 표시
    document.write("<ul>");
    for (var i = 0; i < jsonData.length; i++) {
        var matchId = jsonData[i];
        var matchLink = "/fconline/matchDetail?jsonData=" + matchId + "&word=" + encodeURIComponent(serverName);
        document.write("<li><a href='" + matchLink + "'>최근 경기 " + (jsonData.length - i) + "</a></li>");
    }
    document.write("</ul>");
</script>
</body>
</html>