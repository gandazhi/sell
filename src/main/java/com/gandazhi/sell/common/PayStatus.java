package com.gandazhi.sell.common;

public enum PayStatus implements CodeEnum{
    WAIT(0, "未支付"),
    SUCCESS(1, "支付成功");

    private Integer code;
    private String msg;

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    PayStatus(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 循环遍历，通过传code值来获取枚举
     * @param code
     * @return
     */
    public static PayStatus getPayStatus(Integer code){
        for (PayStatus payStatus : PayStatus.values()){
            if (payStatus.code == code){
                return payStatus;
            }
        }
        return null;
    }
}
