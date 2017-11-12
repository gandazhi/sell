package com.gandazhi.sell.dao;

import com.gandazhi.sell.pojo.CartInfo;
import com.gandazhi.sell.vo.CartVo;

import java.util.List;

public interface CartInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CartInfo record);

    int insertSelective(CartInfo record);

    CartInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CartInfo record);

    int updateByPrimaryKey(CartInfo record);

    List<CartVo> selectCartVoByOpenId(String openId);
}