package com.gandazhi.sell.service.impl;

import com.gandazhi.sell.common.RedisConstant;
import com.gandazhi.sell.common.RedisIndex;
import com.gandazhi.sell.common.ServiceResponse;
import com.gandazhi.sell.dao.SellerInfoMapper;
import com.gandazhi.sell.pojo.SellerInfo;
import com.gandazhi.sell.service.IUserService;
import com.gandazhi.sell.util.CookieUtil;
import com.gandazhi.sell.util.MD5Util;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Autowired
    private SellerInfoMapper sellerInfoMapper;

    //通过openId检查这个用户是否在数据库中有了
    @Override
    public ServiceResponse checkHasUser(String openId) {
        int resultCount = sellerInfoMapper.selectByOpenId(openId);
        if (resultCount > 0) {
            //数据库中有这个openId
            return ServiceResponse.createBySuccessMesage("这个用户是注册过的老用户");
        } else {
            //数据库中没有这个openId，把这个openId插入
            return ServiceResponse.createByErrorMessage("这个用户还没有注册");
        }
    }

    @Override
    public ServiceResponse login(HttpServletResponse response, String username, String password) {
        if (username == null || StringUtils.isEmpty(username)) {
            return ServiceResponse.createByErrorMessage("userName不能为空");
        }
        if (password == null || StringUtils.isEmpty(password)) {
            return ServiceResponse.createByErrorMessage("password不能为空");
        }

        //把password加上一段文字提高安全性进行MD5加密
        final String md5Str = "login";
        String md5Password = MD5Util.MD5EncodeUtf8(password + md5Str);
        SellerInfo sellerInfo = sellerInfoMapper.selectLogin(username, md5Password);
        if (sellerInfo.getUsername() == null || sellerInfo.getUsername().equals("")) {
            //说明没有在数据库中找到这个用户.登录失败
            return ServiceResponse.createByErrorMessage("用户名或密码错误");
        } else {
            //登录成功,设置token进redis,设置token进cookie
            String tokenUUID = UUID.randomUUID().toString();
            Integer expire = RedisConstant.EXPIRE;

            sellerInfo.setPassword(StringUtils.EMPTY); //把密码至空
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();  //实体属性为null时不参加序列化
            String userJson = gson.toJson(sellerInfo);

            Jedis jedis = new Jedis();
            jedis.select(RedisIndex.SESSION.getCode());
            jedis.setex(String.format(RedisConstant.TOKEN_PREFIX, tokenUUID), expire, userJson);
            jedis.close();

            CookieUtil.set(response, "token", tokenUUID, expire);

            return ServiceResponse.createBySuccessMesage("登录成功");
        }
    }
}
