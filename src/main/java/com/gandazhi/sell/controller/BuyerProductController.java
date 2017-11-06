package com.gandazhi.sell.controller;

import com.gandazhi.sell.common.ServiceResponse;
import com.gandazhi.sell.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/buyer/product/")
public class BuyerProductController {

    @Autowired
    private IProductService iProductService;

    /**
     * 获取不同分类下的全部商品
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    public ServiceResponse getList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                   @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        return iProductService.getProductList(pageNum, pageSize);
    }
}
