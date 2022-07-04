package com.quickmarket.order.service;

import com.quickmarket.common.api.CommonResult;
import com.quickmarket.common.exception.BusinessException;
import com.quickmarket.order.domain.ConfirmOrderResult;
import com.quickmarket.order.domain.MqCancelOrder;
import com.quickmarket.order.domain.OmsOrderDetail;
import com.quickmarket.order.domain.OrderParam;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 前台订单管理Service
 */
public interface OmsPortalOrderService {
    /**
     * 根据用户购物车信息生成确认单信息
     */
    ConfirmOrderResult generateConfirmOrder(List<Long> itemIds, Long memberId) throws BusinessException;

    /**
     * 根据用户的商品id生成订单信息
     */
    CommonResult getConfirmOrder(Long productId, Long skuId, Long memberId) throws BusinessException;

    /**
     * 根据结算信息生成订单
     */
    CommonResult<Map<String,Object>> generateOrderByQuick(OrderParam orderParam, Long memberId) throws BusinessException;


    /**
     * 根据提交信息生成订单
     */
    @Transactional
//    @ShardingTransactionType(TransactionType.XA)
    CommonResult generateOrder(OrderParam orderParam, Long memberId) throws BusinessException;

    /**
     * 订单详情
     * @param orderId
     * @return
     */
    CommonResult getDetailOrder(Long orderId);

    CommonResult getDetailOrder1(Long orderId);

    /**
     * 支付成功后的回调
     */
    @Transactional
    Integer paySuccess(Long orderId, Integer payType);

    /**
     * 自动取消超时订单
     */
    @Transactional
    CommonResult cancelTimeOutOrder();

    /**
     * 取消单个超时订单
     */
    @Transactional
    void cancelOrder(Long orderId, Long memberId);

    /**
     * 删除订单[逻辑删除],只能status为：3->已完成；4->已关闭；5->无效订单，才可以删除
     * ，否则只能先取消订单然后删除。
     * @param orderId
     * @return
     *      受影响的行
     */
    @Transactional
    int deleteOrder(Long orderId);
    /**
     * 发送延迟消息取消订单
     */
    void sendDelayMessageCancelOrder(MqCancelOrder mqCancelOrder);

    /**
     * 查询会员的订单
     * @param pageSize
     * @param pageNum
     * @param memberId
     *      会员ID
     * @param status
     *      订单状态
     * @return
     */
    CommonResult<List<OmsOrderDetail>> findMemberOrderList(Integer pageSize, Integer pageNum, Long memberId, Integer status);
}
