package com.gandazhi.sell.dao;

import com.gandazhi.sell.pojo.CartInfo;
import com.gandazhi.sell.vo.CartVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CartInfo record);

    int insertSelective(CartInfo record);

    CartInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CartInfo record);

    int updateByPrimaryKey(CartInfo record);

    List<CartVo> selectCartVoByOpenId(String openId);

    CartVo selectCartVoByProductId(String productId);

    int selectQuantityForProductIdAndOpenId(@Param(value = "productId") String productId, @Param(value = "openId") String openId);

    int deleteByProductIdAndOpenId(@Param(value = "productId") String productId, @Param(value = "openId") String openId);

    int updateQuantityByProductIdAndOpenId(@Param(value = "productId") String productId, @Param(value = "openId") String openId,
                                          @Param(value = "updateQuantity") Integer updateQuantity);
}