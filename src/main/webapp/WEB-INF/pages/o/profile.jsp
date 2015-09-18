<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<% String menuId = "profile"; %>
<%@include file="top.jsp" %>

<script>
    $(function() {
        $("#deleteAccountButton").on("click", function(){
            $("#delete-account-form").submit();
        });
        var message = "${param.message}";
        if(message != "") {
            alert(message);
        }
    });
</script>

<div class="container" id="content">
    <div class="row">
        <div class="col-md-12">

            <div class="page-header">
                <h1 id="tables">My Profile</h1>
            </div>

            <div>
                <%--<div class="pull-right">--%>
                    <%--<a href="#" class="btn btn-primary outline">Edit</a>--%>
                <%--</div>--%>
                <%--<i class="label label-default">User</i>--%>
                <p>${user.id} <i class="label label-primary">${userType}</i></p>
                <p>${orgName}</p>
                <p>Member Since ${joinDate}</p>
            </div>

            <br>

            <div class="box" >
                <div class="pull-right">
                    <button type="button" class="btn btn-lg btn-danger outline" data-toggle="modal" data-target="#deleteAccountModal">Delete Account</button>
                </div>
                <h2>Delete Account</h2>
                <p>This will permanently delete all account information.</p>
            </div>

        </div>
    </div>
</div>

<div class="modal fade" id="deleteAccountModal" tabindex="-1" role="dialog" >
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">Are you sure?</h4>
            </div>
            <div class="modal-body">
                <p>This will permanently delete all account information.</p>
                <p><strong class="text-danger">Delete account "${user.id}".</strong></p>
                <form id="delete-account-form" action="/account/${user.id}/delete" method="post">
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">No</button>
                <button type="button" class="btn btn-danger" id="deleteAccountButton">Delete</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->

<%@include file="bottom.jsp" %>
