package com.gandazhi.sell.dto;

import lombok.Data;

import java.util.List;

@Data
public class CartRedisListDto {
    private List<CartRedisDto> cartRedisDtos;
}
