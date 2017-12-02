package com.gandazhi.sell.controller;

import com.gandazhi.sell.common.ServiceResponse;
import com.gandazhi.sell.customException.WriteDbException;
import com.gandazhi.sell.dto.OrderDetailDto;
import com.gandazhi.sell.dto.OrderMasterDto;
import com.gandazhi.sell.pojo.OrderMaster;
import com.gandazhi.sell.service.IOrderService;
import com.gandazhi.sell.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/buyer/product/")
public class BuyerProductController {

    @Autowired
    private IProductService iProductService;
    @Autowired
    private IOrderService iOrderService;

    /**
     * 获取不同分类下的全部商品
     * @param pageNum 分页，当前显示的页数
     * @param pageSize 分页，每页显示的个数
     * @return
     */
    @GetMapping("/list")
    public ServiceResponse getList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                   @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        return iProductService.getProductList(pageNum, pageSize);
    }

    @PostMapping("/createOrder")
    public ServiceResponse createOrder(OrderMasterDto orderMasterDto) throws WriteDbException {
        if (orderMasterDto.getBuyerOpenid().equals("")){
            return ServiceResponse.createByErrorMessage("用户未登录，需登录后在下单");
        }
        return iOrderService.createOrder(orderMasterDto);
    }

    @GetMapping("/getAllCategoryName")
    public ServiceResponse getAllCategoryName(){
        return iProductService.getAllCategory();
    }
}
