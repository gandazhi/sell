package com.gandazhi.sell.handler;

import com.gandazhi.sell.common.ResponseCode;
import com.gandazhi.sell.common.ServiceResponse;
import com.gandazhi.sell.customException.ApiSellerAuthorizeException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author gandazhi
 * @create 2017-12-09 下午3:19
 **/
@RestControllerAdvice
public class ApiSellerExceptionHandler {

    @ExceptionHandler(value = ApiSellerAuthorizeException.class)
    public ServiceResponse handlerApiAuthorizeException(){

        return ServiceResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
    }
}
