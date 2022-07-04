package com.quickmarket.admin.dao;

import com.quickmarket.admin.vo.OrderCount;
import com.quickmarket.admin.vo.Show;
import com.quickmarket.admin.vo.TodayAllCount;
import com.quickmarket.admin.vo.UpAndDown;

import java.util.List;

/**
 * @program: quickmarket
 * @author: cx
 * @create: 2022-03-31 22:03
 * @description:
 **/
public interface IndexDao {

    /**
     * 展示今日订单总数， 今日销售总数
     */
    TodayAllCount getOdToAndSaleTo();

    /**
     * 展示有各个订单的数量
     */
    List<OrderCount> getOrdercount();

    /**
     * 全部商品
     */
    Integer getProductCount();

    /**
     * 已经下架和上架的商品
     */
    List<UpAndDown> getUpAndDown();


    /**
     * 订单数本月
     */
    Integer getMothOd();

    /**
     * 订单数本周
     */
    Integer getWeekOd();

    /**
     * 销售额本月
     */
    Integer getMothSale();

    /**
     * 销售额本周
     */
     Integer getWeekSale();

     List<Show> getShow();
}
