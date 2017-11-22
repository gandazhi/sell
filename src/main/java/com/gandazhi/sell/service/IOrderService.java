package com.gandazhi.sell.service;

import com.gandazhi.sell.common.ServiceResponse;
import com.gandazhi.sell.customException.WriteDbException;
import com.gandazhi.sell.dto.OrderDetailDto;
import com.gandazhi.sell.dto.OrderMasterDto;
import com.gandazhi.sell.vo.SellerOrderVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface IOrderService {

    ServiceResponse createOrder(OrderMasterDto orderMasterDto);

    PageInfo getOrderList(Integer pageNum, Integer pageSize);

    boolean cancelOrder(String orderId);

    ServiceResponse getOrderDetail(String orderId);


}
