package com.gandazhi.sell.vo;

import lombok.Data;

import java.util.List;

@Data
public class CartInfoVo {

    private List<CartVo> cartVoList;

    private Integer cartQuantity;
}
