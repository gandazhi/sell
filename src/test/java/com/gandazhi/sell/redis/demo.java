package com.gandazhi.sell.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;

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


}
