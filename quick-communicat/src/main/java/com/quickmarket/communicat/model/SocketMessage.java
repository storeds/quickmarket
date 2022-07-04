package com.quickmarket.communicat.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * @program: quickmarket
 * @author: cx
 * @create: 2022-03-12 10:10
 * @description: socket接消息实体
 **/
@Data
@AllArgsConstructor
@ToString
public class SocketMessage {

    /**
     * 消息类型
     */
    private String messageType;
    /**
     * 消息发送者id
     */
    private Integer userId;
    /**
     * 消息接受者id或群聊id
     */
    private Integer chatId;
    /**
     * 消息内容
     */
    private String message;

    public SocketMessage() {
    }
}
