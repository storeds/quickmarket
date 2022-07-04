package com.quickmarket.member.controller;

import com.quickmarket.common.api.CommonResult;
import com.quickmarket.mbg.model.UmsMemberReceiveAddress;
import com.quickmarket.member.service.UmsMemberReceiveAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@Api(tags = "UmsMemberReceiveAddressController", description = "会员收货地址管理")
@RequestMapping("/member/address")
public class UmsMemberReceiveAddressController {
    @Autowired
    private UmsMemberReceiveAddressService memberReceiveAddressService;

    @ApiOperation("添加收货地址")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult add(@RequestBody UmsMemberReceiveAddress address,@RequestHeader("memberId") Long memberId) {
        int count = memberReceiveAddressService.add(address,memberId);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("删除收货地址")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult delete(@PathVariable Long id,@RequestHeader("memberId") Long memberId) {
        int count = memberReceiveAddressService.delete(id,memberId);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("修改收货地址")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult update(@PathVariable Long id, @RequestBody UmsMemberReceiveAddress address,@RequestHeader("memberId")Long memberId) {
        int count = memberReceiveAddressService.update(id, address,memberId);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("显示所有收货地址")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<List<UmsMemberReceiveAddress>> list(@RequestHeader("memberId") long memberId) {
        List<UmsMemberReceiveAddress> addressList = memberReceiveAddressService.list(memberId);
        return CommonResult.success(addressList);
    }

    @ApiOperation("显示收货地址详情")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<UmsMemberReceiveAddress> getItem(@PathVariable Long id,@RequestHeader("memberId") long memberId) {
        UmsMemberReceiveAddress address = memberReceiveAddressService.getItem(id,memberId);
        return CommonResult.success(address);
    }
}
