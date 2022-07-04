package com.quickmarket.coupons.clientapi;


import com.quickmarket.common.api.CommonResult;
import com.quickmarket.coupons.clientapi.interceptor.config.FeignConfig;
import com.quickmarket.coupons.domain.CartPromotionItem;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


/**
* @desc: 类的描述:远程调用订单中心购物车详细客户端
* @createDate: 2022/1/14 22:17
* @version: 1.0
*/
@FeignClient(name = "quickmarket-order",configuration = FeignConfig.class)
public interface OmsCartItemClientApi {

    @RequestMapping(value = "/cart/list/promotion", method = RequestMethod.GET)
    @ResponseBody
    CommonResult<List<CartPromotionItem>> listPromotionByMemberId();

}
