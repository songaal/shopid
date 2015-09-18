<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<% String menuId = "store"; %>
<%@include file="top.jsp" %>
<script>
    function subscribeApp(appId) {
        $.ajax({
            url:"/api/subscribe/"+appId,
            type: "POST",
            success:function(data) {
                alert(data);
            },
            error:function(xhr) {
                alert("Subscription fails : " + xhr.responseText);
            }
        });
    }

</script>
<div class="container" id="content">
    <div class="row">
        <div class="col-md-12">

            <div class="page-header">
                <h1 id="tables">Store</h1>
            </div>

            <c:if test="${not empty appList}">
                <div class="app-grid-list">
                    <c:forEach var="app" items="${appList}">
                        <div class="app-entry-wrapper col-md-4 col-sm-6 col-xs-6">
                            <div class="app-entry col-md-4 col-sm-6 col-xs-6">
                                <div class="app-thumbnail">
                                    <%--<image src="resources/img/default-thumbnail.jpg"></image>--%>
                                </div>
                                <div class="app-info">
                                    <div class="app-title">${app.name}</div>
                                    <div class="app-provider">${app.orgName}</div>
                                    <div class="app-date">${app.updateDateDisplay}&nbsp;</div>
                                    <div class="app-button" align="right">
                                        <c:if test="${not empty sessionScope._user}">
                                            <a href="javascript:subscribeApp('${app.id}')" class="btn btn-primary outline">Subscribe</a>
                                        </c:if>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:if>

            <c:if test="${empty appList}">
                <div class="well col-md-12 empty-apps">
                    <h3>No apps</h3>
                </div>
            </c:if>
        </div>
    </div>
</div>

<%@include file="bottom.jsp" %>
