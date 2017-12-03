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
                    <div class="form-group">
                        <label>类目</label>
                        <select name="categoryName" class="form-control" id="category">

                        </select>
                    </div>

                    <div class="form-group">
                        <label>图片</label>
                        <img id="changeImg" height="100" width="100"
                             src="">
                        <input id="file_upload" name="file_upload" type="file" multiple="true"
                               style="float: right;">
                    </div>
                    <form role="form" method="post" action="/sell/seller/product/save">
                        <div class="form-group">
                            <label>名称</label>
                            <input name="productName" type="text" class="form-control"/>
                        </div>
                        <div class="form-group">
                            <label>价格</label>
                            <input name="productPrice" type="text" class="form-control"/>
                        </div>
                        <div class="form-group">
                            <label>库存</label>
                            <input name="productStock" type="number" class="form-control"/>
                        </div>
                        <div class="form-group">
                            <label>描述</label>
                            <input name="productDescription" type="text" class="form-control"/>
                        </div>
                        <input id="productIcon" name="productIcon" hidden>
                        <input id="categoryType" name="categoryType" hidden>
                    <#--<div class="form-group">-->
                    <#--<label>类目</label>-->
                    <#--<select name="categoryType" class="form-control" id="category">-->

                    <#--</select>-->
                    <#--</div>-->
                        <button type="submit" class="btn btn-default" formaction="/seller/product/createProduct"
                                id="save">提交
                        </button>
                    </form>
                </div>
            </div>
        </div>
    </div>

</div>
</body>
<script src="https://cdn.bootcss.com/jquery/2.1.4/jquery.min.js"></script>
<script type="text/javascript" src="/js/pekeUpload.js"></script>
<script>
    $('#file_upload').pekeUpload();

    /**
     * 用ajax给select添加值
     */
    $.ajax({
        url: '/buyer/product/getAllCategoryName',
        type: 'GET',
        async: false,
        dataType: 'json',
        success: function (res) {
            for (var i = 0; i < res.data.length; i++) {
                var option = "<option value=" + res.data[i] + ">" + res.data[i] + "</option>";
                $('#category').append(option);
            }
            $("#category option:first").prop("selected", true);
            console.log($('#category option:selected').text());
        },
        error: function (err) {
            alert(err);
        }
    });

    /**
     * 根据分类名更新input的值
     * var categoryName = {
     *      categoryName: "categoryName",
     * }
     * @param categoryName
     */
    function getCategoryType(categoryName) {
        $.ajax({
            url: '/api/seller/getCategoryType',
            type: 'GET',
            dataType: 'JSON',
            async: false,
            data: categoryName,
            success: function (res) {
                //更新categoryType的input
                $('#categoryType').val(res.data);
                console.log($('#categoryType').val());
            },
            error: function (err) {
                alert(err);
            }
        })
    }

    var category = {
        categoryName: $('#category option:selected').text(),
    };
    getCategoryType(category);

    $('#category').change(function () {
        var newCategoryName = {
            categoryName: $(this).children('option:selected').val(),
        };
        getCategoryType(newCategoryName);
    });

    $('#save').click(function () {
        //设置提交productIcon的input
        var inputValue = $('#changeImg').attr('src');
        console.log(inputValue);
        $('#productIcon').val(inputValue);
    });


</script>
</html>