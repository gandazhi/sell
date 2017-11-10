package com.gandazhi.sell.common;

import lombok.Getter;

@Getter
public enum RedisIndex {
    CART(0,"购物车");

    private int code;
    private String msg;

    RedisIndex(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
