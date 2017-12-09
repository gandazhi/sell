package com.gandazhi.sell.controller;

import com.gandazhi.sell.common.ServiceResponse;
import com.gandazhi.sell.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author gandazhi
 * @create 2017-12-07 下午2:52
 **/
@Controller
@RequestMapping("/seller/user")
public class SellerUserController {

    @Autowired
    private IUserService iUserService;

    @GetMapping("/index")
    public ModelAndView index(Map<String, Object> map) {

        return new ModelAndView("index/index", map);
    }

}
