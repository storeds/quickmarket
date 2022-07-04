package com.quickmarket.order.dao;

import com.quickmarket.mbg.model.OmsOrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface PortalOrderItemDao {
    int insertList(@Param("list") List<OmsOrderItem> list);
}
