package com.gandazhi.sell.service;

import com.gandazhi.sell.common.ServiceResponse;

public interface ICartService {

    ServiceResponse addCart(String openId, String productId, Integer quantity);

    ServiceResponse delCart(Integer cartId);

    ServiceResponse getCart(String openId);
}
