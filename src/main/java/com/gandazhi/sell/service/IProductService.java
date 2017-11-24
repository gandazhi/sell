package com.gandazhi.sell.service;

import com.gandazhi.sell.common.ServiceResponse;
import com.github.pagehelper.PageInfo;

public interface IProductService {

    //获取全部上架了的商品列表
    ServiceResponse getProductList(int pageNum, int pageSize);

    //获取全部商品列表
    PageInfo getAllProductList(int pageNum, int pageSize);
}
