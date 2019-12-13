package com.kill.killshopping.mq;

import com.alibaba.fastjson.JSON;
import com.kill.killshopping.domain.vo.KillOrderVo;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @Date 2019/12/12 15:38
 */
@Component
public class TopicSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(KillOrderVo vo) {
        String msg = JSON.toJSONString(vo);
        System.out.println("TopicSender send the 3rd : " + msg);
        //                                  String exchange,            String routingKey, Object object
        //                                  order.seckill.producer          order.seckill
        this.rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_SECKILL, RabbitConfig.KEY_SECKILL, msg);

    }

}
