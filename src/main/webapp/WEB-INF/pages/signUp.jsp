
<!DOCTYPE html>
<html>
<head>
    <title>Garuda Service Portal</title>
    <!-- Favicon -->
    <link rel="shortcut icon" href="/resources/favicon.ico">
    <link rel="stylesheet" media="screen" href="/resources/bootstrap/css/bootstrap.min.css" />
    <link rel="stylesheet" media="screen" href="/resources/css/login.css" />
    <script src="/resources/jquery/jquery-1.11.3.min.js"></script>
    <script src="/resources/js/jquery.validate.min.js"></script>

    <meta content='width=device-width' name='viewport'>
    <script>
    $(document).ready(function() {
        $("#orgId").on("focusout", checkOrganization);

        function checkOrganization() {
            var orgId = $("#orgId").val();
//            console.log("checkOrganization : ", orgId);
            $.ajax({
                url : "/api/organization/"+orgId,
                async: false,
                type : "GET",
                dataType : "json",
                success : function(response) {
                    var orgName = response.name;
                    $("#orgName").val(orgName);
                    $("#orgName").attr("readonly", "readonly");
                    console.log("org success : ", response, orgName);
                },
                error : function(jqXHR, textStatus, errorThrown) {
//                    console.log("org error : ", textStatus, errorThrown);
                    $("#orgName").val("");
                    $("#orgName").removeAttrs("readonly");
                }
            });
        }

        $("#signup-form").validate({
            onkeyup: function(element) {
                var element_id = $(element).attr('id');
                if (this.settings.rules[element_id] && this.settings.rules[element_id].onkeyup != false) {
                    $.validator.defaults.onkeyup.apply(this, arguments);
                }
            },
            rules: {
                userId: {
                    idExists: true,
                    onkeyup: false
                },
                password2: {
                    equalTo: "#password"
                }
            }
        });

        $.validator.addMethod("idExists", function(value, element) {
            var ret = true;
            $.ajax({
                url : "/api/user/" + value,
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
        }, "This e-mail already exists.");
    });


    </script>
</head>
<body class='login'>

    <img class="logo" src="/resources/css/oce.png" alt="Garuda logo" />
    <form class="vertical-form" id="signup-form" action="/signUp" accept-charset="UTF-8" method="post">
        <legend>Sign Up</legend>
        <h4>Organization</h4>
        <input placeholder="Organization ID" type="text" class="required" name="orgId" minlength="3" id="orgId" />
        <input placeholder="Organization Name" type="text" class="required" name="orgName" id="orgName" />
        <h4>Personal</h4>
        <input placeholder="Email Address" type="text" class="required email" name="userId" id="userId" />
        <input placeholder="Password" autocomplete="off" type="password" class="required" minlength="4" name="password" id="password" />
        <input placeholder="Password Again" autocomplete="off" type="password" class="required" minlength="4" name="password2" id="password2" />
        <input type="submit" class="btn btn-success" name="commit" value="Create Account" />
        <%--<p><a href="/forgot_password/new">Forgot password?</a></p>--%>
    </form>
    <div class='footer'>
        <p>
            Don't have an account?
            <a href="/login">Log In</a>
        </p>
    </div>

</body>
</html>
