package com.gandazhi.sell.handler;

import com.gandazhi.sell.customException.SellerAuthorizeException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author gandazhi
 * @create 2017-12-07 下午9:14
 **/
@ControllerAdvice
public class SellerExceptionHandler {

    //拦截登录异常
    @ExceptionHandler(value = SellerAuthorizeException.class)
    public ModelAndView handlerAuthorizeException(){
        return new ModelAndView("redirect:" + "/seller/user/index");//跳转到登录页去
    }

}
