package com.gandazhi.sell.scheduled;

import com.gandazhi.sell.common.RedisIndex;
import com.gandazhi.sell.customException.RedisToMysqlException;
import com.gandazhi.sell.dao.CartInfoMapper;
import com.gandazhi.sell.dto.CartRedisDto;
import com.gandazhi.sell.dto.CartRedisListDto;
import com.gandazhi.sell.pojo.CartInfo;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Component
@Log
public class RedisToMysql {

    @Autowired
    private CartInfoMapper cartInfoMapper;

    @Scheduled(cron = "0 0 18 * * ?")//每天18点执行，把redis中的数据存进MySQL
    private void cartInfo() throws RedisToMysqlException {
        //把redis中的信息存到MySQL中
        List<CartRedisListDto> cartRedisListDtoList = Lists.newArrayList();
        Gson gson = new Gson();
        Jedis jedis = new Jedis();
        jedis.select(RedisIndex.CART.getCode());//0用来存购物车信息
        Set redisAllKey = jedis.keys("*");
        Iterator it = redisAllKey.iterator();
        //遍历redis中所有的库
        while (it.hasNext()) {
            cartRedisListDtoList.add(gson.fromJson(jedis.get(it.next().toString()), CartRedisListDto.class));
        }
        //取出cartRedisDtoList
        boolean isAllMove = false;
        List<CartRedisDto> cartRedisDtoList = cartRedisListDtoList.get(0).getCartRedisDtos();
        for (CartRedisDto cartRedisDto : cartRedisDtoList) {
            CartInfo cartInfo = new CartInfo();
            cartInfo.setOpenid(cartRedisDto.getOpenId());
            cartInfo.setProductId(cartRedisDto.getProductId());
            cartInfo.setQuantity(cartRedisDto.getQuantity());
            cartInfo.setCreateTime(cartRedisDto.getCreateTime());
            cartInfo.setUpdateTime(cartRedisDto.getUpdateTime());
            int resultCount = cartInfoMapper.insert(cartInfo);
            if (resultCount <= 0) {
                // TODO 主动抛出异常,不清空redis
                throw new RedisToMysqlException("存入MySQL时发生错误");

            } else {
                isAllMove = true;
            }
        }
        if (!isAllMove) {
            jedis.flushAll();//清空redis中所有的key
            log.info("redis存入MySQL中成功");
        }
    }
}
