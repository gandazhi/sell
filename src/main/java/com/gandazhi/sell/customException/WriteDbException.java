package com.gandazhi.sell.customException;

public class WriteDbException extends Exception{

    public WriteDbException() {
    }

    public WriteDbException(String msg) {
        super(msg);
    }

}
