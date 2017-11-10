package com.gandazhi.sell.service.impl;

import com.gandazhi.sell.common.OrderStatus;
import com.gandazhi.sell.common.PayStatus;
import com.gandazhi.sell.common.ServiceResponse;
import com.gandazhi.sell.dao.OrderDetailMapper;
import com.gandazhi.sell.dao.OrderMasterMapper;
import com.gandazhi.sell.dao.ProductInfoMapper;
import com.gandazhi.sell.dto.OrderDetailDto;
import com.gandazhi.sell.dto.OrderMasterDto;
import com.gandazhi.sell.pojo.OrderDetail;
import com.gandazhi.sell.pojo.OrderMaster;
import com.gandazhi.sell.pojo.ProductInfo;
import com.gandazhi.sell.service.IOrderService;
import com.gandazhi.sell.util.BigDecimalUtil;
import com.gandazhi.sell.util.ObjUtil;
import com.gandazhi.sell.vo.OrderVo;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Random;


@Service("iOrderService")
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private ProductInfoMapper productInfoMapper;
    @Autowired
    private OrderMasterMapper orderMasterMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Override
    public ServiceResponse createOrder(OrderMasterDto orderMasterDto, List<OrderDetailDto> orderDetailDtoList) {
        String orderId = createOrderId();
        BigDecimal orderAmount = new BigDecimal(BigInteger.ZERO);
        List<OrderDetail> orderDetailList = Lists.newArrayList();
        // 1.先查询productInfo
        for (OrderDetailDto orderDetailDto : orderDetailDtoList) {
            ProductInfo productInfo = productInfoMapper.selectByPrimaryKey(orderDetailDto.getProductId());
            if (!ObjUtil.checkObjFieldIsNotNull(productInfo)) {
                return ServiceResponse.createByErrorMessage("没有productId" + orderDetailDto.getProductId() + "的商品");
            }
            //2.计算商品总价
            orderAmount = BigDecimalUtil.mul(productInfo.getProductPrice().doubleValue(), orderDetailDto.getProductQuantity())
                    .add(orderAmount);

            //生成orderDetailList
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setProductId(productInfo.getProductId());
            orderDetail.setProductName(productInfo.getProductName());
            orderDetail.setProductPrice(productInfo.getProductPrice());
            orderDetail.setProductQuantity(orderDetailDto.getProductQuantity());
            orderDetail.setProductIcon(productInfo.getProductIcon());
            orderDetailList.add(orderDetail);
        }
        //将数据写入order_master表中

        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderMasterDto, orderMaster);
        orderMaster.setOrderId(orderId);
        orderMaster.setOrderAmount(orderAmount);
        orderMaster.setOrderStatus(OrderStatus.NEW.getCode());
        orderMaster.setPayStatus(PayStatus.WAIT.getCode());
        int resultCount = orderMasterMapper.insertSelective(orderMaster);
        if (resultCount <= 0){
            return ServiceResponse.createByErrorMessage("写入order_master表时错误");
        }

        // TODO 将数据写入order_detail表中
        for (OrderDetail orderDetail : orderDetailList){
            resultCount = orderDetailMapper.insert(orderDetail);
            if (resultCount <= 0){
                return ServiceResponse.createByErrorMessage("写入order_detail表时错误");
            }
        }

        //组装orderVo,返回orderVo
        OrderVo orderVo = new OrderVo();
        orderVo.setOrderId(orderId);
        return ServiceResponse.createBySuccess(orderVo);
    }

    //生成orderId
    private String createOrderId() {
        Random random = new Random();
        Integer number = random.nextInt(1000000);
        String orderId = System.currentTimeMillis() + number.toString();
        return orderId;
    }
}
