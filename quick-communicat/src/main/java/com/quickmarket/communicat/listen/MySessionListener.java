package com.quickmarket.communicat.listen;






import com.quickmarket.communicat.netty.WebSocketHandler;
import io.netty.channel.ChannelId;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.HashMap;

/**
 * @program: quickmarket
 * @author: cx
 * @create: 2022-03-12 10:11
 * @description: session超时, 移除 websocket对应的channel
 **/
@Slf4j
public class MySessionListener implements HttpSessionListener {




    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        log.info("sessionCreated sessionId={}", httpSessionEvent.getSession().getId());
        MySessionContext.AddSession(httpSessionEvent.getSession());
    }


    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        // 获取信息
        HttpSession session = httpSessionEvent.getSession();
        Integer userId = session.getAttribute("userId") == null ? null : Integer.parseInt(session.getAttribute("userId").toString());

        //销毁时从websocket channel中移除
        if (userId != null) {
            ChannelId channelId = WebSocketHandler.userMap.get(userId);
        }
    }


    private static class MySessionContext{
        private static HashMap mymap = new HashMap();

        /**
         * 添加session
         * @param session
         */
        public static synchronized void AddSession(HttpSession session) {
            if (session != null) {
                mymap.put(session.getId(), session);
            }
        }

        /**
         * 删除session
         * @param session
         */
        public static synchronized void DelSession(HttpSession session) {
            if (session != null) {
                mymap.remove(session.getId());
            }
        }

        /**
         * 获取session
         * @param session_id
         * @return
         */
        public static synchronized HttpSession getSession(String session_id) {
            if (session_id == null) {
                return null;
            }
            return (HttpSession) mymap.get(session_id);
        }
    }

}
