package com.gandazhi.sell.service;

import com.gandazhi.sell.common.ServiceResponse;

public interface IUserService {

    ServiceResponse checkHasUser(String openId);

}
