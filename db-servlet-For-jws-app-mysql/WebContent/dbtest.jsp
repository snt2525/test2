<%@page import="dao.ConnectDB"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>



<%

ConnectDB  con =new ConnectDB();

out.println("DB confirm - " +con.getConncetion());

%>
</body>
</html>