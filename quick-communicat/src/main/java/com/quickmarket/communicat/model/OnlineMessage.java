package com.quickmarket.communicat.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * @program: quickmarket
 * @author: cx
 * @create: 2022-03-12 10:09
 * @description: socket接消息实体
 **/
@Data
@AllArgsConstructor
@ToString
public class OnlineMessage {

    /**
     * 消息发送者id
     */
    private String sendId;
    /**
     * 消息接受者id
     */
    private String acceptId;
    /**
     * 消息内容
     */
    private String message;

    /**
     * 头像
     */
    private String headImg;

    public OnlineMessage() {
    }
}
