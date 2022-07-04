package com.quickmarket.admin.service;

import com.quickmarket.mbg.model.UmsMemberLevel;

import java.util.List;

/**
 * 会员等级管理Service
 * Created on
 */
public interface UmsMemberLevelService {
    /**
     * 获取所有会员登录
     * @param defaultStatus 是否为默认会员
     */
    List<UmsMemberLevel> list(Integer defaultStatus);
}
