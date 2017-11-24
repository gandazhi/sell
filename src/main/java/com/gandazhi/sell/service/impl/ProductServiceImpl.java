package com.gandazhi.sell.service.impl;

import com.gandazhi.sell.common.ProductStatus;
import com.gandazhi.sell.common.ServiceResponse;

import com.gandazhi.sell.dao.ProductCategoryMapper;
import com.gandazhi.sell.dao.ProductInfoMapper;
import com.gandazhi.sell.pojo.ProductCategory;
import com.gandazhi.sell.pojo.ProductInfo;
import com.gandazhi.sell.service.IProductService;
import com.gandazhi.sell.vo.ProductInfoVo;
import com.gandazhi.sell.vo.ProductVo;
import com.gandazhi.sell.vo.SellerChangeProductStatusVo;
import com.gandazhi.sell.vo.SellerProductInfoVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service("iProductService")
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductInfoMapper productInfoMapper;
    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    @Override
    public ServiceResponse getProductList(int pageNum, int pageSize) {
        /**
         * 1.查询所有上架商品
         * 2.查询categoryType
         * 3.组装productVoList
         */

        PageHelper.startPage(pageNum, pageSize);

        List<ProductInfo> productInfoList = productInfoMapper.selectUpAll();
        if (CollectionUtils.isEmpty(productInfoList)) {
            return ServiceResponse.createBySuccessMesage("现在还没有产品，去添加一些");
        }
        List<Integer> categoryTypeList = productInfoList.stream().map(e -> e.getCategoryType()).collect(Collectors.toList()); //java8 lambda表达式
        if (CollectionUtils.isEmpty(categoryTypeList)) {
            return ServiceResponse.createByErrorMessage("categoryTypeList是空的");
        }
        List<ProductCategory> productCategoryList = productCategoryMapper.selectByCategoryTypeList(categoryTypeList);
        if (CollectionUtils.isEmpty(productCategoryList)) {
            return ServiceResponse.createByErrorMessage("productCategoryList是空的");
        }

        List<ProductInfoVo> productInfoVoList = assembleProductInfoVoList(productInfoList);
        List<ProductVo> productVoList = assembleProductVoList(productCategoryList, productInfoVoList);
        PageInfo pageResult = new PageInfo(productInfoList);
        pageResult.setList(productVoList);
        return ServiceResponse.createBySuccess(pageResult);
    }

    //组装productVoList
    private List<ProductVo> assembleProductVoList(List<ProductCategory> productCategoryList, List<ProductInfoVo> productInfoVoList) {
        List<ProductVo> productVoList = Lists.newArrayList();
        for (ProductCategory productCategory : productCategoryList) {
            ProductVo productVo = new ProductVo();
            productVo.setCategoryName(productCategory.getCategoryName());
            productVo.setCategoryType(productCategory.getCategoryType());
            productVo.setProductInfoVoList(productInfoVoList);
            productVoList.add(productVo);
        }
        return productVoList;
    }

    //组装productInfoVoList
    private List<ProductInfoVo> assembleProductInfoVoList(List<ProductInfo> productInfoList) {
        List<ProductInfoVo> productInfoVoList = Lists.newArrayList();
        for (ProductInfo productInfo : productInfoList) {
            ProductInfoVo productInfoVo = new ProductInfoVo();
            /**
             * 用BeanUtils copy对象
             * copy相同属性的变量
             * 不同的属性不做处理，需要手动处理
             */
            BeanUtils.copyProperties(productInfo, productInfoVo);//copy对象
//            productInfoVo.setProductId(productInfo.getProductId());
//            productInfoVo.setProductName(productInfo.getProductName());
//            productInfoVo.setProductPrice(productInfo.getProductPrice());
//            productInfoVo.setProductDescription(productInfo.getProductDescription());
//            productInfoVo.setProductIcon(productInfo.getProductIcon());

            productInfoVoList.add(productInfoVo);
        }
        return productInfoVoList;
    }

    @Override
    public PageInfo getAllProductList(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<SellerProductInfoVo> productInfoVoList = productInfoMapper.selectLeftJoinCategoryAll();

        PageInfo pageInfo = new PageInfo(productInfoVoList);
        return pageInfo;
    }

    @Override
    public SellerChangeProductStatusVo changeProduct(String productId, ProductStatus productStatus) {
        boolean isSuccess = false;
        String msg = "";
        ProductInfo productInfo = productInfoMapper.selectByPrimaryKey(productId);
        if (productInfo == null) {
            msg = "没有找到" + productId + "这个商品";
        }else {
            switch (productStatus){
                case UP:
                    if (productInfo.getProductStatus() != ProductStatus.DOWN.getCode()){
                        msg = "只有下架的商品才能上架";
                    }else {
                        productInfo.setProductStatus(ProductStatus.UP.getCode());
                        int resultCount = productInfoMapper.updateByPrimaryKeySelective(productInfo);
                        if (resultCount <= 0) {
                            msg = "更新商品状态失败";
                        } else {
                            isSuccess = true;
                            msg = "更新商品状态成功";
                        }
                    }
                    break;

                case DOWN:
                    if (productInfo.getProductStatus() != ProductStatus.UP.getCode()){
                        msg = "只有上架架的商品才能下架";
                    }else {
                        productInfo.setProductStatus(ProductStatus.DOWN.getCode());
                        int resultCount = productInfoMapper.updateByPrimaryKeySelective(productInfo);
                        if (resultCount <= 0) {
                            msg = "更新商品状态失败";
                        } else {
                            isSuccess = true;
                            msg = "更新商品状态成功";
                        }
                    }
                    break;
            }
        }
        SellerChangeProductStatusVo sellerChangeProductStatusVo = new SellerChangeProductStatusVo();
        sellerChangeProductStatusVo.setIsSuccess(isSuccess);
        sellerChangeProductStatusVo.setMsg(msg);
        return sellerChangeProductStatusVo;
    }
}
