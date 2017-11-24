package com.gandazhi.sell.controller;

import com.gandazhi.sell.common.ServiceResponse;
import com.gandazhi.sell.service.IProductService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * @author gandazhi
 * @create 2017-11-23 下午9:35
 **/

@Controller
//@RestController
@RequestMapping("/seller/product")
public class SellerProductController {

    @Autowired
    private IProductService productService;

    @GetMapping("/list")
    public ModelAndView getAllProductList(@RequestParam(value="pageNum", defaultValue = "1") Integer pageNum,
                                             @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                             Map<String, Object> map){
        PageInfo pageInfo =  productService.getAllProductList(pageNum, pageSize);
        map.put("pageInfo", pageInfo);
        return new ModelAndView("product/list", map);
//        return ServiceResponse.createBySuccess(pageInfo);
    }

}
