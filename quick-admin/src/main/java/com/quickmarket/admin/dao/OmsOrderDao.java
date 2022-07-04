package com.quickmarket.admin.dao;

import com.quickmarket.admin.dto.OmsOrderDeliveryParam;
import com.quickmarket.admin.dto.OmsOrderDetail;
import com.quickmarket.admin.dto.OmsOrderQueryParam;
import com.quickmarket.mbg.model.OmsOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单自定义查询Dao
 * Created on
 */
public interface OmsOrderDao {
    /**
     * 条件查询订单
     */
    List<OmsOrder> getList(@Param("queryParam") OmsOrderQueryParam queryParam);

    /**
     * 批量发货
     */
    int delivery(@Param("list") List<OmsOrderDeliveryParam> deliveryParamList);

    /**
     * 获取订单详情
     */
    OmsOrderDetail getDetail(@Param("id") Long id);
}
