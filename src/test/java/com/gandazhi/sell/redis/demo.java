package com.gandazhi.sell.redis;

import com.gandazhi.sell.dto.CartRedisDto;
import com.gandazhi.sell.dto.CartRedisListDto;
import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class demo {

    @Test
    public void redisTest(){
        Jedis jedis = new Jedis();
        jedis.set("ceshi","hhh");
        jedis.hset("myhash", "adders", "chengdu");
        String value = jedis.get("ceshi");
        System.out.println(value);
        jedis.close();
    }

    @Test
    public void hgetallTest(){
        Jedis jedis = new Jedis();
        Map map = jedis.hgetAll("sadasd");
        System.out.println(CollectionUtils.isEmpty(map));
    }

    @Test
    public void gsonRedis(){
        Gson gson = new Gson();
        Jedis jedis = new Jedis();
        List<CartRedisDto> cartRedisDtoList = Lists.newArrayList();
        CartRedisDto cartRedisDto = new CartRedisDto();
        cartRedisDto.setProductId("11111");
        cartRedisDto.setQuantity(2);
        cartRedisDto.setOpenId("123456");
        cartRedisDto.setCreateTime(new Date());
        cartRedisDto.setUpdateTime(new Date());
        cartRedisDtoList.add(cartRedisDto);

        CartRedisListDto cartRedisListDto = new CartRedisListDto();
        cartRedisListDto.setCartRedisDtos(cartRedisDtoList);
        jedis.set("ceshiKey", gson.toJson(cartRedisListDto));

        String jedisString = jedis.get("ceshiKey");

        CartRedisListDto cartRedisListDto2 = gson.fromJson(jedisString, CartRedisListDto.class);
        List<CartRedisDto> cartRedisDtoList1 = cartRedisListDto2.getCartRedisDtos();

    }

    @Test
    public void listTest(){
        Jedis jedis = new Jedis();
        String json = jedis.get("123456_cart");
        Gson gson = new Gson();
        CartRedisListDto cartRedisListDto = gson.fromJson(json, CartRedisListDto.class);
        System.out.println(cartRedisListDto.toString());
    }


}
