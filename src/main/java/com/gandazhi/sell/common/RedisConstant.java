package com.gandazhi.sell.common;

/**
 * redis常量
 * @author gandazhi
 * @create 2017-12-06 下午10:53
 **/
public interface RedisConstant {

    String TOKEN_PREFIX = "token_%s";
    Integer EXPIRE = 7200; //2小时
}
