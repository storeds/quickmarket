package com.quickmarket.admin.service;

import com.quickmarket.mbg.model.OmsOrderSetting;

/**
 * 订单设置Service
 * Created on
 */
public interface OmsOrderSettingService {
    /**
     * 获取指定订单设置
     */
    OmsOrderSetting getItem(Long id);

    /**
     * 修改指定订单设置
     */
    int update(Long id, OmsOrderSetting orderSetting);
}
