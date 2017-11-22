package com.gandazhi.sell.vo;

import com.gandazhi.sell.common.OrderStatus;
import com.gandazhi.sell.dto.ProductInfoListDto;
import com.gandazhi.sell.util.EnumUtil;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderDetailVo {

    private String orderId;
    private Integer orderStatus;
    private BigDecimal orderAmount; //订单总金额
    private List<ProductInfoListDto> productInfoListDtoList;

    //在模板中通过code返回枚举
    public OrderStatus getOrderStatus(Integer code){
        return EnumUtil.getByCode(code, OrderStatus.class);
    }
}
