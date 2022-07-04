package com.quickmarket.admin.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: quickmarket
 * @author: cx
 * @create: 2022-04-01 01:44
 * @description:
 **/
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Show {
    private Integer orderCount;
    private BigDecimal orderAmount;
    private Date date;
}
