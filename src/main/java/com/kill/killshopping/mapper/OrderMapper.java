package com.kill.killshopping.mapper;

import com.kill.killshopping.domain.entity.Order;

public interface OrderMapper {
    /**
     *  查询商品是否存在
     * @param orderId
     * @return
     */
    Order selectByPrimaryKey(Integer orderId);

    /**
     * 暂时只用来做秒杀新增
     * @param order
     */
    void insert(Order order);
}
