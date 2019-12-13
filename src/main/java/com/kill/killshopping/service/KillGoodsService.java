package com.kill.killshopping.service;

import com.kill.killshopping.domain.vo.KillGoodsSpecPriceDetailVo;
import com.kill.killshopping.domain.vo.KillOrderVo;
import com.kill.killshopping.mq.TopicSender;
import com.kill.killshopping.util.GridModel;
import com.kill.killshopping.util.KillConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @Date 2019/12/12 16:41
 */
@Service
public class KillGoodsService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    TopicSender topicSender;
    @Autowired
    IKillSpecManageService iKillSpecManageService;

    /**
     * 这里用到了redis缓存，首先从redis里面查询，如果查询到了直接返回，缓存里面没有直接去数据库查询
     * -------------------------------秒杀查询
     * @return
     */
    public GridModel<KillGoodsSpecPriceDetailVo> queryByPage(){
        GridModel<KillGoodsSpecPriceDetailVo> gridModel = (GridModel)redisTemplate.opsForValue().get(KillConstants.KILLGOODS_LIST);
        if (null != gridModel){
            System.out.println("缓存中得到数据");
            return gridModel;
        }

        gridModel = iKillSpecManageService.queryView(1,100);
        if (null != gridModel){
            redisTemplate.opsForValue().set(KillConstants.KILLGOODS_LIST,gridModel,500000, TimeUnit.MILLISECONDS);//set缓存
        }
        return gridModel;
    }

    /**
     * 一样，首先从缓存查询，查询到了返回，否则从数据库查询，查询后把数据库得内容丢到存里
     * -------------------------------------秒杀详情页
     * @param id
     * @return
     */
    public KillGoodsSpecPriceDetailVo detail(Integer id) {
        String killgoodDetail = KillConstants.KILLGOOD_DETAIL+id;
        KillGoodsSpecPriceDetailVo killGoodsPrice = (KillGoodsSpecPriceDetailVo)redisTemplate.opsForValue()
                .get(killgoodDetail);
        if (null != killGoodsPrice){
            System.out.println("缓存中得到数据");
            return killGoodsPrice;
        }
        killGoodsPrice = iKillSpecManageService.detailById(id);
        if (null != killGoodsPrice){
            redisTemplate.opsForValue().set(killgoodDetail,killGoodsPrice,500000, TimeUnit.MILLISECONDS);//set缓存
            System.out.println("detail：将数据存入redis中");
        }
        return killGoodsPrice;
    }
    /**
     * 首先检查你是否秒杀过，如果秒杀过不让继续秒杀，直接返回false
     * <p>
     * 然后对秒杀的产品数目-1，如果商品数目不够返回false
     * <p>
     * 秒杀成功后，缓存用户的秒杀信息
     * <p>
     * --------------------------秒杀的方法
     *
     * @param killId
     * @param userId
     * @return
     */
    public boolean getKillGoods(Integer killId, String userId) {
        //判断用户是否进行过秒杀
        Long add = redisTemplate.opsForSet().add(KillConstants.KILL_ORDER_USER + userId);
        if (add == 0) { //判断用户已经秒杀过，直接返回当次秒杀失败
            return false;
        }
        //秒杀的产品数目-1，如果商品数目不够返回false
        /*
        increment 调用的是connection.incrBy(rawKey, delta);

        Redis Incrby 命令将 key 中储存的数字加上指定的增量值。

        如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCRBY 命令。

        如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回一个错误。

        本操作的值限制在 64 位(bit)有符号数字表示之内。
         */
        if (redisTemplate.opsForValue().increment(KillConstants.KILL_GOOD_COUNT + killId, -1) < 0) {
            return false;
        }
        //秒杀成功，缓存秒杀用户和商品
        redisTemplate.opsForSet().add(KillConstants.KILLGOOD_USER, killId + userId);
        return true;
    }

    /**
     * -------------------------------------秒杀
     *
     * @param killId
     * @param userId
     * @return
     */
    public boolean kill(String killId, String userId) {
        final String killGoodCount = KillConstants.KILL_GOOD_COUNT + killId;
//        redisTemplate.opsForValue().set(killGoodCount,"10");
        try {
            //\xAC\xED\x00\x05t\x00\x011
            //首先扣库存
            Long count = redisTemplate.opsForValue().increment(killGoodCount, 1);
            Object obj = redisTemplate.execute(new SessionCallback() {
                @Override
                public Object execute(RedisOperations redisOperations) throws DataAccessException {
                    redisOperations.watch(killGoodCount);
                    Object val = redisOperations.opsForValue().get(killGoodCount);
                    Integer valint = Integer.valueOf(val.toString());

                    if (valint > 0) {
                        redisOperations.multi();
                        redisOperations.opsForValue().increment(killGoodCount, -1);
                        Object rs = redisOperations.exec();
                        System.out.println(rs);
                        return rs;
                    }
                    return null;
                }
            });

            if (null != obj) {
                redisTemplate.opsForSet().add(KillConstants.KILLGOOD_USER, killId + userId);
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 1.把相关信息（秒杀ID+用户ID）作为Key传入Reids，值为订单ID，但是现在订单ID还没有产生，所以现在redis的订单ID为一个固定的值1，代表还没生成秒杀订单。
     * 2.redis的值设置好后就把秒杀的请求交给消息队列（rabbitmq）,等服务消费方进行处理
     * 3.循环遍历，300ms轮询一次如果服务方处理完毕返回订单，如果3秒还没返回取消订单
     * @param addressId
     * @param killId
     * @param userId
     * @return
     */
    public String submitOrder(int addressId, int killId,String userId){
        KillGoodsSpecPriceDetailVo detail = detail(killId);
        KillOrderVo vo = new KillOrderVo();
        vo.setUserId(userId);
        vo.setAddressId(addressId);
        vo.setKillGoodsSpecPriceDetailVo(detail);

        ValueOperations<String,String> valueOperations = redisTemplate.opsForValue();
        //订单有效时间3秒
        String kill_order_user = KillConstants.KILL_ORDER_USER+killId+userId;//TODO
        valueOperations.set(kill_order_user,KillConstants.KILL_ORDER_USER_UNDO,300000,TimeUnit.MILLISECONDS);
        /*同步转异步，发送到消息队列*/
        topicSender.send(vo);

        String orderId = "";
        try {
            while (true){
                orderId = valueOperations.get(kill_order_user);
                if(null ==orderId){//处理超时，则直接置秒杀失败，取消秒杀订单
                    return null;
                }
                if(!KillConstants.KILL_ORDER_USER_UNDO.equals(orderId)){
                    stringRedisTemplate.delete(kill_order_user);
                    return orderId.toString();//
                }
                Thread.sleep(300l);//300ms轮循1次
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return null;
    }
}
