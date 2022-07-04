package com.quickmarket.api.clientapi;


import com.quickmarket.api.config.UmsMemberFeignConfig;
import com.quickmarket.common.api.CommonResult;
import com.quickmarket.mbg.model.UmsMember;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
* @desc: 类的描述:Feign远程调用用户服务接口
* @createDate: 2022/1/14 21:44
* @version: 1.0
*/
@FeignClient(name = "quickmall-member",configuration = UmsMemberFeignConfig.class)
public interface UmsMemberClientApi {

    @RequestMapping(value = "/getCurrentMember", method = RequestMethod.GET)
    @ResponseBody
    CommonResult<UmsMember> getCurrentMember();
}
