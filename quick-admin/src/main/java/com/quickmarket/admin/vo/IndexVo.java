package com.quickmarket.admin.vo;

import io.swagger.models.auth.In;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * @program: quickmarket
 * @author: cx
 * @create: 2022-03-31 22:31
 * @description:
 **/
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class IndexVo {
    /**
     * 全部商品
     */
    private Integer productall;

    /**
     * 订单数本月
     */
    private Integer mothOd;

    /**
     * 订单数本周
     */
    private Integer weekOd;

    /**
     * 销售额本月
     */
    private Integer mothSale;

    /**
     * 销售额本周
     */
    private Integer weekSale;

    /**
     * 展示有各个订单的数量
     */
    private List<OrderCount> orderCount;

    /**
     * 展示今日订单总数， 今日销售总数
     */
    private TodayAllCount todayAllCount;

    /**
     * 已经下架和上架的商品
     */
    private List<UpAndDown> upAndDown;


    private List<Show> rows;


}
