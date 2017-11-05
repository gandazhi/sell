package com.gandazhi.sell.service.impl;

import com.gandazhi.sell.common.ServiceResponse;

import com.gandazhi.sell.dao.ProductCategoryMapper;
import com.gandazhi.sell.dao.ProductInfoMapper;
import com.gandazhi.sell.pojo.ProductCategory;
import com.gandazhi.sell.pojo.ProductInfo;
import com.gandazhi.sell.service.IProductService;
import com.gandazhi.sell.vo.ProductInfoVo;
import com.gandazhi.sell.vo.ProductVo;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("iProductService")
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductInfoMapper productInfoMapper;
    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    @Override
    public ServiceResponse getProductList() {
        /**
         * 1.查询所有上架商品
         * 2.查询categoryType
         * 3.组装productVoList
         */

        List<ProductInfo> productInfoList = productInfoMapper.selectUpAll();
        List<Integer> categoryTypeList = productInfoList.stream().map(e -> e.getCategoryType()).collect(Collectors.toList()); //java8 lambda表达式
        List<ProductCategory> productCategoryList = productCategoryMapper.selectByCategoryTypeList(categoryTypeList);

        List<ProductInfoVo> productInfoVoList = assembleProductInfoVoList(productInfoList);
        List<ProductVo> productVoList = assembleProductVoList(productCategoryList, productInfoVoList);

        return ServiceResponse.createBySuccess(productVoList);
    }

    private List<ProductVo> assembleProductVoList(List<ProductCategory> productCategoryList, List<ProductInfoVo> productInfoVoList){
        List<ProductVo> productVoList = Lists.newArrayList();
        for (ProductCategory productCategory : productCategoryList){
            ProductVo productVo = new ProductVo();
            productVo.setCategoryName(productCategory.getCategoryName());
            productVo.setCategoryType(productCategory.getCategoryType());
            productVo.setProductInfoVoList(productInfoVoList);
            productVoList.add(productVo);
        }
        return productVoList;
    }

    private List<ProductInfoVo> assembleProductInfoVoList(List<ProductInfo> productInfoList){
        List<ProductInfoVo> productInfoVoList = Lists.newArrayList();
        for (ProductInfo productInfo : productInfoList){
            ProductInfoVo productInfoVo = new ProductInfoVo();
            productInfoVo.setProductId(productInfo.getProductId());
            productInfoVo.setProductName(productInfo.getProductName());
            productInfoVo.setProductPrice(productInfo.getProductPrice());
            productInfoVo.setProductDescription(productInfo.getProductDescription());
            productInfoVo.setProductIcon(productInfo.getProductIcon());

            productInfoVoList.add(productInfoVo);
        }
        return productInfoVoList;
    }
}
