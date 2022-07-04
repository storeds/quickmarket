package com.quickmarket.product.controller;

import com.quickmarket.common.api.CommonResult;
import com.quickmarket.product.dao.PortalProductDao;
import com.quickmarket.product.domain.*;
import com.quickmarket.product.service.PmsProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Api(tags = "PortalProductController", description = "商品查询查看")
@RequestMapping("/pms")
public class PortalProductController {

    @Autowired
    private PmsProductService pmsProductService;

    @Autowired
    private PortalProductDao portalProductDao;

    @ApiOperation(value = "根据商品id获取商品详情")
    @RequestMapping(value = "/productInfo/{id}", method = RequestMethod.GET)
    public CommonResult getProductInfo(@PathVariable Long id) {
        PmsProductParam pmsProductParam=pmsProductService.getProductInfoold(id);
        return CommonResult.success(pmsProductParam);
    }

    @ApiOperation(value = "确认商品的订单")
    @RequestMapping(value = "/productInfoOnly/{id}", method = RequestMethod.GET)
    public CommonResult getProductInfoOnly(@PathVariable("id") Long id,
                                           @RequestParam("skuId") Long skuId,
                                           @RequestParam("memberIdLeve") Long memberIdLeve) {
        ProductId productId1 = new ProductId();
        productId1.setProductId(id);
        productId1.setSkuId(skuId);
        productId1.setMemberIdLeve(memberIdLeve);
        PmsProductParam pmsProductParam=pmsProductService.getProductInfoOnly(productId1);
        return CommonResult.success(pmsProductParam);
    }


    @ApiOperation(value = "根据商品Id获取购物车商品的信息")
    @RequestMapping(value = "/cartProduct/{productId}", method = RequestMethod.GET)
    public CommonResult<CartProduct> getCartProduct(@PathVariable("productId") Long productId){
        CartProduct cartProduct = portalProductDao.getCartProduct(productId);
        return CommonResult.success(cartProduct);
    }

    @ApiOperation(value = "根据商品Id获取购物车商品的信息")
    @RequestMapping(value = "/cartProductBySku/{productId}", method = RequestMethod.GET)
    public CommonResult<CartProduct> getCartProductBySku(@PathVariable("productId") Long productId, @RequestParam("skuId") Long skuId){
        CartProduct cartProduct = portalProductDao.getCartProductBySku(productId, skuId);
        return CommonResult.success(cartProduct);
    }


    @ApiOperation(value = "根据商品Ids获取促销商品信息")
    @RequestMapping(value = "/getPromotionProductList", method = RequestMethod.GET)
    public CommonResult<List<PromotionProduct>> getPromotionProductList(@RequestParam("productIds") List<Long> ids){
        List<PromotionProduct> promotionProducts = portalProductDao.getPromotionProductList(ids);
        return CommonResult.success(promotionProducts);
    }

    @ApiOperation("当前秒杀活动场-产品列表#需要做QPS优化")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "flashPromotionId", value = "秒杀活动ID", required = true, paramType = "query", dataType = "integer"),
            @ApiImplicitParam(name = "flashPromotionSessionId", value = "秒杀活动时间段ID", required = true, paramType = "query", dataType = "integer")})
    @GetMapping("/flashPromotion/productList")
    public CommonResult<List<FlashPromotionProduct>> getProduct(
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            //当前秒杀活动主题ID
            @RequestParam(value = "flashPromotionId") Long flashPromotionId,
            //当前秒杀活动场次ID
            @RequestParam(value = "flashPromotionSessionId") Long flashPromotionSessionId){
        return CommonResult.success(pmsProductService.getFlashProductList(pageSize,pageNum,flashPromotionId,flashPromotionSessionId));
    }


    @ApiOperation(value = "获取当前日期所有活动场次#需要做QPS优化",notes = "示例：10:00场,13:00场")
    @GetMapping("/flashPromotion/getSessionTimeList")
    public CommonResult<List<FlashPromotionSessionExt>> getSessionTimeList() {
        return CommonResult.success(pmsProductService.getFlashPromotionSessionList());
    }

    /**
     * 获取首页秒杀商品
     * @return
     */
    @GetMapping("/flashPromotion/getHomeSecKillProductList")
    public CommonResult<List<FlashPromotionProduct>> getHomeSecKillProductList(){
        return CommonResult.success(pmsProductService.getHomeSecKillProductList());
    }

}
