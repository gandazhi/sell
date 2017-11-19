package com.gandazhi.sell.controller;

import com.gandazhi.sell.common.ServiceResponse;
import com.gandazhi.sell.service.IUserService;
import jdk.nashorn.internal.runtime.logging.Logger;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;

@Controller
@RequestMapping("/weChart")
@Slf4j
public class WeChartController {

    @Autowired
    private WxMpService wxMpService;
    @Autowired
    private IUserService iUserService;

    @GetMapping("/authorize")
    public String authorize(@RequestParam("returnUrl") String returnUrl) {
        //1. 配置
        //2. 调用方法
        String url = "http://3acg3e.natappfree.cc/weChart/userInfo";
        String redirectUrl = wxMpService.oauth2buildAuthorizationUrl(url, WxConsts.OAUTH2_SCOPE_BASE, URLEncoder.encode(returnUrl));
        return "redirect:" + redirectUrl;
    }

    @GetMapping("/userInfo")
    public String userInfo(@RequestParam("code") String code,
                           @RequestParam("state") String returnUrl) {
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();
        try {
            wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
        } catch (WxErrorException e) {
            log.error("微信授权错误{}", e);
        }

        String openId = wxMpOAuth2AccessToken.getOpenId();
        log.info(openId);
        //获取到openId检查数据库中是否有这条数据，有就把用户信息存session，没有就跳转到用户完善页
        ServiceResponse response = iUserService.checkHasUser(openId);
        if (response.isSuccess()) {
            //是注册过了的，之后写跳主页
            return "redirect:" + returnUrl + "?openid=" + openId;
        }else {
            //之后写调register页面
            return "redirect:" + "http://www.koudaimiao.com/Shop/personcenter/newpersoncenter";
        }
//        return "redirect:" + returnUrl + "?openid=" + openId;
    }
}
