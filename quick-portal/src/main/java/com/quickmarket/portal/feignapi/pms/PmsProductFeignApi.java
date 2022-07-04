package com.quickmarket.portal.feignapi.pms;

import com.quickmarket.common.api.CommonResult;
import com.quickmarket.portal.domain.FlashPromotionProduct;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@FeignClient(name = "quickmall-product",path = "/pms")
public interface PmsProductFeignApi {
    
    @RequestMapping(value = "/flashPromotion/getHomeSecKillProductList", method = RequestMethod.GET)
    @ResponseBody
    CommonResult<List<FlashPromotionProduct>> getHomeSecKillProductList();
}
