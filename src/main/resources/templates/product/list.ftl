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
                    <table class="table table-hover">
                        <thead>
                        <tr>
                            <th>商品id</th>
                            <th>名称</th>
                            <th>图片</th>
                            <th>单价</th>
                            <th>库存</th>
                            <th>描述</th>
                            <th>类目</th>
                            <th>创建时间</th>
                            <th>修改时间</th>
                            <th colspan="2">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list pageInfo.list as list>
                        <tr>
                            <td id="productId_${list.productId}">${list.productId}</td>
                            <td id="name_${list.productId}">${list.productName}</td>
                            <td><img height="100" width="100" src="${list.productIcon}" id="icon_${list.productId}">
                            </td>
                            <td id="price_${list.productId}">${list.productPrice}</td>
                            <td id="stock_${list.productId}">${list.productStock}</td>
                            <td id="desc_${list.productId}">${list.productDescription}</td>
                            <td id="category_${list.productId}">${list.categoryName}</td>
                            <td>${list.createTime?string('yyyy-MM-dd HH:mm:ss')}</td>
                            <td id="updateTime_${list.productId}">${list.updateTime?string('yyyy-MM-dd HH:mm:ss')}</td>
                            <td><a id="${list.productId}" data-toggle="modal" data-target="#myModal" role="button"
                                   class="change">修改</a></td>

                            <#if list.productStatus == 0>
                                <td><a href="/seller/product/changeStatus?productId=${list.productId}&productStatus=1">下架</a>
                                </td>
                            <#elseif list.productStatus == 1>
                                <td><a href="/seller/product/changeStatus?productId=${list.productId}&productStatus=0">上架</a>
                                </td>
                            </#if>
                        </tr>
                        </#list>
                        </tbody>
                    </table>

                </div>

            <#--分页-->
                <div class="col-md-12 column">
                    <ul class="pagination pull-right">
                    <#if pageInfo.hasPreviousPage>
                        <li>
                            <a href="/seller/product/list?pageNum=${pageInfo.prePage}&pageSize=${pageInfo.pageSize}">上一页</a>
                        </li>
                    </#if>
                    <#list pageInfo.navigatepageNums as page>
                        <#if pageInfo.pageNum == page>
                            <li class="disabled">
                                <a>${page}</a>
                            </li>
                        <#else >
                            <li>
                                <a href="/seller/product/list?pageNum=${page}&pageSize=${pageInfo.pageSize}">${page}</a>
                            </li>
                        </#if>
                    </#list>
                    <#if pageInfo.hasNextPage>
                        <li>
                            <a href="/seller/product/list?pageNum=${pageInfo.nextPage}&pageSize=${pageInfo.pageSize}">下一页</a>
                        </li>
                    </#if>
                    </ul>
                </div>
            </div>
        </div>
    </div>

    <div class="modal fade" id="myModal" role="dialog" aria-labelledby="myModalLabel"
         aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                    <h4 class="modal-title" id="myModalLabel">
                        修改商品信息
                    </h4>
                </div>
                <div class="modal-body">
                    商品id<input type="text" class="form-control" value="11111" id="changeId" disabled="disabled">
                </div>
                <div class="modal-body">
                    名称<input type="text" class="form-control" value="11111" id="changeName">
                </div>
                <div class="modal-body">
                    <form id="upload" enctype="multipart/form-data" method="post" action="/api/upload/image">
                        图片<img id="changeImg" height="100" width="100"
                               src="http://owioow1ef.bkt.clouddn.com/1505889358794_22.png">
                        <input id="file_upload" name="file_upload" type="file" multiple="true"
                               style="float: right;">
                    </form>
                </div>
                <div class="modal-body">
                    价格<input type="text" class="form-control" value="11111" id="changePrice">
                </div>
                <div class="modal-body">
                    库存<input type="text" class="form-control" value="11111" id="changeStock">
                </div>
                <div class="modal-body">
                    描述<input type="text" class="form-control" value="11111" id="changeDesc">
                </div>
                <div class="modal-body">
                    类目<select type="text" class="form-control" id="changeCategory">
                </select>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary" id="save">保存</button>
                </div>
            </div>

        </div>

    </div>

</div>

<script src="https://cdn.bootcss.com/jquery/2.1.4/jquery.min.js"></script>
<script src="https://cdn.bootcss.com/bootstrap/3.0.1/js/bootstrap.min.js"></script>
<script type="text/javascript" src="/js/pekeUpload.js"></script>
<script>
    $('.change').click(function () {
        var id = $(this).attr('id');
        var oldProductInfo = {
            id: $('#productId_' + id).text(),
            name: $('#name_' + id).text(),
            icon: $('#icon_' + id).attr('src'),
            price: $('#price_' + id).text(),
            desc: $('#desc_' + id).text(),
            stock: $('#stock_' + id).text(),
            category: $('#category_' + id).text()
        };
        console.log(oldProductInfo);
        //弹层加载修改之前的数据
        loadOldProductInfo(oldProductInfo);
        loadCategory(oldProductInfo);

    });

    /**
     * 加载该商品的信息
     * @param oldProductInfo 修改之前商品的信息
     */
    function loadOldProductInfo(oldProductInfo) {
        $('#changeId').val(oldProductInfo.id);
        $('#changeName').val(oldProductInfo.name);
        $('#changeImg').attr('src', oldProductInfo.icon);
        $('#changePrice').val(oldProductInfo.price);
        $('#changeStock').val(oldProductInfo.stock);
        $('#changeDesc').val(oldProductInfo.desc);
    }

    /**
     * ajax加载所有分类名称
     */
    function loadCategory(oldProductInfo) {
        $.ajax({
            url: '/buyer/product/getAllCategoryName',
            type: 'GET',
            dataType: 'json',
            success: function (res) {
                $('#changeCategory').empty();
                for (var i = 0; i < res.data.length; i++) {
                    var option = "<option value=" + res.data[i] + ">" + res.data[i] + "</option>";
                    $('#changeCategory').append(option);
                }
                ;
                $('#changeCategory').val(oldProductInfo.category);
            },
            error: function (err) {
                alert(err);
            }


        });
    }

    /**
     * 使用ajax上传图片
     */
    $("#file_upload").pekeUpload();

    $('#save').click(function () {

        var fromData = {
            productId: $('#changeId').val(),
            productName: $('#changeName').val(),
            productIcon: $('#changeImg').attr('src'),
            productPrice: $('#changePrice').val(),
            productStock: $('#changeStock').val(),
            productDescription: $('#changeDesc').val(),
            categoryName: $('#changeCategory').val()
        };

        $.ajax({
            url: '/api/seller/updateProduct',
            type: 'POST',
            dataType: 'JSON',
            data: fromData,
            success: function (res) {
                console.log(res);
                //隐藏弹框，更新列表
                updateProductList(fromData.productId);
            },
            error: function (err) {
                console.log(err);
            }
        });
    });

    function updateProductList(productId) {
        $('#myModal').modal('toggle');
        var ajaxData = {
            productId: productId,
        };
        $.ajax({
            url: '/api/seller/getProduct',
            type: 'GET',
            dataType: 'JSON',
            data: ajaxData,
            success: function (res) {
                console.log(res.data);
                //更新列表中的数据
                $('#name_' + productId).html(res.data.productName);
                $('#icon_' + productId).attr('src', res.data.productIcon);
                $('#price_' + productId).html(res.data.productPrice);
                $('#stock_' + productId).html(res.data.productStock);
                $('#desc_' + productId).html(res.data.productDescription);
                $('#category_' + productId).val(res.data.categoryName);
                $('#updateTime_' + productId).html(dateFormat(res.data.updateTime));

            },
            error: function (err) {
                alert(err);
            }
        })
    }

    //时间格式化
    function dateFormat(date) {
        var date = new Date(date);
        Y = date.getFullYear() + '-';
        M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
        D = date.getDate() + ' ';
        h = date.getHours() + ':';
        m = date.getMinutes() + ':';
        s = date.getSeconds();
        return Y+M+D+h+m+s;
    }
</script>
</body>
</html>