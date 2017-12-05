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
                            <th>类目id</th>
                            <th>名字</th>
                            <th>type</th>
                            <th>创建时间</th>
                            <th>修改时间</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list pageInfo.list as list>
                        <tr>
                            <td id="id_${list.categoryId}">${list.categoryId}</td>
                            <td id="categoryName_${list.categoryId}">${list.categoryName}</td>
                            <td id="categoryType_${list.categoryId}">${list.categoryType}</td>
                            <td>${list.createTime?string('yyyy-MM-dd HH:mm:ss')}</td>
                            <td>${list.updateTime?string('yyyy-MM-dd HH:mm:ss')}</td>
                            <td><a id="${list.categoryId}" data-toggle="modal" data-target="#myModal" role="button"
                                   class="changeCategory">修改</a></td>
                        </tr>
                        </#list>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
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

<#--修改遮罩-->
    <div class="modal fade" id="myModal" role="dialog" aria-labelledby="myModalLabel"
         aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                    <h4 class="modal-title" id="myModalLabel">
                        修改分类信息信息
                    </h4>
                </div>
                <div class="modal-body">
                    id<input type="text" class="form-control" value="11111" id="changeId" disabled="disabled">
                </div>
                <div class="modal-body">
                    名字<input type="text" class="form-control" value="11111" id="changeName">
                </div>
                <div class="modal-body">
                    type<input type="text" class="form-control" value="11111" id="changeType">
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

<script>
    $(document).ready(function () {
        $('.changeCategory').click(function () {
            var id = $(this).attr('id');
            var oldData = {
                oldId: id,
                categoryName: $('#categoryName_' + id).text(),
                categoryType: $('#categoryType_' + id).text(),
            };
            console.log(oldData);
            loadOldData(oldData);
        });

        $('#save').click(function () {
            $('#myModal').modal('toggle');
            var newData = {
                categoryId: $('#changeId').val(),
                categoryName: $('#changeName').val(),
                categoryType: $('#changeType').val()
            };

            $.ajax({
                url: '/api/seller/category/updateProductCategory',
                type: 'POST',
                dataType: 'JSON',
                data: newData,
                success: function (res) {
                    console.log(res);
                    location.reload()
                },
                error: function (err) {
                    console.log(err);
                }
            })
        });
    });

    function loadOldData(oldData) {
        $('#changeId').val(oldData.oldId);
        $('#changeName').val(oldData.categoryName);
        $('#changeType').val(oldData.categoryType);
    }
</script>
</body>

</html>