package com.gandazhi.sell.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @author gandazhi
 * @create 2017-12-06 下午10:46
 **/
public class CookieUtil {

    public static void set(HttpServletResponse response, String name, String value, int maxAge){
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
    }
}
