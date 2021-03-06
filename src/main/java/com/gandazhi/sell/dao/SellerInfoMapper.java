package com.gandazhi.sell.dao;

import com.gandazhi.sell.pojo.SellerInfo;
import org.apache.ibatis.annotations.Param;

public interface SellerInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SellerInfo record);

    int insertSelective(SellerInfo record);

    SellerInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SellerInfo record);

    int updateByPrimaryKey(SellerInfo record);

    int selectByOpenId(String openId);

    SellerInfo selectLogin(@Param(value = "username") String username, @Param(value = "md5Password") String md5Password);

    int selectCountByUsername(String username);
}