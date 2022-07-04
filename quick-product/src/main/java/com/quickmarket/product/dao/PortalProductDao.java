package com.quickmarket.product.dao;


import com.quickmarket.product.domain.CartProduct;
import com.quickmarket.product.domain.PmsProductParam;
import com.quickmarket.product.domain.ProductId;
import com.quickmarket.product.domain.PromotionProduct;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 前台系统自定义商品Dao
 */
public interface PortalProductDao {


    CartProduct getCartProduct(@Param("id") Long id);

    CartProduct getCartProductBySku(@Param("id")Long id, @Param("skuId") Long skuId);

    List<PromotionProduct> getPromotionProductList(@Param("ids") List<Long> ids);

    /**
     * add by yangguo
     * 获取商品详情信息
     * @param id 产品ID
     */
    PmsProductParam getProductInfo(@Param("id") Long id);

    /**
     * 查找出所有的产品ID
     * @return
     */
    List<Long> getAllProductId();

    /**
     * 确认商品
     */
    PmsProductParam getProductInfoOnly(ProductId productId);

}
