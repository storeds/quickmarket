package com.quickmarket.order.domain;

import lombok.Data;

/**
* @desc: 类的描述:mq取消订单封装对象
* @createDate: 2022/1/23 17:26
* @version: 1.0
*/
@Data
public class MqCancelOrder {

    private Long orderId;

    private Long memberId;
}
