<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Hello World</title>
</head>
<body>
<h1>Hello ${not empty param.name ? param.name : 'World'}</h1>
<%
out.println(new Date());
out.println(request.getParameter("name"));
%>
<c:if test="${empty param.name}">
Kein Parameter angegeben.
</c:if>
</body>
</html>