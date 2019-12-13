package com.kill.killshopping.mapper;

import com.kill.killshopping.domain.entity.OrderGoods;

import java.util.List;

public interface OrderGoodsMapper {
    /**
     * 查询订单商品
     * @param orderId
     * @return
     */
    List<OrderGoods> selectByOrderId(Integer orderId);
}
