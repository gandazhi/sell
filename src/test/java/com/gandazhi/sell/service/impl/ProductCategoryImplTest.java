package com.gandazhi.sell.service.impl;

import com.gandazhi.sell.pojo.ProductCategory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductCategoryImplTest {

    @Autowired
    private ProductCategoryImpl productCategoryService;

    @Test
    public void findOne() throws Exception {
        ProductCategory productCategory = productCategoryService.findOne(1);
        Assert.assertEquals(new Integer(1), productCategory.getCategoryId());
    }

    @Test
    public void findAll() throws Exception {
        List<ProductCategory> productCategory = productCategoryService.findAll();
        Assert.assertNotEquals(0, productCategory.size());
    }

    @Test
    public void findByCategoryTypeIn() throws Exception {
        List<Integer> integerList = Arrays.asList(111);
        List<ProductCategory> productCategory = productCategoryService.findByCategoryTypeIn(integerList);
        Assert.assertNotEquals(0, productCategory.size());
    }

    @Test
    @Transactional
    public void save() throws Exception {
        ProductCategory productCategory = new ProductCategory("ceshi2", 222);
        ProductCategory result = productCategoryService.save(productCategory);
        Assert.assertNotNull(result);
    }

}