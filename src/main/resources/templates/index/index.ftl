<html>
<#include "../common/header.ftl">
<body>

<div class="container">
    <div class="row clearfix">
        <div class="col-md-12 column">
            <label for="inputEmail3" class="col-sm-2 control-label">Username</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" id="username"/>
            </div>
            <label for="inputPassword3" class="col-sm-2 control-label">Password</label>
            <div class="col-sm-10">
                <input type="password" class="form-control" id="password"/>
            </div>
            <div class="col-sm-offset-2 col-sm-10">
                <button class="btn btn-default" id="login">login</button>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.bootcss.com/jquery/2.1.4/jquery.min.js"></script>
<script>
    $('#login').click(function () {
        var loginData = {
            username: $('#username').val(),
            password: $('#password').val()
        };

        $.ajax({
            url: '/api/seller/user/login',
            type: 'POST',
            dataType: 'JSON',
            data: loginData,
            success: function (res) {
                console.log(res);
                if (res.status === 0){
                    location.href="/seller/order/list";
                }
            },
            error: function (err) {
                console.log(err);
            }
        });
    });


</script>
</body>

</html>