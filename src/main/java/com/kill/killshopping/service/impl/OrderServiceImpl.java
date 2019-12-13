package com.kill.killshopping.service.impl;

import com.kill.killshopping.constant.OrderStatus;
import com.kill.killshopping.domain.entity.Order;
import com.kill.killshopping.domain.entity.OrderGoods;
import com.kill.killshopping.domain.entity.UserAddress;
import com.kill.killshopping.domain.vo.KillGoodsSpecPriceDetailVo;
import com.kill.killshopping.domain.vo.KillOrderVo;
import com.kill.killshopping.exception.BusinessException;
import com.kill.killshopping.mapper.OrderGoodsMapper;
import com.kill.killshopping.mapper.OrderMapper;
import com.kill.killshopping.mapper.UserAddressMapper;
import com.kill.killshopping.service.IOrderService;
import com.kill.killshopping.util.PayStatus;
import com.kill.killshopping.util.SequenceGenerator;
import com.kill.killshopping.util.ShippingStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**  重写方法，方便以后的复用
 *
 *      0|0|0 待支付
 *       0|0|1 已付款待配货
 *       1|0|1 已配货待出库
 *       1|1|1 待收货
 *       2|1|1 已完成
 *       4|1|1 已完成
 * @Date 2019/12/13 15:36
 */
public class OrderServiceImpl implements IOrderService {
    @Resource
    OrderMapper orderMapper;
    @Resource
    OrderGoodsMapper orderGoodsMapper;
    @Resource
    UserAddressMapper userAddressMapper;

    @Resource
    SequenceGenerator sequenceGenerator;

    @Transactional
    @Override
    public Integer killOrder(KillOrderVo killOrderVo) {
        return this.killOrder(killOrderVo.getAddressId(),
                killOrderVo.getKillGoodsSpecPriceDetailVo(),killOrderVo.getUserId());
    }

    private Integer killOrder(int addressId, KillGoodsSpecPriceDetailVo killGoods, String userId) {
        //创建一个订单
        Order order = new Order();
        //从SequenceGenerator中获取订单的变化
        order.setOrderSn(sequenceGenerator.getOrderNo());
        order.setAddTime(System.currentTimeMillis());//系统当前时间
        //设置订单的状态为未确定订单
        order.setOrderStatus(OrderStatus.UNCONFIRMED.getCode());
        //未支付
        order.setPayStatus(PayStatus.UNPAID.getCode());
        //未发货
        order.setShippingStatus(ShippingStatus.UNSHIPPED.getCode());
        //获取发货地址
        UserAddress userAddress = userAddressMapper.selectByPrimaryKey(addressId);
        BeanUtils.copyProperties(userAddress,order);
        order.setUserId(userId);
        //新增订单
        orderMapper.insert(order);
        int orderId = order.getOrderId();

        //TODO
        return orderId;
    }


    @Override
    public void cancel(Integer orderId) {
        cancel(orderId,null,false);
    }



    private void cancel(Integer orderId, String userId, boolean checkUser) {
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if(order!=null){
            if(checkUser){
                if(StringUtils.isEmpty(userId)||!userId.equals(order.getUserId())){
                    throw new BusinessException("订单不存在");
                }
            }
        }else {
            throw new BusinessException("订单不存在");
        }

        order.setOrderStatus(OrderStatus.CANCELED.getCode());
        List<OrderGoods> orderGoodsList = orderGoodsMapper.selectByOrderId(orderId);
        for (OrderGoods orderGoods : orderGoodsList) {

        }
    }
}
