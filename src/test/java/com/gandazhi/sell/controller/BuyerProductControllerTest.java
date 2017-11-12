package com.gandazhi.sell.controller;

import com.gandazhi.sell.dto.OrderDetailDto;
import com.gandazhi.sell.dto.OrderMasterDto;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BuyerProductControllerTest {
    @Test
    public void getList() throws Exception {
    }

    @Test
    public void createOrder() throws Exception {
        OrderMasterDto orderMasterDto = new OrderMasterDto();
        orderMasterDto.setBuyerName("gandazhi");
        orderMasterDto.setBuyerPhone("18408243385");
        orderMasterDto.setBuyerAddress("天府二街");
        orderMasterDto.setBuyerOpenid("123");

        List<OrderDetailDto> orderDetailDtoList = Lists.newArrayList();
        OrderDetailDto orderDetailDto = new OrderDetailDto();
        orderDetailDto.setProductId("111111");
        orderDetailDto.setProductQuantity(2);
        orderDetailDtoList.add(orderDetailDto);
        
    }
}