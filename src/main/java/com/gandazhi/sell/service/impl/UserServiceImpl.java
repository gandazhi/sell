package com.gandazhi.sell.service.impl;

import com.gandazhi.sell.common.ServiceResponse;
import com.gandazhi.sell.dao.SellerInfoMapper;
import com.gandazhi.sell.pojo.SellerInfo;
import com.gandazhi.sell.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("iUserService")
public class UserServiceImpl implements IUserService{

    @Autowired
    private SellerInfoMapper sellerInfoMapper;

    //通过openId检查这个用户是否在数据库中有了
    @Override
    public ServiceResponse checkHasUser(String openId) {
        int resultCount = sellerInfoMapper.selectByOpenId(openId);
        if (resultCount > 0){
            //数据库中有这个openId
            return ServiceResponse.createBySuccessMesage("这个用户是注册过的老用户");
        }else {
            //数据库中没有这个openId，把这个openId插入
            return ServiceResponse.createByErrorMessage("这个用户还没有注册");
        }
    }
}
