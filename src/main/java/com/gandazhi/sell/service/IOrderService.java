package com.gandazhi.sell.service;

import com.gandazhi.sell.common.ServiceResponse;
import com.gandazhi.sell.dto.OrderDetailDto;
import com.gandazhi.sell.dto.OrderMasterDto;

import java.util.List;

public interface IOrderService {

    ServiceResponse createOrder(OrderMasterDto orderMasterDto, List<OrderDetailDto> orderDetailDtoList);
}
