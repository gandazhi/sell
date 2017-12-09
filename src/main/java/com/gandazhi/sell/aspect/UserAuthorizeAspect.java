package com.gandazhi.sell.aspect;

import com.gandazhi.sell.common.RedisConstant;
import com.gandazhi.sell.common.RedisIndex;
import com.gandazhi.sell.customException.ApiSellerAuthorizeException;
import com.gandazhi.sell.util.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import redis.clients.jedis.Jedis;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * api验证是否登录
 * @author gandazhi
 * @create 2017-12-07 下午10:02
 **/
@Aspect
@Component
@Slf4j
public class UserAuthorizeAspect {

    @Pointcut("execution(public * com.gandazhi.sell.controller.ApiSeller*.*(..))"+
    "&& !execution(public * com.gandazhi.sell.controller.ApiSellerUser*.*(..))")
    public void apiVerify(){}

    @Before("apiVerify()")
    public void doApiVerify(){
        ServletRequestAttributes requestAttributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
        HttpServletRequest request = requestAttributes.getRequest();

        //查询cookie
        Cookie cookie = CookieUtil.get(request, "token");
        if (cookie == null){
            log.warn("[API登录校验],Cookie中找不到token");
            throw new ApiSellerAuthorizeException();
        }

        //去redis中查找
        Jedis jedis = new Jedis();
        jedis.select(RedisIndex.SESSION.getCode());
        String tokenValue = jedis.get(String.format(RedisConstant.TOKEN_PREFIX, cookie.getValue()));
        if (tokenValue == null || StringUtils.isEmpty(tokenValue)){
            log.warn("[API登录校验],redis中找不到session");
            throw new ApiSellerAuthorizeException();
        }
    }

}
