package com.quickmarket.seckill.feignapi.ums;

import com.quickmarket.common.api.CommonResult;
import com.quickmarket.seckill.domain.PortalMemberInfo;
import com.quickmarket.mbg.model.UmsMember;
import com.quickmarket.mbg.model.UmsMemberReceiveAddress;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
* @desc: 类的描述:远程调用 会员中心获取具体收获地址
* @createDate: 2022/1/23 15:01
* @version: 1.0
*/
@FeignClient(name = "quickmall-member",path = "/member")
public interface UmsMemberFeignApi {

    @RequestMapping(value = "/address/{id}", method = RequestMethod.GET)
    @ResponseBody
    CommonResult<UmsMemberReceiveAddress> getItem(@PathVariable(value = "id") Long id);

    @RequestMapping(value = "/center/updateUmsMember",method = RequestMethod.POST)
    CommonResult<String> updateUmsMember(@RequestBody UmsMember umsMember);


    @RequestMapping(value = "/center/getMemberInfo", method = RequestMethod.GET)
    @ResponseBody
    CommonResult<PortalMemberInfo> getMemberById();

    @RequestMapping(value = "/address/list", method = RequestMethod.GET)
    @ResponseBody
    CommonResult<List<UmsMemberReceiveAddress>> list();
}
