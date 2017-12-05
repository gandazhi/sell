package com.gandazhi.sell.service.impl;

import com.gandazhi.sell.common.ServiceResponse;
import com.gandazhi.sell.customException.WriteDbException;
import com.gandazhi.sell.dao.ProductCategoryMapper;
import com.gandazhi.sell.dao.ProductInfoMapper;
import com.gandazhi.sell.dto.SellerProductCategoryDto;
import com.gandazhi.sell.pojo.ProductCategory;
import com.gandazhi.sell.service.ICategoryService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author gandazhi
 * @create 2017-12-05 下午5:04
 **/
@Service("iCategory")
@Slf4j
public class CategoryServiceImpl implements ICategoryService{

    @Autowired
    private ProductCategoryMapper productCategoryMapper;
    @Autowired
    private ProductInfoMapper productInfoMapper;

    @Override
    public PageInfo getCategoryList(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ProductCategory> productCategoryList = productCategoryMapper.selectAllProductCategory();
        PageInfo pageInfo = new PageInfo(productCategoryList);
        return pageInfo;
    }

    @Override
    @Transactional
    public ServiceResponse updateCategory(ProductCategory newProductCategory) {
        if (newProductCategory.getCategoryId() == null){
            return ServiceResponse.createByErrorMessage("categoryId不能传空");
        }
        ProductCategory productCategory = productCategoryMapper.selectByPrimaryKey(newProductCategory.getCategoryId());
        if (productCategory == null){
            return ServiceResponse.createByErrorMessage("categoryId传错了,没有找到这个分类");
        }

        //更新productInfo表
        int resultCount = productInfoMapper.updateCategoryTypeByOldType(productCategory.getCategoryType(), newProductCategory.getCategoryType());
        if (resultCount <= 0){
            try {
                throw new WriteDbException("更新productInfo表category_type字段错误");
            } catch (WriteDbException e) {
                log.error("更新productInfo表category_type字段错误");
            }
        }

        //更新product_category表
        resultCount = productCategoryMapper.updateByPrimaryKeySelective(newProductCategory);
        if (resultCount <= 0 ){
            try {
                throw new WriteDbException("更新product_category表错误");
            } catch (WriteDbException e) {
                log.error("更新product_category表错误");
            }
        }

        return ServiceResponse.createBySuccessMesage("更新category_type成功");
    }

    @Override
    public ServiceResponse addCategory(SellerProductCategoryDto sellerProductCategoryDto) {
        ProductCategory productCategory = new ProductCategory();
        BeanUtils.copyProperties(sellerProductCategoryDto, productCategory);
        if (productCategory.getCategoryType() == null || StringUtils.isEmpty(productCategory.getCategoryName()) || productCategory.getCategoryName() == null){
            return ServiceResponse.createByErrorMessage("categoryType或categoryName不能为空");
        }
        int resultCount = productCategoryMapper.insertSelective(productCategory);
        if (resultCount <= 0){
            try {
                throw new WriteDbException("插入product_category表失败");
            } catch (WriteDbException e) {
                log.error("插入product_category表失败");
                return ServiceResponse.createByErrorMessage("插入product_category表失败");
            }
        }else {
            return ServiceResponse.createBySuccessMesage("新建分类成功");
        }
    }
}
