<html>
<#include "../common/header.ftl">

<body>
<div id="wrapper" class="toggled">

<#--边栏sidebar-->
<#include "../common/nav.ftl">

<#--主要内容content-->
    <div id="page-content-wrapper">
        <div class="container-fluid">
            <div class="row clearfix">
                <div class="col-md-12 column">
                    <form id="fromData">
                        <div class="form-group">
                            <label>名字</label>
                            <input name="categoryName" type="text" class="form-control" value=""/>
                        </div>
                        <div class="form-group">
                            <label>type</label>
                            <input name="categoryType" type="number" class="form-control" value=""/>
                        </div>
                        <button class="btn btn-default" id="submitData">提交</button>
                    </form>
                </div>
            </div>
        </div>
    </div>

</div>
<script src="https://cdn.bootcss.com/jquery/2.1.4/jquery.min.js"></script>
<script src="https://cdn.bootcss.com/bootstrap/3.0.1/js/bootstrap.min.js"></script>
<script>
    $('#submitData').click(function () {
        var sellerProductCategoryDto = $('#fromData').serialize();
        $.ajax({
            url: '/api/seller/category/addProductCategory',
            dataType: 'JSON',
            type: 'POST',
            data: sellerProductCategoryDto,
            async: false,
            success: function (res) {
                alert(res.msg);
            },
            error: function (err) {
                alert(err);
            }
        });
    });
</script>
</body>
</html>