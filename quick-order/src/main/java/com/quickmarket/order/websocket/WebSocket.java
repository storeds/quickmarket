package com.quickmarket.order.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.alibaba.nacos.common.utils.JacksonUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: quickmarket
 * @author: cx
 * @create: 2022-03-21 11:25
 * @description:
 **/
@Data
@Slf4j
@ServerEndpoint(value = "/websocket/{sid}")
@Component
public class WebSocket {

    private String sid;
    private Session session;

    public static Map<String, Set<WebSocket>> holder = new ConcurrentHashMap<>();

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @OnOpen
    public void onOpen(@PathParam("sid") String sid, Session session) {
        log.info("WebSocket onOpen sid:{}", sid);
        this.setSession(session);
        this.setSid(sid);
        WebSocketHolder.put(sid, this);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        log.info("WebSocket onClose sid:{}", sid);
        WebSocketHolder.remove(sid, this);
    }

    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(String message, Session session) {

        log.info("WebSocket onMessage message: {}", message);
        if (message.equals("ping")) {
            sendMessage("pong");
        }
    }

    /**
     * 发生错误时调用
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("WebSocket onError");
    }

    /**
     * 发送消息
     * @param message
     */
    public void sendMessage(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            log.error("消息发送失败: {}", e.getMessage(), e);
        }
    }

    public void close() {
        try {
            this.session.close();
        } catch (IOException e) {
            log.error("session 关闭失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 检查有没有这个用户id，并且将订单添加成功的消息发送给他
     * @param orderId
     */
    public static void checkMessage( Long orderId){
        orderId = orderId - 200;
        if (!CollectionUtils.isEmpty(WebSocketHolder.get(orderId.toString()))) {
            Set<WebSocket> set = WebSocketHolder.get(orderId.toString());
            for (WebSocket socket : set) {
                Session session = socket.getSession();
                HashMap sendMessage = new HashMap();
                sendMessage.put("orderId",orderId);
                sendMessage.put("sucsess", 1);
                String message = JSON.toJSONString(sendMessage);
                socket.sendMessage(message);
            }
        }
        return;
    }

}
