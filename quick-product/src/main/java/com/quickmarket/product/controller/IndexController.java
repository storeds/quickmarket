package com.quickmarket.product.controller;


import com.quickmarket.common.api.CommonResult;
import com.quickmarket.product.dao.IndexDao;
import com.quickmarket.product.vo.IndexVo;
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
@Api(tags = "PortalProductController", description = "商品查询查看")
@RequestMapping("/pms")
public class IndexController {


    @Autowired
    IndexDao indexDao;

    @RequestMapping(value = "/static/list",method = RequestMethod.GET)
    @ApiOperation(value = "获取首页信息")
    public CommonResult<IndexVo> getIndex(){
        IndexVo indexVo = new IndexVo();
        indexVo.setMothOd(indexDao.getMothOd());
        indexVo.setMothSale(indexDao.getMothSale() == null ? 0 : indexDao.getMothSale());
        indexVo.setOrderCount(indexDao.getOrdercount());
        indexVo.setProductall(indexDao.getProductCount());
        indexVo.setTodayAllCount(indexDao.getOdToAndSaleTo());
        indexVo.setUpAndDown(indexDao.getUpAndDown());
        indexVo.setWeekOd(indexDao.getWeekOd());
        indexVo.setWeekSale(indexDao.getWeekSale() == null ? 0 : indexDao.getWeekSale());
        return  CommonResult.success(indexVo);
    }

}
