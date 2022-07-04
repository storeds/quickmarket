package com.quickmarket.search.controller;

import com.quickmarket.search.service.QuickMallSearchService;
import com.quickmarket.search.util.CommonResult;
import com.quickmarket.search.vo.ESRequestParam;
import com.quickmarket.search.vo.ESResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @program: quickmarket
 * @author: cx
 * @create: 2022-02-21 22:32
 * @description:
 **/
@RestController("/es")
public class QuickMallSearchController {

    @Autowired
    private QuickMallSearchService quickMallSearchService;

    /**
     * 自动将页面提交过来的所有请求参数封装成我们指定的对象
     *
     * @param param
     * @return
     */

    @ResponseBody
    @RequestMapping(value = "/searchList")
    public CommonResult<ESResponseResult> listPage(@RequestBody ESRequestParam param, HttpServletRequest request) {

        //1、根据传递来的页面的查询参数，去es中检索商品
        ESResponseResult searchResult = quickMallSearchService.search(param);

        return CommonResult.success(searchResult);
    }


}
