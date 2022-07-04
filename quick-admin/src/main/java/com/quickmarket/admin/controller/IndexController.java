package com.quickmarket.admin.controller;

import com.quickmarket.admin.dao.IndexDao;
import com.quickmarket.admin.vo.IndexVo;
import com.quickmarket.common.api.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: quickmarket
 * @author: cx
 * @create: 2022-03-31 22:37
 * @description:
 **/
@RestController
@Api(tags = "CmsSubjectController", description = "后台首页")
@RequestMapping("/index")
public class IndexController {


    @Autowired
    IndexDao indexDao;

    @RequestMapping(value = "/static/list",method = RequestMethod.GET)
    @ApiOperation(value = "获取首页信息")
    public CommonResult<IndexVo> getIndex(){
        IndexVo indexVo = new IndexVo();
        indexVo.setMothOd(indexDao.getMothOd());
        indexVo.setMothSale(indexDao.getMothSale());
        indexVo.setOrderCount(indexDao.getOrdercount());
        indexVo.setProductall(indexDao.getProductCount());
        indexVo.setTodayAllCount(indexDao.getOdToAndSaleTo());
        indexVo.setUpAndDown(indexDao.getUpAndDown());
        indexVo.setWeekOd(indexDao.getWeekOd());
        indexVo.setRows(indexDao.getShow());
        return  CommonResult.success(indexVo);
    }

}
