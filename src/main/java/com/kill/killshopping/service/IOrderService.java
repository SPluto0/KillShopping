package com.kill.killshopping.service;

import com.kill.killshopping.domain.vo.KillOrderVo;

/**
 * 订单类
 */
public interface IOrderService {
    /**
     * 创建秒杀订单
     * @param vo
     * @return
     */
    Integer killOrder(KillOrderVo vo);

    /**
     * 订单超时----》取消订单
     * @param orderId
     */
    void cancel(Integer orderId);
}
