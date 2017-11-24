package com.gandazhi.sell.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author gandazhi
 * @create 2017-11-23 下午9:48
 **/
@Data
public class SellerProductInfoVo {

    private String productId;
    private String productName;
    private String productIcon;
    private BigDecimal productPrice;
    private Integer productStock;
    private String productDescription;
    private Integer productStatus;
    private Integer categoryType;
    private Date createTime;
    private Date updateTime;
    private String categoryName;

    public SellerProductInfoVo(String productId, String productName, String productIcon, BigDecimal productPrice, Integer productStock, String productDescription, Integer productStatus, Integer categoryType, Date createTime, Date updateTime, String categoryName) {
        this.productId = productId;
        this.productName = productName;
        this.productIcon = productIcon;
        this.productPrice = productPrice;
        this.productStock = productStock;
        this.productDescription = productDescription;
        this.productStatus = productStatus;
        this.categoryType = categoryType;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.categoryName = categoryName;
    }
}
