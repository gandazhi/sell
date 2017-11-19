<html>
<head>
    <meta charset="UTF-8">
    <title>order-list</title>
    <link href="https://cdn.bootcss.com/bootstrap/3.0.1/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container">
    <div class="row clearfix">
        <div class="col-md-12 column">
            <table class="table table-hover">
                <thead>
                <tr>
                    <th>订单id</th>
                    <th>姓名</th>
                    <th>手机号</th>
                    <th>地址</th>
                    <th>总金额</th>
                    <th>订单状态</th>
                    <th>支付状态</th>
                    <th>创建时间</th>
                    <th>操作</th>
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
                    <td>详情</td>
                    <td>取消</td>
                </tr>
                </#list>
                </tbody>
            </table>
        </div>
    </div>
</div>
</body>
</html>
