package com.gandazhi.sell.common;

public enum OrderStatus implements CodeEnum{
    NEW(0, "新订单"),
    FINISHED(1, "已完成"),
    CANCEL(2, "已取消");

    private Integer code;
    private String msg;

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    OrderStatus(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 循环遍历枚举中的code，如果遍历完都没有return，就是传来的code在枚举里没有找到，返回null
     * @param code
     * @return
     */
    public static OrderStatus getOrderStatus(Integer code){
        for (OrderStatus orderStatus : OrderStatus.values()){
            if (orderStatus.code == code){
                return orderStatus;
            }
        }
        return null;
    }
}
