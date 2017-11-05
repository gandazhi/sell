package com.gandazhi.sell.dao;

import com.gandazhi.sell.pojo.ProductCategory;

import java.util.List;
import java.util.Locale;

public interface ProductCategoryMapper {
    int deleteByPrimaryKey(Integer categoryId);

    int insert(ProductCategory record);

    int insertSelective(ProductCategory record);

    ProductCategory selectByPrimaryKey(Integer categoryId);

    int updateByPrimaryKeySelective(ProductCategory record);

    int updateByPrimaryKey(ProductCategory record);

    List<ProductCategory> selectByCategoryTypeList(List<Integer> categoryTypeList);
}