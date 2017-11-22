package com.gandazhi.sell.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gandazhi.sell.common.OrderStatus;
import com.gandazhi.sell.common.PayStatus;
import com.gandazhi.sell.util.EnumUtil;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class SellerOrderVo {

    private String orderId;
    private String buyerName;
    private String buyerPhone;
    private String buyerAddress;
    private BigDecimal orderAmount;
    private Integer orderStatus;
    private Integer payStatus;
    private Date createTime;

    //用于在模板中获取orderStatus的枚举类
    @JsonIgnore //不序列化
    public OrderStatus getOrderStatusEnum(Integer orderStatus){
        return EnumUtil.getByCode(orderStatus, OrderStatus.class);
    }

    //用于在模板中获取payStatus的枚举类
    @JsonIgnore
    public PayStatus getPayStatusEnum(Integer payStatus){
        return EnumUtil.getByCode(payStatus, PayStatus.class);
    }
}
