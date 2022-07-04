package com.quickmarket.admin.dto;

import com.quickmarket.mbg.model.OmsOrder;
import com.quickmarket.mbg.model.OmsOrderItem;
import com.quickmarket.mbg.model.OmsOrderOperateHistory;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 订单详情信息
 */
public class OmsOrderDetail extends OmsOrder {
    @Getter
    @Setter
    private List<OmsOrderItem> orderItemList;
    @Getter
    @Setter
    private List<OmsOrderOperateHistory> historyList;
}
