package com.gandazhi.sell.service.impl;

import com.gandazhi.sell.common.OrderStatus;
import com.gandazhi.sell.common.PayStatus;
import com.gandazhi.sell.common.ServiceResponse;
import com.gandazhi.sell.customException.WriteDbException;
import com.gandazhi.sell.dao.CartInfoMapper;
import com.gandazhi.sell.dao.OrderDetailMapper;
import com.gandazhi.sell.dao.OrderMasterMapper;
import com.gandazhi.sell.dao.ProductInfoMapper;
import com.gandazhi.sell.dto.*;
import com.gandazhi.sell.pojo.OrderDetail;
import com.gandazhi.sell.pojo.OrderMaster;
import com.gandazhi.sell.pojo.ProductInfo;
import com.gandazhi.sell.service.IOrderService;
import com.gandazhi.sell.util.BigDecimalUtil;
import com.gandazhi.sell.util.ObjUtil;
import com.gandazhi.sell.vo.CartVo;
import com.gandazhi.sell.vo.OrderVo;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import lombok.extern.java.Log;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.Jedis;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Random;

@Log
@Service("iOrderService")
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private ProductInfoMapper productInfoMapper;
    @Autowired
    private OrderMasterMapper orderMasterMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private CartInfoMapper cartInfoMapper;

    //生成orderId
    private String createOrderId() {
        Random random = new Random();
        Integer number = random.nextInt(1000000);
        String orderId = System.currentTimeMillis() + number.toString();
        return orderId;
    }

    @Override
    public ServiceResponse createOrder(OrderMasterDto orderMasterDto) {
        if (orderMasterDto.getBuyerOpenid().equals("")) {
            return ServiceResponse.createByErrorMessage("用户未登录，请登录");
        }
        //先从redis和MySQL中取出购物车的数据
        String orderId = createOrderId();
        Jedis jedis = new Jedis();
        final String KEY = orderMasterDto.getBuyerOpenid() + "_cart";
        String redisCartJson = jedis.get(KEY);
        jedis.close();
        BigDecimal orderAmount = new BigDecimal(BigInteger.ZERO);
        List<ProductInfoDto> productInfoDtoList = Lists.newArrayList();

        if (redisCartJson == null) {
            //redis中没有数据，从MySQL中查找数据
            productInfoDtoList = getMySQLCart(orderMasterDto.getBuyerOpenid());
            if (CollectionUtils.isEmpty(productInfoDtoList)){
                //mysql中也没有数据
                return ServiceResponse.createByErrorMessage("redis和MySQL中都没有数据");
            }
            for (ProductInfoDto productInfoDto : productInfoDtoList) {
                orderAmount = BigDecimalUtil.mul(productInfoDto.getProductQuantity().doubleValue(), productInfoDto.getProductPrice().doubleValue()).add(orderAmount);
            }
        } else {
            ProductInfoDto productInfoDto = new ProductInfoDto();
            //redis中有数据，先查了redis中的数据，再查MySQL中的数据，最后整合
            List<CartRedisDto> cartRedisDtoList = getRedisCart(redisCartJson);
            for (CartRedisDto cartRedisDto : cartRedisDtoList) {
                ProductInfo productInfo = productInfoMapper.selectByPrimaryKey(cartRedisDto.getProductId());
                productInfoDto.setProductName(productInfo.getProductName());
                productInfoDto.setProductId(cartRedisDto.getProductId());
                productInfoDto.setProductIcon(productInfo.getProductIcon());
                productInfoDto.setProductQuantity(cartRedisDto.getQuantity());
                productInfoDto.setProductPrice(productInfo.getProductPrice());
                productInfoDtoList.add(productInfoDto);
                //计算出redis中的总价
                orderAmount = BigDecimalUtil.mul(productInfo.getProductPrice().doubleValue(), cartRedisDto.getQuantity().doubleValue())
                        .add(orderAmount);
            }

            List<ProductInfoDto> mysqlProductInfoDtoList = getMySQLCart(orderMasterDto.getBuyerOpenid());
            //计算MySQL中的总和
            for (ProductInfoDto mysqlProductInfoDto : mysqlProductInfoDtoList){
                ProductInfo productInfo = productInfoMapper.selectByPrimaryKey(mysqlProductInfoDto.getProductId());
                orderAmount = BigDecimalUtil.mul(mysqlProductInfoDto.getProductQuantity().doubleValue(), productInfo.getProductPrice().doubleValue())
                        .add(orderAmount);
            }
            //将MySQL中的数据取出，整合进productInfoDtoList
            productInfoDtoList.addAll(mysqlProductInfoDtoList);

            //整合productInfoDtoList中的数量
            for (int i = 0; i < productInfoDtoList.size(); i++) {
                for (int j = i + 1; j < productInfoDtoList.size(); j++) {
                    if (productInfoDtoList.get(i).getProductId().equals(productInfoDtoList.get(j).getProductId())){
                        //整合数量
                        productInfoDtoList.get(i).setProductQuantity(productInfoDtoList.get(i).getProductQuantity() + productInfoDtoList.get(j).getProductQuantity());
                        productInfoDtoList.remove(j);
                    }
                }
            }
        }
        //写入数据库
        boolean isSuccess = writeOrder(orderAmount, orderId, orderMasterDto, productInfoDtoList);
        if (!isSuccess){
            return ServiceResponse.createByErrorMessage("数据库写入order时错误");
        }
        OrderVo orderVo = new OrderVo();
        orderVo.setOrderId(orderId);
        return ServiceResponse.createBySuccess(orderVo);
    }

    //从MySQL中获得购物车信息
    private List<ProductInfoDto> getMySQLCart(String openId) {
        List<CartVo> cartVoList = cartInfoMapper.selectCartVoByOpenId(openId);
        List<ProductInfoDto> productInfoDtoList = Lists.newArrayList();
        for (CartVo cartVo : cartVoList) {
            //遍历出总价
            ProductInfo productInfo = productInfoMapper.selectByPrimaryKey(cartVo.getProductId());
            ProductInfoDto productInfoDto = new ProductInfoDto();
            BeanUtils.copyProperties(productInfo, productInfoDto);
            productInfoDto.setProductQuantity(cartVo.getQuantity());
            productInfoDtoList.add(productInfoDto);
        }
        return productInfoDtoList;
    }

    //从redis中获得购物车信息
    private List<CartRedisDto> getRedisCart(String redisJson) {
        List<CartRedisDto> cartRedisDtoList = Lists.newArrayList();
        Gson gson = new Gson();
        CartRedisListDto cartRedisListDto = gson.fromJson(redisJson, CartRedisListDto.class);
        //遍历取出CartRedisDto
        for (CartRedisDto cartRedisDto : cartRedisListDto.getCartRedisDtos()) {
            cartRedisDtoList.add(cartRedisDto);
        }
        return cartRedisDtoList;
    }

    //将数据写入到数据库中 有order_master表和order_detail表
    @Transactional
    public boolean writeOrder(BigDecimal orderAmount, String orderId, OrderMasterDto orderMasterDto, List<ProductInfoDto> productInfoDtoList) {
        boolean isSuccess = true;
        //写入order_master表
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderMasterDto, orderMaster);
        orderMaster.setOrderAmount(orderAmount);
        orderMaster.setOrderId(orderId);
        orderMaster.setOrderStatus(OrderStatus.NEW.getCode());
        orderMaster.setPayStatus(PayStatus.WAIT.getCode());
        int resultCount = orderMasterMapper.insertSelective(orderMaster);
        if (resultCount <= 0) {
            //手动抛出异常
            try {
                throw new WriteDbException("写入order_master时错误");
            } catch (WriteDbException e) {
                isSuccess = false;
                log.warning("写入order_master时错误");
            }
        }
        //写入order_detail表
        OrderDetail orderDetail = new OrderDetail();
        for (ProductInfoDto productInfoDto : productInfoDtoList) {
            //更新product_info中的库存
            ProductInfo productInfo = productInfoMapper.selectByPrimaryKey(productInfoDto.getProductId());
            productInfo.setProductStock(productInfo.getProductStock() - productInfoDto.getProductQuantity());
            resultCount = productInfoMapper.updateByPrimaryKeySelective(productInfo);
            if (resultCount <= 0){
                try {
                    throw new WriteDbException("更新product_info表的库存时错误");
                } catch (WriteDbException e) {
                    isSuccess = false;
                    log.warning("更新product_info表的库存时错误");
                }
            }
            orderDetail.setOrderId(orderId);
            orderDetail.setProductId(productInfoDto.getProductId());
            orderDetail.setProductName(productInfoDto.getProductName());
            orderDetail.setProductPrice(productInfoDto.getProductPrice());
            orderDetail.setProductQuantity(productInfoDto.getProductQuantity());
            orderDetail.setProductIcon(productInfoDto.getProductIcon());
            resultCount = orderDetailMapper.insertSelective(orderDetail);
            if (resultCount <= 0) {
                //手动抛出异常
                try {
                    throw new WriteDbException("写入order_detail时错误");
                } catch (WriteDbException e) {
                    isSuccess = false;
                    log.warning("写入order_detail时错误");
                }
            }
        }
        if (isSuccess){
            //清空redis和MySQL中购物车的信息
            Jedis jedis = new Jedis();
            final String KEY = orderMasterDto.getBuyerOpenid() + "_cart";
            jedis.del(KEY);
            jedis.close();
            //删除MySQL中的数据
            resultCount = cartInfoMapper.deleteByOpenId(orderMasterDto.getBuyerOpenid());
            if (resultCount < 0){
                try {
                    throw new WriteDbException("删除mysql中的购物车信息失败");
                } catch (WriteDbException e) {
                    isSuccess = false;
                    log.warning("删除mysql中的购物车信息失败");
                }
            }else if (resultCount == 0){
                log.info("MySQL数据库中没有数据");
            }
        }
        return isSuccess;
    }
}
