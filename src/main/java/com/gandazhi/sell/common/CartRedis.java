package com.gandazhi.sell.common;


public enum CartRedis {
    PRODUCT_ID("productId"),
    QUANTITY("quantity"),
    CREATE_TIME("create_time"),
    UPDATE_TIME("update_time"),
    OPEN_ID("openId");


    private String word;

    public String getWord() {
        return word;
    }

    CartRedis(String word) {
        this.word = word;
    }
}
