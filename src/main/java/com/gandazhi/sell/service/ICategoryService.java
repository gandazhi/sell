package com.gandazhi.sell.service;

import com.gandazhi.sell.common.ServiceResponse;
import com.gandazhi.sell.pojo.ProductCategory;
import com.github.pagehelper.PageInfo;

/**
 * @author gandazhi
 * @create 2017-12-05 下午5:03
 **/
public interface ICategoryService {

    //获取全部分类名
    PageInfo getCategoryList(int pageNum, int pageSize);

    ServiceResponse updateCategory(ProductCategory newProductCategory);
}
