package com.gandazhi.sell.common;

public enum PayStatus {
    WAIT(0, "未支付"),
    SUCCESS(1, "支付成功");

    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    PayStatus(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
