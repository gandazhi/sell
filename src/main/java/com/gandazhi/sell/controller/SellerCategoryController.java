package com.gandazhi.sell.controller;

import com.gandazhi.sell.service.ICategoryService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * @author gandazhi
 * @create 2017-12-05 下午4:50
 **/
@Controller
@RequestMapping("/seller/category")
public class SellerCategoryController {

    @Autowired
    private ICategoryService iCategoryService;

    @GetMapping("/list")
    public ModelAndView getCategoryList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize, Map<String, Object> map){
        PageInfo pageInfo = iCategoryService.getCategoryList(pageNum, pageSize);
        map.put("pageInfo", pageInfo);
        return new ModelAndView("category/list", map);
    }
}
