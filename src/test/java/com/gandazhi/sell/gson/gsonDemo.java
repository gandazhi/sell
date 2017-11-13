package com.gandazhi.sell.gson;

import com.gandazhi.sell.customException.WriteDbException;
import com.gandazhi.sell.dao.CartInfoMapper;
import com.gandazhi.sell.dto.CartRedisDto;
import com.gandazhi.sell.pojo.CartInfo;
import com.gandazhi.sell.util.DateUtil;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

@RunWith(SpringRunner.class)
@SpringBootTest
public class gsonDemo {

    @Autowired
    private CartInfoMapper cartInfoMapper;

    @Test
    public void  demo(){
        List<CartRedisDto> cartRedisDtoList = Lists.newArrayList();
        CartRedisDto cartRedisDto = new CartRedisDto();
        cartRedisDto.setOpenId("123456");
        cartRedisDto.setProductId("11111");
        cartRedisDto.setQuantity(2);
        Date date = new Date();
        cartRedisDto.setCreateTime(date);
        cartRedisDto.setUpdateTime(date);
        cartRedisDtoList.add(cartRedisDto);

        CartRedisDto cartRedisDto2 = new CartRedisDto();
        cartRedisDto2.setOpenId("123456");
        cartRedisDto2.setProductId("11111");
        cartRedisDto2.setQuantity(2);
        Date date2 = new Date();
        cartRedisDto.setCreateTime(date2);
        cartRedisDto.setUpdateTime(date2);
        cartRedisDtoList.add(cartRedisDto2);

        Gson gson = new Gson();
        String gsonObject = gson.toJson(cartRedisDtoList);

        Jedis jedis = new Jedis();
        jedis.set("cartRedis", gsonObject);
        jedis.close();
        System.out.println(gsonObject);
    }

    @Test
    public void dateTest() throws ParseException {
        Long time = new Date().getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String d = format.format(time);
        Date date=format.parse(d);


        System.out.println(date);
    }

    @Test
    @Transactional
    public void testA(){
        CartInfo cartInfo = new CartInfo();
        cartInfo.setUpdateTime(DateUtil.getCurrentTime());
        cartInfo.setUpdateTime(DateUtil.getCurrentTime());
        cartInfo.setProductId("11111");
        cartInfo.setQuantity(2);
        cartInfo.setOpenid("123456");
        cartInfoMapper.insertSelective(cartInfo);
        try {
            throw new WriteDbException("手动抛出异常");
        } catch (WriteDbException e) {
            System.out.println("抛异常了.....");
        }
    }
}
