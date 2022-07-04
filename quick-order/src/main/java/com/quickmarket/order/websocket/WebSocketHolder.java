package com.quickmarket.order.websocket;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program: quickmarket
 * @author: cx
 * @create: 2022-03-21 11:27
 * @description:
 **/
@Slf4j
public class WebSocketHolder {

    // 用于缓存用户的会话，value之所以是一个集合，是为了保存同一个用户多终端登录的会话
    public static Map<String, Set<WebSocket>> holder = new ConcurrentHashMap<>();
    // 计数器，用于统计当前登录用户会话数
    public static AtomicInteger counter = new AtomicInteger();

    /**
     * 存储session
     * @param sid
     * @param session
     */
    public static void put(String sid, WebSocket session) {
        Set<WebSocket> sessions = holder.getOrDefault(sid, new HashSet<>());
        if (sessions.size() == 0) {
            holder.put(sid, sessions);
        }
        sessions.add(session);
        // 计数
        int c = counter.incrementAndGet();
        log.info("用户{}登录,当前在线会话为: {}", sid, c);
    }

    /**
     * 获取session
     * <p>
     * 1. sid不为空，则获取指定用户；<br/>
     * 2. sid为空，则获取所有登录用户；<br/>
     * </p>
     * @param sid
     * @return
     */
    public static Set<WebSocket> get(String sid) {
        Set<WebSocket> set = new HashSet<>();
        if (StringUtils.isEmpty(sid)) {
            // sid标识为空
            holder.values().forEach(s -> set.addAll(s));
        } else {
            // sid不为空
            if (holder.containsKey(sid)) {
                set.addAll(holder.get(sid));
            }
        }
        return set;
    }

    /**
     * 移除session
     * @param sid
     */
    public static void remove(String sid, WebSocket socket) {
        Set<WebSocket> sockets = holder.get(sid);
        socket.close();
        sockets.remove(socket);
        if (sockets.size() == 0) {
            holder.remove(sid);
        }
        int c = counter.decrementAndGet();
        log.info("用户{}退出,当前在线会话为: {}", sid, c);
    }

}
