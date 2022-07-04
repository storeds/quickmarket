package com.quickmarket.order.service;


import com.quickmarket.order.domain.OmsOrderReturnApplyParam;

/**
 * 订单退货管理Service
 */
public interface OmsPortalOrderReturnApplyService {
    /**
     * 提交申请
     */
    int create(OmsOrderReturnApplyParam returnApply);

    int receive(OmsOrderReturnApplyParam returnApply);
}
