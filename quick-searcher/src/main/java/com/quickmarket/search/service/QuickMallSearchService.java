package com.quickmarket.search.service;

import com.quickmarket.search.vo.ESRequestParam;
import com.quickmarket.search.vo.ESResponseResult;
import org.springframework.stereotype.Service;

/**
 * @program: quickmarket
 * @author: cx
 * @create: 2022-02-21 22:26
 * @description:
 **/

public interface QuickMallSearchService {

    /**
     * @param param 检索的所有参数
     * @return  返回检索的结果，里面包含页面需要的所有信息
     */
    ESResponseResult search(ESRequestParam param);

    /**
     * 从数据库中导入所有商品到ES
     */
    int importAll();

    /**
     * 根据id删除商品
     */
    void delete(Long id);

}
