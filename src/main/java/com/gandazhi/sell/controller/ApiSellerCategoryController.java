package com.gandazhi.sell.controller;

import com.gandazhi.sell.common.ServiceResponse;
import com.gandazhi.sell.pojo.ProductCategory;
import com.gandazhi.sell.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商户后台获取分类相关的接口
 *
 * @author gandazhi
 * @create 2017-12-05 下午6:42
 **/
@RestController
@RequestMapping("/api/seller/category")
public class ApiSellerCategoryController {

    @Autowired
    private ICategoryService iCategoryService;

    @PostMapping("/updateProductCategory")
    public ServiceResponse updateCategory(ProductCategory productCategory){
        return iCategoryService.updateCategory(productCategory);
    }
}
