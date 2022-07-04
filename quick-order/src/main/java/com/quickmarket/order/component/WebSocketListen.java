package com.quickmarket.order.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.quickmarket.order.domain.OrderMessage;
import com.quickmarket.order.websocket.WebSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @program: quickmarket
 * @author: cx
 * @create: 2022-03-21 14:04
 * @description:
 **/
@Slf4j
@Component
public class WebSocketListen implements MessageListener {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private WebSocket webSocket;

    @Override
    public void onMessage(Message message, byte[] bytes) {
        String order = new String(message.getBody(), StandardCharsets.UTF_8);
        log.info("获取到订阅的消息{}", order);
        String[] s = order.split(",");
        log.info("用户id:{}",s[0].replace("\"",""));
        log.info("订单id:{}",Long.parseLong(s[1].replace("\"","")));
        String uid = s[0].replace("\"","");
        Long orderid = Long.parseLong(s[1].replace("\"",""));
//         websocket发送成功的消息

    }
}
