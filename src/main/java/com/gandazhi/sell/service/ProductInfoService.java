package com.gandazhi.sell.service;

import com.gandazhi.sell.pojo.ProductInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductInfoService {

    ProductInfo findOne(String productId);

    //查询所有在架商品
    List<ProductInfo> findUpAll();

    //查询所有商品
    Page<ProductInfo> findAll(Pageable pageable);

    ProductInfo save(ProductInfo productInfo);
}
