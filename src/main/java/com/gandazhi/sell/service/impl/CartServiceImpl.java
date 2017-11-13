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
import org.springframework.util.CollectionUtils;
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
        //1.生成redis set的key，和当前系统时间
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

        return ServiceResponse.createBySuccessMesage("添加购物车信息到redis中成功");
    }

    @Override
    public ServiceResponse delCart(String productId, Integer quantity, String openId) {
        /*
        判断productId是否在product_info中有，有继续，没有返回没有productId这个商品
        判断quantity是否为负数，如果是正数，就返回参数错误，quantity不能为正数
        先删除redis中的数据
        如果redis中有数量，就删除，如果redis中的数据删了还不够，就继续删MySQL中的数据，如果MySQL中没有，就返回参数错误
        如果redis中没有数量，就直接向MySQL中删除，MySQL中有，但如果没MySQL不够删，就返回参数错误，MySQL中没有就返回参数错误
         */
        if (quantity >= 0) {
            return ServiceResponse.createByErrorMessage("参数错误，quantity不能≥0");
        }
        int resultCount = productInfoMapper.selectCountByProductId(productId);
        if (resultCount <= 0) {
            return ServiceResponse.createByErrorMessage("参数错误，没有找到id为" + productId + "的商品");
        }
        Jedis jedis = new Jedis();
        final String KEY = openId + "_cart";
        Gson gson = new Gson();
        String cartRedisJson = jedis.get(KEY);
        if (cartRedisJson == null) {
            //redis中没有这个数据，向MySQL中减少
            Integer reduceForMysqlQuantity = cartInfoMapper.selectQuantityForProductIdAndOpenId(productId, openId);
            if (Math.abs(quantity) > reduceForMysqlQuantity) {
                return ServiceResponse.createByErrorMessage("参数错误，购物车中的数量比要减少的数量更少");
            }
            if (Math.abs(quantity) == reduceForMysqlQuantity) {
                //直接删除MySQL中的这条记录
                resultCount = cartInfoMapper.deleteByProductIdAndOpenId(productId, openId);
                if (resultCount < 0) {
                    return ServiceResponse.createByErrorMessage("MySQL数量减少失败");
                }
            }
            if (Math.abs(quantity) < reduceForMysqlQuantity) {
                //减少MySQL中的quantity
                //更新MySQL中的数量
                Integer updateQuantity = reduceForMysqlQuantity + quantity;
                resultCount = cartInfoMapper.updateQuantityByProductIdAndOpenId(productId, openId, updateQuantity);
                if (resultCount <= 0) {
                    return ServiceResponse.createByErrorMessage("MySQL更新数量失败");
                }
            }
        } else {
            //redis中有这个数据，判断redis中的quantity的绝对值是不是大于传来的quantity的绝对值
            CartRedisListDto newCartRedListDto = new CartRedisListDto();
            List<CartRedisDto> cartRedisDtoList = Lists.newArrayList();
            CartRedisListDto cartRedisListDto = gson.fromJson(cartRedisJson, CartRedisListDto.class);
            boolean isAllReduceForRedis = true;
            for (CartRedisDto cartRedisDto : cartRedisListDto.getCartRedisDtos()) {
                if (cartRedisDto.getProductId().equals(productId)) {
                    if (cartRedisDto.getQuantity() >= Math.abs(quantity)) {
                        //redis中的数量大于或等于传来的quantity的绝对值，直接从redis中的减
                        isAllReduceForRedis = false;
                        cartRedisDto.setQuantity(cartRedisDto.getQuantity() + quantity);
                        cartRedisDto.setUpdateTime(DateUtil.getCurrentTime());

                    } else {
                        //redis中的数量小于传来的quantity的绝对值
                        jedis.del(KEY);
                        Integer reduceForMysqlQuantity = Math.abs(quantity) - cartRedisDto.getQuantity();
                        Integer mysqlQuantity = cartInfoMapper.selectQuantityForProductIdAndOpenId(productId, openId);
                        if (reduceForMysqlQuantity > mysqlQuantity) {
                            return ServiceResponse.createByErrorMessage("参数错误，购物车中的数量比要减少的数量更少");
                        }
                        if (reduceForMysqlQuantity == mysqlQuantity) {
                            //直接删除MySQL中的这条记录
                            resultCount = cartInfoMapper.deleteByProductIdAndOpenId(productId, openId);
                            if (resultCount < 0) {
                                return ServiceResponse.createByErrorMessage("MySQL数量减少失败");
                            }
                        } else {
                            //更新MySQL中的数量
                            Integer updateQuantity = mysqlQuantity - reduceForMysqlQuantity;
                            resultCount = cartInfoMapper.updateQuantityByProductIdAndOpenId(productId, openId, updateQuantity);
                            if (resultCount <= 0) {
                                return ServiceResponse.createByErrorMessage("MySQL更新数量失败");
                            }
                        }

                    }
                }
                cartRedisDtoList.add(cartRedisDto);
            }
            if (!isAllReduceForRedis) {
                //组装CartRedisListDto
                newCartRedListDto.setCartRedisDtos(cartRedisDtoList);
                //把quantity为0的从redis中删掉
                for (int i = 0; i <= newCartRedListDto.getCartRedisDtos().size(); i++) {
                    if (newCartRedListDto.getCartRedisDtos().get(i).getQuantity() == 0) {
                        newCartRedListDto.getCartRedisDtos().remove(i);
                    }
                }
                if (CollectionUtils.isEmpty(newCartRedListDto.getCartRedisDtos())) {
                    jedis.del(KEY);
                } else {
                    jedis.set(KEY, gson.toJson(newCartRedListDto));
                }
            }
        }
        CartInfoVo cartInfoVo = new CartInfoVo();
        List<CartVo> cartVoList = cartInfoMapper.selectCartVoByOpenId(openId);
        Integer mysqlCount = new Integer("0");
        //遍历取出MySQL中的数量
        for (CartVo cartVo : cartVoList) {
            mysqlCount = mysqlCount + cartVo.getQuantity();
        }
        String nowCartRedisJson = jedis.get(KEY);
        Integer redisCount = new Integer("0");
        if (nowCartRedisJson != null) {
            CartRedisListDto nowCartRedisListDto = gson.fromJson(nowCartRedisJson, CartRedisListDto.class);
            //遍历取出数量
            for (CartRedisDto cartRedisDto : nowCartRedisListDto.getCartRedisDtos()) {
                redisCount = redisCount + cartRedisDto.getQuantity();
            }
        }

        cartInfoVo.setCartVoList(cartVoList);
        cartInfoVo.setCartQuantity(mysqlCount + redisCount);
        return ServiceResponse.createBySuccess(cartInfoVo);
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
