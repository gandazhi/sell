package com.gandazhi.sell.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 商户后台中一个订单中的商品列表Dto
 */
@Data
public class ProductInfoListDto {

    private String productId;
    private String productName;
    private BigDecimal productPrice;
    private Integer productQuantity;
    private BigDecimal productAmount;
}
