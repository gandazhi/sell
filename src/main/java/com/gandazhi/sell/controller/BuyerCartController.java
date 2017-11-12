package com.gandazhi.sell.controller;

import com.gandazhi.sell.common.ServiceResponse;
import com.gandazhi.sell.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/buyer/cart/")
public class BuyerCartController {

    // TODO 从session中取出openId

    @Autowired
    private ICartService iCartService;

    @PostMapping("/add")
    public ServiceResponse addCart(String productId, Integer quantity){
        String openId = "123456";
        return iCartService.addCart(openId, productId, quantity);
    }

    @GetMapping("/getCart")
    public ServiceResponse getCart(){
        return iCartService.getCart("123456");
    }

}
