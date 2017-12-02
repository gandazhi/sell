package com.gandazhi.sell.controller;

import com.gandazhi.sell.common.ServiceResponse;
import com.gandazhi.sell.dto.UpdateProductInfoDto;
import com.gandazhi.sell.service.IProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * SellerProductController 返回API的controller
 *
 * @author gandazhi
 * @create 2017-12-02 下午5:22
 **/
@RestController
@RequestMapping("/api/seller/")
public class ApiSellerProductController {

    @Autowired
    private IProductService iProductService;

    /**
     * 如果没有传productId，就是新建一个商品
     * 如果传了productId，就是更新一个商品
     *
     * @param productId
     * @return
     */
    @PostMapping("/updateProduct")
    public ServiceResponse addOrChangeProduct(@RequestParam(value = "productId", required = false) String productId, UpdateProductInfoDto updateProductInfoDto) {
        if (StringUtils.isNotBlank(productId)) {
            //传了productId,更新product
            return iProductService.updateOrCreateProduct(productId, updateProductInfoDto);
        } else {
            return null;
        }
    }

    @GetMapping("/getProduct")
    public ServiceResponse getProduct(String productId) {
        if (productId == null || productId.isEmpty()) {
            return ServiceResponse.createByErrorMessage("productId不能为空");
        }else {
            return iProductService.getSellerProductInfo(productId);
        }
    }
}
