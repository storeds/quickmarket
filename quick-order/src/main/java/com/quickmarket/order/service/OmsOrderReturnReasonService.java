package com.quickmarket.order.service;

import com.quickmarket.common.api.CommonPage;
import com.quickmarket.common.api.CommonResult;
import com.quickmarket.mbg.model.OmsOrderReturnReason;
import com.quickmarket.order.domain.OmsOrderDetail;

import java.util.List;

/**
 * 订单原因管理Service
 * Created on
 */
public interface OmsOrderReturnReasonService {

    /**
     * 分页获取退货原因
     */
    CommonResult<List<OmsOrderReturnReason>> list(Integer pageSize, Integer pageNum);

}
