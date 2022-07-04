package com.quickmarket.order.feignapi.ums;

import com.quickmarket.common.api.CommonResult;
import com.quickmarket.order.domain.CartPromotionItem;
import com.quickmarket.order.domain.SmsCouponHistoryDetail;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
* @desc: 类的描述:会员优惠卷服务
* @createDate: 2022/1/23 17:00
* @version: 1.0
*/
@FeignClient(name = "quickmall-coupons",path = "/coupon")
public interface UmsCouponFeignApi {

    @RequestMapping(value = "/listCart", method = RequestMethod.POST)
    @ResponseBody
    CommonResult<List<SmsCouponHistoryDetail>> listCart2Feign(@RequestParam("type") Integer type,
                                                              @RequestBody List<CartPromotionItem> cartPromotionItemList);

}
