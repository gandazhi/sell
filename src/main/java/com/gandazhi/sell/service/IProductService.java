package com.gandazhi.sell.service;

import com.gandazhi.sell.common.ProductStatus;
import com.gandazhi.sell.common.ServiceResponse;
import com.gandazhi.sell.dto.UpdateProductInfoDto;
import com.gandazhi.sell.pojo.ProductInfo;
import com.gandazhi.sell.vo.SellerChangeProductStatusVo;
import com.github.pagehelper.PageInfo;

public interface IProductService {

    //获取全部上架了的商品列表
    ServiceResponse getProductList(int pageNum, int pageSize);

    //商家后台获取全部商品列表
    PageInfo getAllProductList(int pageNum, int pageSize);

    //商家后台更改商品
    SellerChangeProductStatusVo changeProduct(String productId, ProductStatus productStatus);

    ServiceResponse updateOrCreateProduct(String productId, UpdateProductInfoDto updateProductInfoDto);

    //获取全部分类名称
    ServiceResponse getAllCategory();

    //获取商户后台中的一个商品列表信息Z
    ServiceResponse getSellerProductInfo(String productId);


}
