package com.gandazhi.sell.service.impl;

import com.gandazhi.sell.common.ProductStatus;
import com.gandazhi.sell.dao.ProductInfoRepository;
import com.gandazhi.sell.pojo.ProductInfo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductInfoServiceImplTest {
    @Autowired
    private ProductInfoRepository repository;

    @Test
    public void findOne() throws Exception {
        ProductInfo productInfo = repository.findOne("111111");
        Assert.assertEquals("111111", productInfo.getProductId());
    }

    @Test
    public void findUpAll() throws Exception {
        List<ProductInfo> productInfoList = repository.findByProductStatus(ProductStatus.UP.getCode());
        Assert.assertNotEquals(0, productInfoList.size());
    }

    @Test
    public void findAll() throws Exception {
        PageRequest request = new PageRequest(0, 2);
        Page<ProductInfo> productInfoPage = repository.findAll(request);
//        System.out.println(productInfoPage.getTotalElements());
        Assert.assertNotEquals(0, productInfoPage.getTotalElements());
    }

    @Test
    @Transactional
    public void save() throws Exception {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setProductId("22222");
        productInfo.setProductName("测试商品");
        productInfo.setProductPrice(new BigDecimal(20));
        productInfo.setProductStock(100);
        productInfo.setProductDescription("测试商品描述");
        productInfo.setProductIcon("www.baidu.com");
        productInfo.setProductStatus(0);
        productInfo.setCategoryType(1);

        ProductInfo result = repository.save(productInfo);
        Assert.assertNotNull(result);

    }

}