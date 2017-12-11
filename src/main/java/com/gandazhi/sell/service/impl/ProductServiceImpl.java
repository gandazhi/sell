package com.gandazhi.sell.service.impl;

import com.gandazhi.sell.common.ProductStatus;
import com.gandazhi.sell.common.ServiceResponse;

import com.gandazhi.sell.customException.WriteDbException;
import com.gandazhi.sell.dao.ProductCategoryMapper;
import com.gandazhi.sell.dao.ProductInfoMapper;
import com.gandazhi.sell.dto.ProductInfoDto;
import com.gandazhi.sell.dto.UpdateProductInfoDto;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
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
        } else {
            switch (productStatus) {
                case UP:
                    if (productInfo.getProductStatus() != ProductStatus.DOWN.getCode()) {
                        msg = "只有下架的商品才能上架";
                    } else {
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
                    if (productInfo.getProductStatus() != ProductStatus.UP.getCode()) {
                        msg = "只有上架架的商品才能下架";
                    } else {
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

    //获取全部分类的名字
    @Override
    public ServiceResponse getAllCategory() {
        List<String> categoryNameList = productCategoryMapper.selectAllCategoryName();
        if (CollectionUtils.isEmpty(categoryNameList)) {
            return ServiceResponse.createBySuccessMesage("现在还没有分类");
        } else {
            return ServiceResponse.createBySuccess(categoryNameList);
        }
    }

    @Override
    @Transactional
    public ServiceResponse updateProduct(String productId, UpdateProductInfoDto updateProductInfoDto) {
        if (productId == null || StringUtils.isEmpty(productId)) {
            //沒有传productId，创建一个新的product
            return null;
        } else {
            //传了productId，更新product
            ProductInfo productInfo = productInfoMapper.selectByPrimaryKey(productId);
            if (productInfo.getProductId().equals("")) {
                return ServiceResponse.createByErrorMessage("没有找到" + productId + "这个商品");
            } else {
                //找到product，更新product
                BeanUtils.copyProperties(updateProductInfoDto, productInfo);

                ProductCategory productCategory = productCategoryMapper.selectByCategoryName(updateProductInfoDto.getCategoryName());
                productInfo.setCategoryType(productCategory.getCategoryType());
                int resultCount = productInfoMapper.updateByPrimaryKeySelective(productInfo);
                if (resultCount <= 0) {
                    try {
                        throw new WriteDbException("更新productInfo表错误");
                    } catch (WriteDbException e) {
                        log.error("更新productInfo表错误");
                        return ServiceResponse.createByErrorMessage("更新productInfo表错误");
                    }
                }

                return ServiceResponse.createBySuccessMesage("更新product成功");
            }
        }
    }

    @Override
    public ServiceResponse getSellerProductInfo(String productId) {
        SellerProductInfoVo sellerProductInfoVo = productInfoMapper.selectLeftJoinCategoryByProductId(productId);
        return ServiceResponse.createBySuccess(sellerProductInfoVo);
    }

    @Override
    @Transactional
    public ServiceResponse createProduct(ProductInfo productInfo) {
        int resultCount = productInfoMapper.insert(productInfo);
        if (resultCount <= 0) {
            try {
                throw new WriteDbException("插入productInfo错误");
            } catch (WriteDbException e) {
                log.error("插入productInfo错误");
                return ServiceResponse.createByErrorMessage("插入productInfo错误");
            }
        } else {
            return ServiceResponse.createBySuccessMesage("新建商品成功");
        }
    }

    @Override
    public ServiceResponse getCategoryType(String categoryName) {
        if (categoryName == null || StringUtils.isEmpty(categoryName)) {
            return ServiceResponse.createByErrorMessage("categoryName不能为空");
        }
        ProductCategory productCategory = productCategoryMapper.selectByCategoryName(categoryName);
        if (productCategory.getCategoryType() == null) {
            return ServiceResponse.createByErrorMessage("没有找到" + categoryName + "这个分类");
        }
        return ServiceResponse.createBySuccess(productCategory.getCategoryType());
    }
}
