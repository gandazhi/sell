package com.gandazhi.sell.controller;

import com.gandazhi.sell.common.ServiceResponse;
import com.gandazhi.sell.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/buyer/product/")
public class BuyerProductController {

    @Autowired
    private IProductService iProductService;

    @GetMapping("/list")
    public ServiceResponse getList(){
        return iProductService.getProductList();
    }
}
