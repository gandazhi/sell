package com.gandazhi.sell.controller;

import com.gandazhi.sell.common.ServiceResponse;
import com.gandazhi.sell.service.IOrderService;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
@RequestMapping("/seller/order")
public class SellerOrderController {

    @Autowired
    private IOrderService orderService;

    /**
     * 商家后台获取订单列表
     * @param pageNum 分页，当前页数
     * @param pageSize 分页，每页显示的个数
     * @param map 传到模板的数据
     * @return 模板
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
     * 商家后台取消订单
     * @param orderId 待取消的订单id
     * @param map 传到模板的数据
     * @return  模板
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

    /**
     * 商家后台获取订单详情
     * @param orderId 待获取订单详情的id
     * @param map 传到模板的数据
     * @return 模板
     */
    @GetMapping("/detail")
    public ModelAndView getOrderDetail(@RequestParam(value = "orderId", defaultValue = "") String orderId, Map<String, Object> map) {
        ServiceResponse response = orderService.getOrderDetail(orderId);
        map.put("response", response);
        return new ModelAndView("order/detail", map);
//        return response;
    }

    /**
     * 商家后台完结订单
     * @param orderId 待完结的订单
     * @param map 传到模板的数据
     * @return 模板
     */
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
