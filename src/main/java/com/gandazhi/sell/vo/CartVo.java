package com.gandazhi.sell.vo;

import lombok.Data;

@Data
public class CartVo {

    private String productId;

    private Integer quantity;

    public CartVo(String productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public CartVo() {
        super();
    }
}
