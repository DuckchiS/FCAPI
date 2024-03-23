<%@page import="java.net.URLEncoder"%>
<%@page import="java.net.HttpURLConnection"%>
<%@page import="java.net.URL"%>
<%@page import="com.google.gson.JsonParser"%>
<%@page import="com.google.gson.JsonObject"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.io.IOException"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" href="/resources/cham.css" >
    <meta charset="UTF-8">
    <title>유저</title>
</head>
<body>
<div class="title-box">
    피파
</div>
<a href="/">처음으로 가기</a>
<form action="/fconline/fc" method="post">
    <input name="word">
    <input type="submit" value="검색">
</form>
<hr>
유저: ${serverName}
<hr>
최고티어: ${maxDivision }
<hr>
레벨: ${characterLevel}
<hr>
</body>
</html>