package com.gandazhi.sell.service.impl;

import com.gandazhi.sell.common.ServiceResponse;
import com.gandazhi.sell.dao.ProductInfoMapper;
import com.gandazhi.sell.dto.CartRedisDto;
import com.gandazhi.sell.dto.CartRedisListDto;
import com.gandazhi.sell.service.ICartService;
import com.gandazhi.sell.util.DateUtil;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.*;

@Service("iCartService")
public class CartServiceImpl implements ICartService {

    @Autowired
    private ProductInfoMapper productInfoMapper;

    @Override
    public ServiceResponse addCart(String openId, String productId, Integer quantity) {
        /**
         * 添加购物车
         * 传用户openId,商品id，和添加这个商品的个数
         * 把这些购物车信息存在redis中
         * 设置定时任务，定时同步redis中的数据到MySQL中
         */
        //验证数据库中是否有传来的productId，如果没有则返回错误
        int resultCount = productInfoMapper.selectCountByProductId(productId);
        if (resultCount <= 0) {
            return ServiceResponse.createByErrorMessage("没有找到" + productId + "这个商品，参数错误");
        }

        //把购物车信息存到redis中
        //1.生成redis hset的key，和当前系统时间
        final String KEY = openId + "_cart";
        //2.生成存redis需要的cartRedisDtoList
        CartRedisDto cartRedisDto = new CartRedisDto();
        cartRedisDto.setOpenId(openId);
        cartRedisDto.setProductId(productId);
        cartRedisDto.setQuantity(quantity);
        cartRedisDto.setCreateTime(DateUtil.getCurrentTime());
        cartRedisDto.setUpdateTime(DateUtil.getCurrentTime());

        //把cartRedisDtoList以json的格式存去redis中
        Jedis jedis = new Jedis();
        operatingRedis(jedis, KEY, cartRedisDto);

        // TODO 设置定时任务，定时同步redis中的数据到MySQL中
        return ServiceResponse.createBySuccessMesage("添加购物车信息到redis中成功");
    }

    @Override
    public ServiceResponse delCart(Integer cartId) {
        return null;
    }

    /**
     * 操作redis的方法
     *
     * @param jedis jedis实列化对象
     * @param KEY   jedis的key
     */
    private void operatingRedis(Jedis jedis, String KEY, CartRedisDto newCartRedisDto) {
        Gson gson = new Gson();
        //先判断传来的参数中productId是否存在于redis中，有就更新这个productId的数量，没有就直接新增
        String cartRedisString = jedis.get(KEY);

        List<CartRedisDto> newCartRedisDtoList = Lists.newArrayList();

        CartRedisListDto oldCartRedisListDto = gson.fromJson(cartRedisString, CartRedisListDto.class);
        if (oldCartRedisListDto == null) {
            List<CartRedisDto> cartRedisDtoList = Lists.newArrayList();
            cartRedisDtoList.add(newCartRedisDto);
            CartRedisListDto cartRedisListDto = new CartRedisListDto();
            cartRedisListDto.setCartRedisDtos(cartRedisDtoList);
            jedis.set(KEY, gson.toJson(cartRedisListDto));
            jedis.close();
        } else {
            List<CartRedisDto> oldCartRedisDtoList = oldCartRedisListDto.getCartRedisDtos();
            List<CartRedisDto> checkCartRedisDto = Lists.newArrayList();
            boolean isOldCart = true;
            for (CartRedisDto cartRedisDto : oldCartRedisDtoList) {
                if (cartRedisDto.getProductId().equals(newCartRedisDto.getProductId())) {
                    //redis中有这条数据,更新数量
                    cartRedisDto.setQuantity(cartRedisDto.getQuantity() + newCartRedisDto.getQuantity());
                    cartRedisDto.setUpdateTime(DateUtil.getCurrentTime());
                    newCartRedisDtoList.add(cartRedisDto);
                    isOldCart = false;
                    break;
                }else {
                    newCartRedisDtoList.add(cartRedisDto);
                }
            }
            if (isOldCart){
                //redis中没有这条数据，直接插入
                checkCartRedisDto.add(newCartRedisDto);
            }
            newCartRedisDtoList.addAll(checkCartRedisDto);
            CartRedisListDto cartRedisListDto = new CartRedisListDto();
            cartRedisListDto.setCartRedisDtos(newCartRedisDtoList);
            jedis.set(KEY, gson.toJson(cartRedisListDto));
            jedis.close();
        }
    }
}
