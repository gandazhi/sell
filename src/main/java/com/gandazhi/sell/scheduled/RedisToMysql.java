package com.gandazhi.sell.scheduled;

import com.gandazhi.sell.common.RedisIndex;
import com.gandazhi.sell.dao.CartInfoMapper;
import com.gandazhi.sell.dto.CartRedisDto;
import com.gandazhi.sell.dto.CartRedisListDto;
import com.gandazhi.sell.pojo.CartInfo;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Component
public class RedisToMysql {
    @Autowired
    private CartInfoMapper cartInfoMapper;

    private void cartInfo(){
        //把redis中的信息存到MySQL中
        List<CartRedisListDto> cartRedisListDtoList = Lists.newArrayList();
        Gson gson = new Gson();
        Jedis jedis = new Jedis();
        jedis.select(RedisIndex.CART.getCode());//0用来存购物车信息
        Set redisAllKey = jedis.keys("*");
        Iterator it = redisAllKey.iterator();
        //遍历redis中所有的库
        while (it.hasNext()){
            cartRedisListDtoList.add(gson.fromJson(jedis.get(it.next().toString()), CartRedisListDto.class));
        }
        //取出cartRedisDtoList
        List<CartRedisDto> cartRedisDtoList = cartRedisListDtoList.get(0).getCartRedisDtos();
        for (CartRedisDto cartRedisDto : cartRedisDtoList){
            CartInfo cartInfo = new CartInfo();

        }
    }
}
