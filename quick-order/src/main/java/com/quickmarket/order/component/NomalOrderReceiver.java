package com.quickmarket.order.component;

import com.alibaba.fastjson.JSON;
import com.quickmarket.common.api.CommonResult;
import com.quickmarket.common.api.ResultCode;
import com.quickmarket.common.constant.RedisKeyPrefixConst;
import com.quickmarket.mbg.mapper.OmsOrderItemMapper;
import com.quickmarket.mbg.mapper.OmsOrderMapper;
import com.quickmarket.mbg.model.OmsOrder;
import com.quickmarket.mbg.model.OmsOrderItem;
import com.quickmarket.mbg.model.UmsMemberReceiveAddress;
import com.quickmarket.order.component.rocketmq.OrderMessageSender;
import com.quickmarket.order.dao.PortalOrderDao;
import com.quickmarket.order.dao.PortalOrderItemDao;
import com.quickmarket.order.domain.OrderMessage;
import com.quickmarket.order.util.RedisOpsUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @program: quickmarket
 * @author: cx
 * @create: 2022-03-20 20:57
 * @description:
 **/
@Component
@Slf4j
@RocketMQMessageListener(topic = "${rocketmq.quickmall.asyncOrderTopic}", consumerGroup = "asyncOrderGroupNor", selectorExpression = "norcreate-order")
public class NomalOrderReceiver implements RocketMQListener<OrderMessage> {


    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private LocalCache<Object> cache;

    @Autowired
    private OrderMessageSender orderMessageSender;

    @Autowired
    private RedisOpsUtil redisOpsUtil;

    @Autowired
    private OmsOrderMapper orderMapper;

    @Autowired
    private OmsOrderItemMapper orderItemMapper;

    @Override
    public void onMessage(OrderMessage orderMessage) {
        if (orderMessage == null) {
            return;
        }
        // 防止重复消费，也可以将缓存换成redis
        Long uniqueId = orderMessage.getUniqueId();
        String key = String.valueOf(uniqueId);
        ThreadLocal<Object> threadLocal = new ThreadLocal<>();
        threadLocal.set(cache);

        // 如果缓存中没有这个就将其移出
        if (cache.getCache(key) == null) {
            cache.setLocalCache(key, uniqueId);
            orderMessage.getOrder().setId(uniqueId);



            // 下订单，返回成功消息
            order(orderMessage);
            cache.remove(key);
        }else {
            threadLocal.remove();
            return;
        }

    }

    /**
     * 添加订单到数据库
     * @param orderMessage
     */
    public void order(OrderMessage orderMessage){
        OmsOrder order = new OmsOrder();
        //订单编号
        String orderSn = orderMessage.getOrder().getOrderSn();
        Integer limit = orderMessage.getFlashPromotionLimit();
        Date endDate = orderMessage.getFlashPromotionEndDate();
        Long memberId = orderMessage.getUserId();
        Long productId = orderMessage.getOrderItem().getProductId();


        log.info("下单成功{}",orderMessage);
        // 发送延时订单消息，正式下单并发送延时订单消息
        //        orderMessageSender.sendTimeOutOrderMessage()
        redisOpsUtil.set(RedisKeyPrefixConst.NORMAL_ASYNC_WAITING_PREFIX + memberId
                + ":" + productId, Integer.toString(1),60, TimeUnit.SECONDS);
        //添加redis服务监听.有用户id和下单id
        publish(orderMessage);
    }

    /**
     * 发布订阅将下订单成功的消息发送到客户端
     * @param orderMessage
     */
    public void publish(OrderMessage orderMessage){
        String mes =  orderMessage.getUserId() + "," + orderMessage.getOrder().getId();
        redisTemplate.convertAndSend("ordersucess",mes);
//        log.info("{}",orderMessage);
//        redisTemplate.convertAndSend("ordersucess",orderMessage);
    }

    /**
     * 下单
     * @param order
     * @param orderItem
     * @param flashPromotionRelationId
     * @return
     */
    public Long asyncCreateOrder(OmsOrder order,OmsOrderItem orderItem,Long flashPromotionRelationId) {
        // 减库存在sku表中减
        Integer result = 1;

        if (result <= 0) {
            throw new RuntimeException("订单扣减失败");
        }
        //插入订单记录
        orderMapper.insertSelective(order);

        //OrderItem关联
        orderItem.setOrderId(order.getId());
        orderItem.setOrderSn(order.getOrderSn());

        //插入orderItem
        orderItemMapper.insertSelective(orderItem);

        /**
         * 如果创建订单成功，发送定时消息。20秒后如果没有支付，则取消订单释放库存
         */
        try {
            boolean sendStatus = orderMessageSender.sendTimeOutOrderMessage(order.getId() + ":" + flashPromotionRelationId + ":" + orderItem.getProductId());
            if (!sendStatus) {
                throw new RuntimeException("订单超时取消消息发送失败！");
            }
        } catch (Exception e) {
            throw new RuntimeException("订单超时取消消息发送失败！");
        }

        return order.getId();
    }
}
