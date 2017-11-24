package com.gandazhi.sell.dao;

import com.gandazhi.sell.pojo.ProductInfo;
import com.gandazhi.sell.vo.SellerProductInfoVo;

import java.util.List;

public interface ProductInfoMapper {
    int deleteByPrimaryKey(String productId);

    int insert(ProductInfo record);

    int insertSelective(ProductInfo record);

    ProductInfo selectByPrimaryKey(String productId);

    int updateByPrimaryKeySelective(ProductInfo record);

    int updateByPrimaryKey(ProductInfo record);

    List<ProductInfo> selectUpAll();

    int selectCountByProductId(String productId);

    List<SellerProductInfoVo> selectLeftJoinCategoryAll();
}