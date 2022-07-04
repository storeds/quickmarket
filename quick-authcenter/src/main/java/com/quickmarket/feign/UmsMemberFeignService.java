package com.quickmarket.feign;

import com.quickmarket.common.api.CommonResult;
import com.quickmarket.mbg.model.UmsMember;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @program: quickmarket
 * @author: cx
 * @create: 2022-02-16 16:33
 * @description:
 **/

@FeignClient(value = "quickmall-member", path = "/member/center")
public interface UmsMemberFeignService {

    @RequestMapping("/loadUmsMember")
    CommonResult<UmsMember> loadUserByUsername(@RequestParam("username") String username);

}
