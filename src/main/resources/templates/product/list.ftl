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
                            <td>${list.productId}</td>
                            <td>${list.productName}</td>
                            <td><img src="${list.productIcon}"></td>
                            <td>${list.productPrice}</td>
                            <td>${list.productStock}</td>
                            <td>${list.productDescription}</td>
                            <td>${list.categoryName}</td>
                            <td>${list.createTime?string('yyyy-MM-dd hh:mm:ss')}</td>
                            <td>${list.updateTime?string('yyyy-MM-dd hh:mm:ss')}</td>
                            <td><a href="">详情</a></td>
                            <#if list.productStatus == 0>
                                <td><a href="/seller/product/changeStatus?productId=${list.productId}&productStatus=1">下架</a></td>
                            <#elseif list.productStatus == 1>
                                <td><a href="/seller/product/changeStatus?productId=${list.productId}&productStatus=0">上架</a></td>
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

</div>
</body>
</html>