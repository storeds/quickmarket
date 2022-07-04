package com.quickmarket.admin.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @program: quickmarket
 * @author: cx
 * @create: 2022-03-31 22:17
 * @description:
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpAndDown {

    private Integer publishStatus;
    private Integer num;

}
