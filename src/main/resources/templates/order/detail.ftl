<html>
<head>
    <meta charset="UTF-8">
    <title>order-list</title>
    <link href="https://cdn.bootcss.com/bootstrap/3.0.1/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

<#--主要内容content-->
<div id="page-content-wrapper">
    <div class="container">
    <#if response.status = 0>
        <div class="row clearfix">
            <div class="col-md-4 column">
                <table class="table table-bordered">
                    <thead>
                    <tr>
                        <th>订单id</th>
                        <th>订单总金额</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>${response.data.orderId}</td>
                        <td>${response.data.orderAmount}</td>
                    </tr>
                    </tbody>
                </table>
            </div>

        <#--订单详情表数据-->
            <div class="col-md-12 column">
                <table class="table table-bordered">
                    <thead>
                    <tr>
                        <th>商品id</th>
                        <th>商品名称</th>
                        <th>价格</th>
                        <th>数量</th>
                        <th>总额</th>
                    </tr>
                    </thead>
                    <tbody>
                        <#list response.data.productInfoListDtoList as orderDetail>
                        <tr>
                            <td>${orderDetail.productId}</td>
                            <td>${orderDetail.productName}</td>
                            <td>${orderDetail.productPrice}</td>
                            <td>${orderDetail.productQuantity}</td>
                            <td>${orderDetail.productAmount}</td>
                        </tr>
                        </#list>
                    </tbody>
                </table>
            </div>

        <#--操作-->
            <div class="col-md-12 column">
                <#if response.data.orderStatus == 0>
                    <a href="/sell/seller/order/finish?orderId=${response.data.orderId}" type="button"
                       class="btn btn-default btn-primary">完结订单</a>
                    <a href="/sell/seller/order/cancel?orderId=${response.data.orderId}" type="button"
                       class="btn btn-default btn-danger">取消订单</a>
                </#if>
            </div>
        </div>
    <#else >
        <div class="col-md-12 column">
            <div class="alert alert-dismissable alert-danger">
                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
                <h4>
                    错误!
                </h4> <strong>${response.msg}</strong><a href="/seller/order/list" class="alert-link">3s后自动跳转</a>
            </div>
        </div>
    </#if>
    </div>
</div>
</div>

</body>
<script>
    setTimeout('location.href="/seller/order/list', 3000);
</script>
</html>