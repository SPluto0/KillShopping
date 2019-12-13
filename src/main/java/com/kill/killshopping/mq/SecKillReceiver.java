package com.kill.killshopping.mq;

/**
 * @Date 2019/12/13 9:38
 */

import com.alibaba.fastjson.JSON;
import com.kill.killshopping.domain.vo.KillOrderVo;
import com.kill.killshopping.service.IOrderService;
import com.kill.killshopping.util.KillConstants;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;


@Component
//如果在mq中没有队列的话  是不能够使用RabbitListener的

public class SecKillReceiver{
    @Resource
    private IOrderService orderService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    /**
     * 往前面说过的tp_order，tp_order_goods，tp_order_action等表插入数据，处理完成后，把生成的订单id放入redis
     * @param message
     * @param channel
     * @throws Exception
     */
    @RabbitListener(queues = "order.seckill" )     //必须将Listener写在方法上，如果将Listener写在类上面的话会出现Listener method 'no match' threw exception
    @RabbitHandler
    public void process(Message message, Channel channel) throws Exception {
        try {
            String msg = new String(message.getBody());
            System.out.println("UserReceiver>>>>>>>接收到消息:"+msg);
            try {
                KillOrderVo vo = JSON.parseObject(msg, KillOrderVo.class);

                String kill_order_user = KillConstants.KILL_ORDER_USER+vo.getKillGoodsSpecPriceDetailVo().getId()+vo.getUserId();
                if (null != stringRedisTemplate.opsForValue().get(kill_order_user)){//未超时，则业务处理
                    int orderId = orderService.killOrder(vo);//TODO
                    String oldstr = stringRedisTemplate.opsForValue().getAndSet(kill_order_user,String.valueOf(orderId));
                    if (null == oldstr){//已超时，生产端已拒绝
                        orderService.cancel(orderId);
                        stringRedisTemplate.delete(kill_order_user);
                    }
                }

                System.out.println("UserReceiver>>>>>>消息已消费");
                channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);//手工确认，可接下一条
            } catch (Exception e) {
                System.out.println(e.getMessage());
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,false);//失败，则直接忽略此订单

                System.out.println("UserReceiver>>>>>>拒绝消息，直接忽略");
                throw e;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}


