<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<% String menuId = "manage"; %>
<%@include file="top.jsp" %>

<script src="/resources/js/appEdit.js"></script>
<script>
$(function(){
    $("#app-new-form").validate({
        onkeyup: function(element) {
            var element_id = $(element).attr('id');
            if (this.settings.rules[element_id] && this.settings.rules[element_id].onkeyup != false) {
                $.validator.defaults.onkeyup.apply(this, arguments);
            }
        },
        rules: {
            id: {
                idExists: true,
                lowercase : true,
                onkeyup: false
            },

            appFile1: {
                required: true
            },
            context1: {
                required: true
            },
            appFile2 : {
                required: function(element){
                    return $("#context2").val().length > 0;
                }
            },
            context2 : {
                required: function(element){
                    return $("#appFile2").val().length > 0;
                }
            }
        },
        messages: {
            appFile1: "Upload file is required.",
            appFile2: "Upload file is required."
        }
    });

    $.validator.addMethod("idExists", function(value, element) {
        var ret = true;
        $.ajax({
            url : "/api/apps/" + value,
            async: false,
            type : "HEAD",
            success : function(response) {
                ret = false;
            },
            error : function() {
                ret = true;
            }
        });
        return ret;
    }, "This app id already exists.");

    $.validator.addMethod("lowercase", function(value) {
        // Marathon documentation에서 가져온 정규식.
        // https://mesosphere.github.io/marathon/docs/rest-api.html#post-v2-apps
        return value.match(/^(([a-z0-9]|[a-z0-9][a-z0-9\\-]*[a-z0-9])\\.)*([a-z0-9]|[a-z0-9][a-z0-9\\-]*[a-z0-9])$/);
    }, 'You must use only lowercase letters, dot and dash in app id');
})

</script>
<div class="container" id="content">
    <div class="row">
        <div class="col-md-12">

            <div class="page-header">
                <h1 id="tables">Create New App</h1>
            </div>

            <form id="app-new-form" action="/o/apps" method="POST">
                <div class="row col-md-12">
                    <a href="/o/manage" class="btn btn-default"><i class="glyphicon glyphicon-arrow-left"></i> List</a>
                    &nbsp;<button type="submit" class="btn btn-primary outline">Save all changes</button>
                </div>
                <div class="row col-md-12">
                    <h4 class="bottom-line">General Information</h4>
                    <div class="col-md-12 form-horizontal">
                        <div class="form-group">
                            <label class="col-md-3 col-sm-3 control-label">Domain:</label>
                            <div class="col-md-9 col-sm-9"><input type="text" name="id" id="appId" class="form-control col-150 pull-left required" minlength="3"/>
                                <p class="form-control-static">&nbsp;.${domain}</p>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-3 col-sm-3 control-label">Name:</label>
                            <div class="col-md-9 col-sm-9"><input type="text" name="name" id="appName" class="form-control required" minlength="3"/></div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-3 col-sm-3 control-label">Description :</label>
                            <div class="col-md-9 col-sm-9"><textarea name="description" class="form-control" rows="3"></textarea></div>
                        </div>
                    </div>
                </div>

                <div class="row col-md-12">
                    <h4 class="bottom-line">Operating Plan</h4>
                    <div class="col-md-12 form-horizontal">
                        <div class="form-group">
                            <label class="col-md-3 col-sm-3 control-label">App file:</label>
                            <div class="col-md-9 col-sm-9">
                                <!--file1-->
                                <p class="form-control-static pull-left">Context</p>
                                <input type="text" id="context1" name="context1" class="form-control col-150 pull-left mleft-10" placeholder="/context" value="/">
                                <input type="file" id="appFile1" name="appFile1" class="form-control-static required col-100 pleft-10 simple-file-btn"/>
                                <p class="form-control-static app-file-detail1 maybe-hide"><span id="fileInfo1"></span>
                                    <br><span class="file-date" id="fileDate1"></span>
                                </p>
                                <!--// file1-->
                                <p/>
                                <!--file2-->
                                <p class="form-control-static pull-left">Context</p>
                                <input type="text" id="context2" name="context2" class="form-control col-150 pull-left mleft-10" placeholder="/context">
                                <input type="file" id="appFile2" name="appFile2" class="form-control-static required col-100 pleft-10 simple-file-btn"/>
                                <p class="form-control-static app-file-detail2 maybe-hide"><span id="fileInfo2"></span>
                                    <br><span class="file-date" id="fileDate2"></span>
                                </p>
                                <!--// file2-->
                                <!--hidden info-->
                                <input type="hidden" name="fileName1"/>
                                <input type="hidden" name="filePath1"/>
                                <input type="hidden" name="fileLength1"/>
                                <input type="hidden" name="fileDate1"/>
                                <input type="hidden" name="fileChecksum1"/>
                                <input type="hidden" name="fileName2"/>
                                <input type="hidden" name="filePath2"/>
                                <input type="hidden" name="fileLength2"/>
                                <input type="hidden" name="fileDate2"/>
                                <input type="hidden" name="fileChecksum2"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 col-sm-3 control-label">Environment:</label>
                            <div class="col-md-9 col-sm-9">
                                <select name="environment" class="form-control required">
                                    <option value="">:: Select ::</option>
                                    <option value="java7_tomcat7">java7_tomcat7</option>
                                    <option value="java7_wildfly8.2">java7_wildfly8.2</option>
                                    <option value="php5_apache2">php5_apache2</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 col-sm-3 control-label">CPUs:</label>
                            <div class="col-md-9 col-sm-9">
                                <select name="cpus" class="form-control col-100 required">
                                    <option value="0.1">0.1</option>
                                    <option value="0.2">0.2</option>
                                    <option value="0.3">0.3</option>
                                    <option value="0.4">0.4</option>
                                    <option value="0.5">0.5</option>
                                    <option value="0.6">0.6</option>
                                    <option value="0.7">0.7</option>
                                    <option value="0.8">0.8</option>
                                    <option value="0.9">0.9</option>
                                    <option value="1.0">1.0</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 col-sm-3 control-label">Memory:</label>
                            <div class="col-md-9 col-sm-9">
                                <select name="memory" class="form-control col-100 required">
                                    <%--<option value="50">50MB</option>--%>
                                    <%--<option value="100">100MB</option>--%>
                                    <%--<option value="200">200MB</option>--%>
                                    <option value="300">300MB</option>
                                    <option value="400">400MB</option>
                                    <option value="500">500MB</option>
                                    <option value="600">600MB</option>
                                    <option value="700">700MB</option>
                                    <option value="800">800MB</option>
                                    <option value="900">900MB</option>
                                    <option value="1000">1GB</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 col-sm-3 control-label">Scale:</label>
                            <div class="col-md-9 col-sm-9">
                                <select name="scale"class="form-control col-100 required">
                                    <option value="1">1</option>
                                    <option value="2">2</option>
                                    <option value="3">3</option>
                                    <option value="4">4</option>
                                    <option value="5">5</option>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="row col-md-12">
                    <h4 class="bottom-line">Resource Plan</h4>
                    <div class="col-md-12 form-horizontal">
                        <div class="form-group">
                            <input type="hidden" name="db_resource_size" value="3" />
                            <label class="col-md-3 col-sm-3 control-label">Database:</label>
                            <div class="col-md-9 col-sm-9">
                                <div class="checkbox">
                                    <label>
                                        <input type="checkbox" name="db0" value="DB01"> DB01 ( MySql 5.6.26 )
                                    </label>
                                </div>
                                <div class="sub-options">
                                    <label class="radio-inline">
                                        <input type="radio" name="db0_option" value="separateDB"> Separate DB
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="db0_option" value="SharedDB" disabled> Shared
                                    </label>
                                </div>
                            </div>
                            <div class="col-md-offset-3 col-sm-offset-3 col-md-9 col-sm-9">
                                <div class="checkbox">
                                    <label>
                                        <input type="checkbox" name="db1" value="DB02" > DB02 ( Oracle 11g )
                                    </label>
                                </div>
                                <div class="sub-options">
                                    <label class="radio-inline">
                                        <input type="radio" name="db1_option" value="separateDB"> Separate DB
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="db1_option" value="separateSchema"> Separate Schema
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="db1_option" value="sharedSchema"> Shared
                                    </label>
                                </div>
                            </div>
                            <div class="col-md-offset-3 col-sm-offset-3 col-md-9 col-sm-9">
                                <div class="checkbox">
                                    <label>
                                        <input type="checkbox" name="db2" value="DB03" > DB03 ( Postgres 9.4.4 )
                                    </label>
                                </div>
                                <div class="sub-options">
                                    <label class="radio-inline">
                                        <input type="radio" name="db2_option" value="separateDB"> Separate DB
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="db2_option" value="separateSchema"> Separate Schema
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="db2_option" value="sharedSchema"> Shared
                                    </label>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <input type="hidden" name="ftp_resource_size" value="1" />
                            <label class="col-md-3 col-sm-3 control-label">FTP:</label>
                            <div class="col-md-9 col-sm-9">
                                <div class="checkbox">
                                    <label>
                                        <input type="checkbox" name="ftp0" value="FTP01"> FTP01 ( Ftp 3.2 )
                                    </label>
                                </div>
                                <div class="sub-options">
                                    <label class="radio-inline">
                                        <input type="radio" name="ftp0_option" value="private"> Private
                                    </label>
                                    <label class="radio-inline">
                                        <input type="radio" name="ftp0_option" value="shared"> Shared
                                    </label>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="row col-md-12">
                    <h4 class="bottom-line">Auto Scaling Plan</h4>
                    <div class="col-md-12 form-horizontal">
                        <div class="form-group">
                            <label class="col-md-3 col-sm-3 control-label">Enable Auto Scale-out:</label>
                            <div class="col-md-9 col-sm-9">
                                <div class="checkbox">
                                    <label>
                                        <input type="checkbox" name="autoScaleOutUse" disabled> Yes
                                    </label>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 col-sm-3 control-label">CPU Usage higher than:</label>
                            <div class="col-md-9 col-sm-9">
                                <select class="form-control display-inline col-100" name="cpuHigher">
                                    <option value="50">50%</option>
                                    <option value="60">60%</option>
                                    <option value="70">70%</option>
                                    <option value="80">80%</option>
                                    <option value="90">90%</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 col-sm-3 control-label">During:</label>
                            <div class="col-md-9 col-sm-9">
                                <select class="form-control display-inline col-100" name="cpuHigherDuring">
                                    <option value="1">1 Min</option>
                                    <option value="2">2 Min</option>
                                    <option value="3">3 Min</option>
                                    <option value="4">4 Min</option>
                                    <option value="5">5 Min</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 col-sm-3 control-label">Add Scale:</label>
                            <div class="col-md-9 col-sm-9">
                                <select class="form-control display-inline col-100" name="autoScaleOutSize">
                                    <option value="1">1</option>
                                    <option value="2">2</option>
                                    <option value="3">3</option>
                                </select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-3 col-sm-3 control-label">Enable Auto Scale-in:</label>
                            <div class="col-md-9 col-sm-9">
                                <div class="checkbox">
                                    <label>
                                        <input type="checkbox" name="autoScaleInUse" disabled> Yes
                                    </label>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 col-sm-3 control-label">CPU Usage lower than:</label>
                            <div class="col-md-9 col-sm-9">
                                <select class="form-control display-inline col-100" name="cpuLower">
                                    <option value="10">10%</option>
                                    <option value="20">20%</option>
                                    <option value="30">30%</option>
                                    <option value="40">40%</option>
                                    <option value="50">50%</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 col-sm-3 control-label">During:</label>
                            <div class="col-md-9 col-sm-9">
                                <select class="form-control display-inline col-100" name="cpuLowerDuring">
                                    <option value="1">1 Min</option>
                                    <option value="2">2 Min</option>
                                    <option value="3">3 Min</option>
                                    <option value="4">4 Min</option>
                                    <option value="5">5 Min</option>
                                    <option value="6">6 Min</option>
                                    <option value="7">7 Min</option>
                                    <option value="8">8 Min</option>
                                    <option value="9">9 Min</option>
                                    <option value="10">10 Min</option>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>


                <div class="row col-md-12">
                    <hr>
                    <div>
                        <button type="submit" class="btn btn-primary outline">Save all changes</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>

<%@include file="bottom.jsp" %>