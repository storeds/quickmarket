package com.quickmarket.order.feignapi.ums;

import com.quickmarket.common.api.CommonResult;
import com.quickmarket.mbg.model.UmsMemberReceiveAddress;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

///**
// * @program: quickmarket
// * @author: cx
// * @create: 2022-03-20 16:48
// * @description: 远程调用sku的信息
// **/
//@FeignClient(name = "quickmall-product",path = "/sku")
//public interface UmsSkuStockFeignApi {
//
//    @RequestMapping(value = "/getPmsSkuStock", method = RequestMethod.POST)
//    @ResponseBody
//    CommonResult<UmsMemberReceiveAddress> getPmsSkuStock(Long id, Long skuId, Long memberId);
//
//}
