package com.gandazhi.sell.controller;

import com.gandazhi.sell.common.ServiceResponse;
import com.gandazhi.sell.service.IOrderService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
//@RestController
@RequestMapping("/seller/order")
public class SellerController {

    @Autowired
    private IOrderService orderService;

    /**
     * @param pageNum
     * @param pageSize
     * @param map
     * @return
     */
    @GetMapping("/list")
    public ModelAndView getOrderList(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                     @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                     Map<String, Object> map) {
        PageInfo pageInfo = orderService.getOrderList(pageNum, pageSize);
        map.put("pageInfo", pageInfo);
        return new ModelAndView("order/list", map);
    }

    /**
     * @param orderId
     * @param map
     * @return
     */
    @GetMapping("/cancel")
    public ModelAndView cancelOrder(@RequestParam(value = "orderId", defaultValue = "") String orderId, Map<String, Object> map) {
        map.put("url", "/seller/order/list");
        if (orderId.isEmpty() || orderId == null) {
            map.put("msg", "orderId不能为空");
            return new ModelAndView("common/error", map);
        }
        boolean isSuccess = orderService.cancelOrder(orderId);
        if (isSuccess) {
            map.put("msg", "取消订单成功");
            return new ModelAndView("common/success", map);
        } else {
            map.put("msg", "取消订单失败");
            return new ModelAndView("common/error", map);
        }
    }

    @GetMapping("/detail")
    public ModelAndView getOrderDetail(@RequestParam(value = "orderId", defaultValue = "") String orderId, Map<String, Object> map) {
        ServiceResponse response = orderService.getOrderDetail(orderId);
        map.put("response", response);
        return new ModelAndView("order/detail", map);
//        return response;
    }

    @GetMapping("/finish")
    public ModelAndView finishOrder(String orderId, Map<String, Object> map){
        ServiceResponse response = orderService.finishOrder(orderId);
        map.put("url", "/seller/order/list");
        if (response.isSuccess()){
            map.put("msg", "完结订单成功");
            return new ModelAndView("common/success", map);
        }else {
            map.put("msg", response.getMsg());
            return new ModelAndView("common/error", map);
        }
    }


}
