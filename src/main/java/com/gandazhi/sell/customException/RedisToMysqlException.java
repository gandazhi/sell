package com.gandazhi.sell.customException;

public class RedisToMysqlException extends Exception {

    public RedisToMysqlException() {
    }

    public RedisToMysqlException(String message) {
        super(message);
    }
}
