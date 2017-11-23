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
                            <th>订单id</th>
                            <th>姓名</th>
                            <th>手机号</th>
                            <th>地址</th>
                            <th>金额</th>
                            <th>订单状态</th>
                            <th>支付状态</th>
                            <th>创建时间</th>
                            <th colspan="2">操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        <#list pageInfo.list as list>
                        <tr>
                            <td>${list.orderId}</td>
                            <td>${list.buyerName}</td>
                            <td>${list.buyerPhone}</td>
                            <td>${list.buyerAddress}</td>
                            <td>${list.orderAmount}</td>
                            <td>${list.getOrderStatusEnum(list.orderStatus).msg}</td>
                            <td>${list.getPayStatusEnum(list.payStatus).msg}</td>
                            <td>${list.createTime?string('yyyy-MM-dd hh:mm:ss')}</td>
                            <td><a href="/seller/order/detail?orderId=${list.orderId}">详情</a></td>
                            <#if list.orderStatus == 0>
                                <td><a href="/seller/order/cancel?orderId=${list.orderId}">取消</a></td>
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
                            <a href="/seller/order/list?pageNum=${pageInfo.prePage}&pageSize=${pageInfo.pageSize}">上一页</a>
                        </li>
                    </#if>
                    <#list pageInfo.navigatepageNums as page>
                        <#if pageInfo.pageNum == page>
                            <li class="disabled">
                                <a>${page}</a>
                            </li>
                        <#else >
                            <li>
                                <a href="/seller/order/list?pageNum=${page}&pageSize=${pageInfo.pageSize}">${page}</a>
                            </li>
                        </#if>
                    </#list>
                    <#if pageInfo.hasNextPage>
                        <li>
                            <a href="/seller/order/list?pageNum=${pageInfo.nextPage}&pageSize=${pageInfo.pageSize}">下一页</a>
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