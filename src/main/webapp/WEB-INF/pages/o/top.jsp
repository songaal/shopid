<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <title>Garuda</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <!-- Favicon -->
    <link rel="shortcut icon" href="/resources/favicon.ico">
    <link rel="stylesheet" href="/resources/bootstrap/css/bootstrap.min.css" media="screen">
    <link rel="stylesheet" href="/resources/css/main.css" media="screen">
    <link rel="stylesheet" href="/resources/css/enhance.css" media="screen">
    <script src="/resources/jquery/jquery-1.11.3.min.js"></script>
    <script src="/resources/bootstrap/js/bootstrap.min.js"></script>
    <script src="/resources/js/jquery.validate.min.js"></script>
    <script src="/resources/js/common.js"></script>
</head>
<body>
<div class="navbar navbar-default navbar-fixed-top">
    <div class="container">
        <div class="navbar-header">
            <a href="http://garuda.com/" ><img src="http://www.tsb.co.kr/Ver1/Images/logo.gif"></a>
            <button class="navbar-toggle" type="button" data-toggle="collapse" data-target="#navbar-main">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
        </div>
        <div class="navbar-collapse collapse" id="navbar-main">
            <ul class="nav navbar-nav">
                <li>
                    <a href="/o/apps" class="<%="apps".equals(menuId) ? "current" : ""%>" >Apps</a>
                </li>
                <li>
                    <a href="/o/manage" class="<%="manage".equals(menuId) ? "current" : ""%>" >Manage</a>
                </li>
                <li>
                    <a href="/index">Store</a>
                </li>
            </ul>

            <ul class="nav navbar-nav navbar-right">
                <li class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#" id="themes"><c:out value="${sessionScope._user.id}" /><span class="caret"></span></a>
                    <ul class="dropdown-menu" aria-labelledby="themes">
                        <li><a href="/o/profile">My Profile</a></li>
                        <li><a href="/o/organization">Organization</a></li>
                        <li class="divider"></li>
                        <li><a href="/logout">Log Out</a></li>
                    </ul>
                </li>
                <%--<c:if test="${sessionScope._user.type == 'A'}" >--%>
                <%--<li><a href="/o/settings" class="btn btn-default"><i class="glyphicon glyphicon-cog"></i></a></li>--%>
                <%--</c:if>--%>
            </ul>

        </div>
    </div>
    <%--<div class="col-md-2"></div>--%>
</div>
