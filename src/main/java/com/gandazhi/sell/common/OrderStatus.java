package com.gandazhi.sell.common;

public enum OrderStatus {
    NEW(0, "新订单"),
    FINISHED(1, "已完成"),
    CANCEL(2, "已取消");

    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    OrderStatus(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
