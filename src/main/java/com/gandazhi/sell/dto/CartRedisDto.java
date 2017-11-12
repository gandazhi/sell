package com.gandazhi.sell.dto;

import lombok.Data;

import java.util.*;

@Data
public class CartRedisDto {

    private String openId;
    private String productId;
    private Integer quantity;
    private Date createTime;
    private Date updateTime;

}
