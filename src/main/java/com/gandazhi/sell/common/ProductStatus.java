package com.gandazhi.sell.common;

//商品状态枚举
public enum ProductStatus {
    UP(0, "上架"),
    DOWN(1, "下架");

    private Integer code;
    private String desc;

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    ProductStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
