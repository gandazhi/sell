package com.gandazhi.sell.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 新建和更新productInfo时用的对象
 * @author gandazhi
 * @create 2017-12-02 下午1:03
 **/
@Data
public class UpdateProductInfoDto {
    private String productName;
    private String productIcon;
    private BigDecimal productPrice;
    private Integer productStock;
    private String productDescription;
    private String categoryName;
}
