<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<% String menuId = "manage"; %>
<%@include file="top.jsp" %>
<script>
    function deployApp() {
        $.ajax({
            url:"/api/apps/${app.id}/deploy",
            type: "POST",
            success:function() {
                alert("[${app.id}] app Deploy Success.");

            },
            error:function(xhr) {
                alert("Deploy app fails : " + xhr.responseText);
            },
            complete: function(){
                $("#deployButton").button("reset");
                updateAppStatus();
            }
        });
    }

    $(function(){
        $("#deployButton").on("click", function(){
            $(this).button('loading');
            deployApp();
        });
        $("#scaleConfirmButton").on("click", function(){

            $.ajax({
                url : "/api/apps/${app.id}/scale/" + $("#scaleSize").val(),
                type : "POST",
                success : function() {
                    alert("Scaling started : " + $("#scaleSize").val());
                },
                error : function(xhr) {
                    alert("Scale fails : " + xhr.responseText);
                },
                complete: function(){
                    $("#scaleModal").modal('hide');
                    updateAppStatus();
                }
            });
        });

        updateAppStatus();

    });

    function updateAppStatus() {
        $.ajax({
            url: "/api/apps/${app.id}/status",
            type: "GET",
            dataType: "json",
            success: function(data) {
                $("#appStatus").text(data.status);
                $("#appElapsed").text(data.elapsed);
                $("#appScale").text(data.scale);
            },
            error: function(xhr) {
                alert("App status update error : " + xhr.responseText);
            }
        });
    }
</script>
<div class="container" id="content">
    <div class="row">
        <div class="col-md-12">

            <div class="page-header">
                <h1 id="tables">${app.name}</h1>
            </div>

            <div class="row col-md-12">
                <a href="/o/manage" class="btn btn-default"><i class="glyphicon glyphicon-arrow-left"></i> List</a>
                &nbsp;<a href="${app.id}/edit" class="btn btn-default">Edit</a>
            </div>

            <div class="row col-md-12">
                <br>
                <div class="box" >
                    <div class="pull-right">
                        <button type="button" class="btn btn-lg btn-primary outline" data-toggle="modal" data-target="#scaleModal">Scale</button>
                        &nbsp; <a href="http://${app.id}.${domain}" target="_pop_${app.id}" class="btn btn-lg btn-default">Launch</a>
                        &nbsp; <a href="javascript:void(0)" id="deployButton" class="btn btn-lg btn-primary outline" data-loading-text="Deploying..">Deploy App</a>
                        &nbsp; <a href="javascript:updateAppStatus()" class="btn btn-lg btn-default"><i class="glyphicon glyphicon-refresh"></i></a>
                    </div>
                    <h2>Running Status</h2>
                    <br>
                    <div class="row">
                        <div class="col-md-3 col-sm-3 col-xs-3">
                            <div class="stat-box">
                                <p class="text-success" id="appStatus"></p>
                                <h4>Status</h4>
                            </div>
                        </div>
                        <div class="col-md-3 col-sm-3 col-xs-3">
                            <div class="stat-box">
                                <p class="text-warning" id="appElapsed"></p>
                                <h4>Elapsed</h4>
                            </div>
                        </div>
                        <div class="col-md-3 col-sm-3 col-xs-3">
                            <div class="stat-box">
                                <p class="text-primary" id="appScale"></p>
                                <h4>Scale</h4>
                            </div>
                        </div>
                        <div class="col-md-3 col-sm-3 col-xs-3">
                            <div class="stat-box">
                                <p class="text-danger">OFF</p>
                                <h4>Auto Scale</h4>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row col-md-12">
                <div><h4 class="bottom-line">General Information</h4></div>
                <div class="col-md-12 form-horizontal compact">
                    <div class="form-group">
                        <label class="col-md-3 col-sm-3 control-label">Domain:</label>
                        <div class="col-md-9 col-sm-9"><p class="form-control-static">${app.id}.${domain}</p></div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 col-sm-3 control-label">Name:</label>
                        <div class="col-md-9 col-sm-9"><p class="form-control-static">${app.name}</p></div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 col-sm-3 control-label">Description :</label>
                        <div class="col-md-9 col-sm-9"><p class="form-control-static">${app.description}</p></div>
                    </div>
                </div>
            </div>

            <div class="row col-md-12">
                <div><h4 class="bottom-line">Operating Plan</h4></div>
                <div class="col-md-12 form-horizontal compact">
                    <div class="form-group">
                        <label class="col-md-3 col-sm-3 control-label">App file:</label>
                        <div class="col-md-9 col-sm-9">
                            <p class="form-control-static">Context : ${app.appContext}
                                <br>${app.appFile} ( ${app.appFileLengthDisplay} )
                                <br><i class="file-date">${app.appFileDate}</i>
                            </p>
                        </div>
                        <c:if test="${not empty app.appFile2}">
                        <div class="col-md-offset-3 col-sm-offset-3 col-md-9 col-sm-9">
                            <p class="form-control-static">Context : ${app.appContext2}
                                <br>${app.appFile2} ( ${app.appFileLengthDisplay2} )
                                <br><i class="file-date">${app.appFileDate2}</i>
                            </p>
                        </div>
                        </c:if>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 col-sm-3 control-label">CPUs:</label>
                        <div class="col-md-9 col-sm-9">
                            <p class="form-control-static">${app.cpus}</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 col-sm-3 control-label">Memory:</label>
                        <div class="col-md-9 col-sm-9">
                            <p class="form-control-static">${app.memoryDisplay}</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 col-sm-3 control-label">Scale:</label>
                        <div class="col-md-9 col-sm-9">
                            <p class="form-control-static">${app.scale}</p>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row col-md-12">
                <h4 class="bottom-line">Resource Plan</h4>
                <div class="col-md-12 form-horizontal">
                    <div class="form-group">
                        <label class="col-md-3 col-sm-3 control-label">Database:</label>
                        <div class="col-md-9 col-sm-9">
                            <ul class="no-padding-left">
                                <li><p class="form-control-static">DB01 ( MySql 5.6.26 ) - Separate DB</p></li>
                                <li><p class="form-control-static">DB02 ( Oracle 11g ) - Separate DB</p></li>
                            </ul>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 col-sm-3 control-label">FTP: </label>
                        <div class="col-md-9 col-sm-9">
                            <ul class="no-padding-left">
                                <li><p class="form-control-static">FTP01 ( Ftp 3.2 ) - Private</p></li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row col-md-12">
                <div><h4 class="bottom-line">Auto Scaling Plan</h4></div>
                <div class="col-md-12 form-horizontal compact">
                    <div class="form-group">
                        <label class="col-md-3 col-sm-3 control-label">Enable Auto Scale-out:</label>
                        <div class="col-md-9 col-sm-9">
                            <p class="form-control-static">Yes</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 col-sm-3 control-label">CPU Usage higher than:</label>
                        <div class="col-md-9 col-sm-9">
                            <p class="form-control-static">70%</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 col-sm-3 control-label">During:</label>
                        <div class="col-md-9 col-sm-9">
                            <p class="form-control-static">3 Min</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 col-sm-3 control-label">Add Scale:</label>
                        <div class="col-md-9 col-sm-9">
                            <p class="form-control-static">2</p>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-3 col-sm-3 control-label">Enable Auto Scale-in:</label>
                        <div class="col-md-9 col-sm-9">
                            <p class="form-control-static">Yes</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 col-sm-3 control-label">CPU Usage lower than:</label>
                        <div class="col-md-9 col-sm-9">
                            <p class="form-control-static">30%</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-md-3 col-sm-3 control-label">During:</label>
                        <div class="col-md-9 col-sm-9">
                            <p class="form-control-static">3 Min</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="scaleModal" tabindex="-1" role="dialog" >
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">App scale</h4>
            </div>
            <div class="modal-body">
                <p>This will scale-in or scale-out app.</p>
                <p><strong class="text-primary">Scale app "${app.id}".</strong></p>
                <select id="scaleSize" class="form-control">
                    <option value="0" <c:if test="${app.scale == 0}">selected</c:if>>0</option>
                    <option value="1" <c:if test="${app.scale == 1}">selected</c:if>>1</option>
                    <option value="2" <c:if test="${app.scale == 2}">selected</c:if>>2</option>
                    <option value="3" <c:if test="${app.scale == 3}">selected</c:if>>3</option>
                    <option value="4" <c:if test="${app.scale == 4}">selected</c:if>>4</option>
                    <option value="5" <c:if test="${app.scale == 5}">selected</c:if>>5</option>
                </select>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                <button type="button" class="btn btn-primary outline" id="scaleConfirmButton">Confirm</button>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div><!-- /.modal -->

<%@include file="bottom.jsp" %>