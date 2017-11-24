package com.gandazhi.sell.controller;

import com.gandazhi.sell.common.ProductStatus;
import com.gandazhi.sell.common.ServiceResponse;
import com.gandazhi.sell.service.IProductService;
import com.gandazhi.sell.vo.SellerChangeProductStatusVo;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
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
    public ModelAndView getAllProductList(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                          @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                          Map<String, Object> map) {
        PageInfo pageInfo = productService.getAllProductList(pageNum, pageSize);
        map.put("pageInfo", pageInfo);
        return new ModelAndView("product/list", map);
//        return ServiceResponse.createBySuccess(pageInfo);
    }

    @GetMapping("/changeStatus")
    public ModelAndView changeProductStatus(@RequestParam(value = "productId") String productId,
                                            @RequestParam(value = "productStatus") Integer productStatus) {
        if (productStatus == ProductStatus.UP.getCode()) {
            //上架商品
            SellerChangeProductStatusVo changeProductStatusVo = productService.changeProduct(productId, ProductStatus.UP);
            return returnModelAndView(changeProductStatusVo);
        } else if (productStatus == ProductStatus.DOWN.getCode()) {
            //下架商品
            SellerChangeProductStatusVo changeProductStatusVo = productService.changeProduct(productId, ProductStatus.DOWN);
            return returnModelAndView(changeProductStatusVo);
        } else {
            Map<String, Object> map = Maps.newHashMap();
            map.put("msg", "productStatus状态错误");
            return new ModelAndView("common/error", map);
        }
    }

    /**
     * 根据changeProductStatusVo返回不同的模板
     *
     * @param changeProductStatusVo
     * @return
     */
    private ModelAndView returnModelAndView(SellerChangeProductStatusVo changeProductStatusVo) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("url", "/seller/product/list");
        if (changeProductStatusVo.getIsSuccess()) {
            map.put("msg", changeProductStatusVo.getMsg());
            return new ModelAndView("common/success", map);
        } else {
            //下架失败
            map.put("msg", changeProductStatusVo.getMsg());
            return new ModelAndView("common/error", map);
        }
    }

}
