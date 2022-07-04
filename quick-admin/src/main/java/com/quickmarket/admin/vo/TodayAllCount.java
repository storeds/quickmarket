package com.quickmarket.admin.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @program: quickmarket
 * @author: cx
 * @create: 2022-03-31 22:13
 * @description:
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TodayAllCount {

    /**
     * 今日订单总数，今日消费总数
     */
    private Integer orderCount;
    private Integer saleCount;

}
