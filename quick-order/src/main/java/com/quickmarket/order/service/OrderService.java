package com.quickmarket.order.service;

import com.quickmarket.common.api.CommonResult;
import com.quickmarket.common.exception.BusinessException;
import com.quickmarket.order.domain.OrderMessage;
import com.quickmarket.order.domain.OrderParam;
import com.quickmarket.order.domain.PmsProductParam;
import com.quickmarket.mbg.model.OmsOrder;
import com.quickmarket.mbg.model.OmsOrderItem;
import com.quickmarket.order.domain.ProductId;


public interface OrderService {

//    /**
//     * 订单确认页
//     * @param productId
//     * @param memberId
//     * @return
//     */
//    CommonResult generateConfirmMiaoOrder(Long productId
//            , Long memberId, String token, Long skuId) throws BusinessException;

    /**
     * 订单确认页
     * @param productId
     * @param memberId
     * @return
     */
    CommonResult generateConfirmMiaoOrder(Long productId
            , Long memberId,  Long skuId) throws BusinessException;

    /**
     *订单下单页
     * @param orderParam
     * @param memberId
     * @return
     */
    CommonResult generateSecOrder(OrderParam orderParam, Long memberId) throws BusinessException;

    /**
     * 还原redis库存,每次加1
     * @param productId
     */
    void incrRedisStock(Long productId);

    /**
     * 判断是否应该pub消息清除集群服务本地的售罄标识
     * @param productId
     * @return
     */
    boolean shouldPublishCleanMsg(Long productId);

    /**
     * 异步下单
     * @return
     */
    Long asyncCreateOrder(OrderMessage orderMessage);

    /**
     * 获取产品信息
     * @param productId
     * @return
     */
    public PmsProductParam getProductInfo(ProductId productId);

}
