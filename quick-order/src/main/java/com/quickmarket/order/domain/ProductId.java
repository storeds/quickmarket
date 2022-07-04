package com.quickmarket.order.domain;

import lombok.*;

/**
 * @program: quickmarket
 * @author: cx
 * @create: 2022-03-27 14:47
 * @description:
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductId {
    private Long productId;
    private Long skuId;
    private Long memberIdLeve;
}
