package com.gandazhi.sell.service;

import com.gandazhi.sell.common.ServiceResponse;

public interface IProductService {

    ServiceResponse getProductList(int pageNum, int pageSize);
}
