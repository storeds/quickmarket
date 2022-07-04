package com.quickmarket.order.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import com.quickmarket.common.api.CommonPage;
import com.quickmarket.common.api.CommonResult;
import com.quickmarket.mbg.mapper.OmsOrderReturnReasonMapper;
import com.quickmarket.mbg.model.OmsOrderReturnReason;
import com.quickmarket.mbg.model.OmsOrderReturnReasonExample;


import com.quickmarket.order.service.OmsOrderReturnReasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: quickmarket
 * @author: cx
 * @create: 2022-04-02 00:12
 * @description:
 **/
@Service
public class OmsOrderReturnReasonServiceImpl implements OmsOrderReturnReasonService {

    @Autowired
    OmsOrderReturnReasonMapper returnReasonMapper;

    @Override
    public CommonResult<List<OmsOrderReturnReason>> list(Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum,pageSize);
        OmsOrderReturnReasonExample example = new OmsOrderReturnReasonExample();
        List<OmsOrderReturnReason> examples = returnReasonMapper.selectByExample(example);
        PageInfo info = new PageInfo(examples);
        example.setOrderByClause("sort desc");
        return CommonResult.success(examples, "sucess" ,(int)info.getTotal(), info.getPages());
    }
}
