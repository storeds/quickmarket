package com.quickmarket.admin.dto;

import com.quickmarket.mbg.model.PmsProduct;
import com.quickmarket.mbg.model.SmsFlashPromotionProductRelation;
import lombok.Getter;
import lombok.Setter;

/**
 * 限时购及商品信息封装
 */
public class SmsFlashPromotionProduct extends SmsFlashPromotionProductRelation{
    @Getter
    @Setter
    private PmsProduct product;
}
