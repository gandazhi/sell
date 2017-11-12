package com.gandazhi.sell.service;

import com.gandazhi.sell.common.ServiceResponse;

public interface ICartService {

    ServiceResponse addCart(String openId, String productId, Integer quantity);

    ServiceResponse delCart(String productId, Integer quantity, String openId);

    ServiceResponse getCart(String openId);
}
