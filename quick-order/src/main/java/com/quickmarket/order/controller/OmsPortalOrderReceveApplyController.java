package com.quickmarket.order.controller;

import com.quickmarket.common.api.CommonResult;
import com.quickmarket.order.domain.OmsOrderReturnApplyParam;
import com.quickmarket.order.service.OmsPortalOrderReturnApplyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @program: quickmarket
 * @author: cx
 * @create: 2022-04-02 17:30
 * @description: 接收到订单
 **/
@Controller
@Api(tags = "OmsPortalOrderReturnApplyController", description = "确认收货管理")
@RequestMapping("/order/reciver")
public class OmsPortalOrderReceveApplyController {

    @Autowired
    private OmsPortalOrderReturnApplyService returnApplyService;

    @ApiOperation("确认收货")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult create(@RequestBody OmsOrderReturnApplyParam returnApply) {
        int count = returnApplyService.receive(returnApply);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

}
