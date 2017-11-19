package com.gandazhi.sell.util;

import com.gandazhi.sell.common.CodeEnum;

public class EnumUtil {

    /**
     * 循环遍历枚举中的code，如果遍历完都没有return，就是传来的code在枚举里没有找到，返回null
     * @param code
     * @param enumClass
     * @param <T>
     * @return
     */
    public static <T extends CodeEnum>T getByCode (Integer code, Class<T> enumClass){
        for (T each : enumClass.getEnumConstants()){
            if (each.getCode() == code){
                return each;
            }
        }
        return null;
    }
}
