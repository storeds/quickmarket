package com.quickmarket.admin.service;

public interface ItemService {

    /**
     * 静态化商品详情页
     *
     * @param id
     * @return
     */
    String toStatic(Long id);
}
