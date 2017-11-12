package com.gandazhi.sell.service.impl;

import com.gandazhi.sell.common.RedisIndex;
import com.gandazhi.sell.common.ServiceResponse;
import com.gandazhi.sell.dao.CartInfoMapper;
import com.gandazhi.sell.dao.ProductInfoMapper;
import com.gandazhi.sell.dto.CartRedisDto;
import com.gandazhi.sell.dto.CartRedisListDto;
import com.gandazhi.sell.service.ICartService;
import com.gandazhi.sell.util.DateUtil;
import com.gandazhi.sell.vo.CartInfoVo;
import com.gandazhi.sell.vo.CartVo;
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
    @Autowired
    private CartInfoMapper cartInfoMapper;

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
     * @param jedis           jedis实列化对象
     * @param KEY             jedis的key
     * @param newCartRedisDto 新写入redis中的购物车对象
     */
    private void operatingRedis(Jedis jedis, String KEY, CartRedisDto newCartRedisDto) {
        jedis.select(RedisIndex.CART.getCode());
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
                } else {
                    newCartRedisDtoList.add(cartRedisDto);
                }
            }
            if (isOldCart) {
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

    /**
     * 获取购物车中的信息
     * 先从redis中查找，再从MySQL中查找，最后整合redis中和mysql中的数据返回
     *
     * @param openId 用户的openId
     * @return
     */
    @Override
    public ServiceResponse getCart(String openId) {
        //判断openId是否为空
        if (openId.equals("")) {
            return ServiceResponse.createByErrorMessage("openId不能为空");
        }

        //1.先从redis中查找
        Jedis jedis = new Jedis();
        final String KEY = openId + "_cart";
        String cartRedisString = jedis.get(KEY);
        Gson gson = new Gson();
        CartRedisListDto cartRedisListDto = gson.fromJson(cartRedisString, CartRedisListDto.class);

        CartInfoVo cartInfoVo = new CartInfoVo();
        List<CartVo> cartVoList = Lists.newArrayList();
        Integer redisQuantity = new Integer("0");
        if (cartRedisListDto != null) {
            //redis中有数据
            CartVo redisCartVo = new CartVo();
            for (CartRedisDto cartRedisDto : cartRedisListDto.getCartRedisDtos()) {
                redisCartVo.setProductId(cartRedisDto.getProductId());
                redisCartVo.setQuantity(cartRedisDto.getQuantity());
                redisQuantity = redisQuantity + cartRedisDto.getQuantity();
                cartVoList.add(redisCartVo);
            }

        }
        //redis中没有数据，直接在MySQL中查询
        List<CartVo> mysqlCartVoList = cartInfoMapper.selectCartVoByOpenId(openId);
        Integer mysqlQuantity = new Integer("0");
        //遍历取出MySQL中的总数
        for (int i = 0; i < mysqlCartVoList.size(); i++) {
            mysqlQuantity = mysqlQuantity + mysqlCartVoList.get(i).getQuantity();
        }
        cartVoList.addAll(mysqlCartVoList);
        //整合后的list
        List<CartVo> cartVos = makeUpCartVoList(cartVoList);
        cartInfoVo.setCartVoList(cartVos);
        cartInfoVo.setCartQuantity(mysqlQuantity + redisQuantity);

        return ServiceResponse.createBySuccess(cartInfoVo);
    }

    /**
     * 整合redis中和MySQL中相同的数据
     * 把redis中和MySQL中相同productId的数量加起来
     *
     * @param cartVoList 待整合的list
     * @return 整合后的list
     */
    private List<CartVo> makeUpCartVoList(List<CartVo> cartVoList) {
        for (int i = 0; i < cartVoList.size(); i++) {
            CartVo cartVoNow = cartVoList.get(i);
            for (int j = i + 1; j < cartVoList.size(); j++) {
                CartVo cartVoNext = cartVoList.get(j);
                if (cartVoNow.getProductId().equals(cartVoNext.getProductId())) {
                    //同一个商品，整合数量
                    cartVoNow.setQuantity(cartVoNow.getQuantity() + cartVoNext.getQuantity());
                    cartVoList.remove(j);
                }
            }
        }
        return cartVoList;
    }

}
