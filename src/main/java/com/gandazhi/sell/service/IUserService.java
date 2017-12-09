package com.gandazhi.sell.service;

import com.gandazhi.sell.common.ServiceResponse;

import javax.servlet.http.HttpServletResponse;

public interface IUserService {

    ServiceResponse checkHasUser(String openId);

    ServiceResponse login(HttpServletResponse response, String username, String password);

    ServiceResponse register(String username, String password);

    //字段进行验证
    ServiceResponse<String> checkValid(String str, String type);

}
