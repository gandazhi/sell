package com.gandazhi.sell.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ProductInfoDto {

    //购物车中每个商品的信息传输用的对象
    private String productId;

    private String productName;

    private BigDecimal productPrice;

    private String productIcon;

    private Date createTime;

    private Date updateTime;

    private Integer productQuantity;//购物车中每个商品购买的数量

}
