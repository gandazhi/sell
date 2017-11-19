package com.gandazhi.sell.controller;

import com.gandazhi.sell.service.IOrderService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
@RequestMapping("/seller/order")
public class SellerController {

    @Autowired
    private IOrderService orderService;

    @GetMapping("/list")
    public ModelAndView getOrderList(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                     @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                     Map<String, Object> map){
        PageInfo pageInfo = orderService.getOrderList(pageNum, pageSize);
        map.put("pageInfo", pageInfo);
        return new ModelAndView("order/list", map);
    }
}
