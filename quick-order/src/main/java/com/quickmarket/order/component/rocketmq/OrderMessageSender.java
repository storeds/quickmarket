package com.quickmarket.order.component.rocketmq;

import com.quickmarket.order.domain.OrderMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderMessageSender {

    @Value("${rocketmq.quickmall.scheduleTopic}")
    private String scheduleTopic;

    @Value("${rocketmq.quickmall.transGroup}")
    private String transGroup;

    @Value("${rocketmq.quickmall.transTopic}")
    private String transTopic;

    @Value("${rocketmq.quickmall.asyncOrderTopic}")
    private String asyncOrderTopic;

    private String TAG = "cancelOrder";
    private String TXTAG = "trans";
    private String ORDERTAG = "create-order";
    private String NORMALORDERTAG = "norcreate-order";

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 发送延时订单
     * @param cancelId
     *      订单ID orderId:promotionId 活动ID
     * @return
     */
    public boolean sendTimeOutOrderMessage(String cancelId){
        Message message = MessageBuilder.withPayload(cancelId)
                .setHeader(RocketMQHeaders.KEYS, cancelId)
                .build();
        SendResult result = rocketMQTemplate.syncSend(scheduleTopic+":"+TAG,message,5000,15);
        return SendStatus.SEND_OK == result.getSendStatus();
    }

    /**
     * 事务消息,弱关联分布式系统柔性事务解决方案
     */
    public LocalTransactionState send2CartTransMessage(){
        Message message = MessageBuilder.withPayload("").setHeader(RocketMQHeaders.KEYS, "").build();
        TransactionSendResult transactionSendResult = rocketMQTemplate.sendMessageInTransaction(transGroup, transTopic + ":" + TXTAG, message, null);
        log.info("事务消息-本地事务执行状态:{}" + transactionSendResult.toString()
                ,transactionSendResult.getLocalTransactionState().name());
        return  transactionSendResult.getLocalTransactionState();
    }

    /**
     * 发送订单消息
     * @return
     */
    public boolean sendCreateOrderMsg(OrderMessage message){
        SendResult result = rocketMQTemplate.syncSend(asyncOrderTopic+":"+ORDERTAG,message);
        return SendStatus.SEND_OK == result.getSendStatus();
    }

    /**
     * 发送订单消息normal
     * @return
     */
    public boolean sendNormalCreateOrderMsg(OrderMessage message){
        SendResult result = rocketMQTemplate.syncSend(asyncOrderTopic+":"+NORMALORDERTAG,message);
        return SendStatus.SEND_OK == result.getSendStatus();
    }

    /**
     * 发送延时同步库存消息，60s后同步库存
     * @param productId
     * @param promotionId
     * @return
     */
    public boolean sendStockSyncMessage(Long productId,Long promotionId){
        Message message = MessageBuilder.withPayload(productId+":"+promotionId).build();
        SendResult result = rocketMQTemplate.syncSend("stock-sync",message,5000,5);
        return SendStatus.SEND_OK == result.getSendStatus();
    }

    /**
     * 发送延时同步库存消息，60s后同步库存
     * @param productId
     * @param promotionId
     * @return
     */
    public boolean sendNormalStockSyncMessage(Long productId,Long promotionId){
        Message message = MessageBuilder.withPayload(productId+":"+promotionId).build();
        SendResult result = rocketMQTemplate.syncSend("normal_stock-sync",message,5000,5);
        return SendStatus.SEND_OK == result.getSendStatus();
    }




}
