package com.quickmarket.order.service.impl;

import com.quickmarket.common.api.CommonResult;
import com.quickmarket.common.constant.RedisKeyPrefixConst;
import com.quickmarket.common.exception.BusinessException;
import com.quickmarket.order.component.LocalCache;
import com.quickmarket.order.component.rocketmq.OrderMessageSender;
import com.quickmarket.order.dao.MiaoShaStockDao;
import com.quickmarket.order.domain.*;
import com.quickmarket.order.feignapi.pms.PmsProductFeignApi;
import com.quickmarket.order.feignapi.ums.UmsMemberFeignApi;
import com.quickmarket.mbg.mapper.OmsOrderItemMapper;
import com.quickmarket.mbg.mapper.OmsOrderMapper;
import com.quickmarket.mbg.model.OmsOrder;
import com.quickmarket.mbg.model.OmsOrderItem;
import com.quickmarket.mbg.model.UmsMember;
import com.quickmarket.mbg.model.UmsMemberReceiveAddress;
import com.quickmarket.order.service.OrderService;
import com.quickmarket.order.service.TradeService;
import com.quickmarket.order.util.RedisOpsUtil;
import com.quickmarket.order.util.SnowFlakeIDGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;


@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private UmsMemberFeignApi umsMemberFeignApi;
    @Autowired
    private RedisOpsUtil redisOpsUtil;
    @Autowired
    private PmsProductFeignApi pmsProductFeignApi;
    @Autowired
    private OmsOrderMapper orderMapper;
    @Autowired
    private OmsOrderItemMapper orderItemMapper;
    @Autowired
    private LocalCache<Boolean> cache;
    @Autowired
    private MiaoShaStockDao miaoShaStockDao;
    @Value("${redis.key.prefix.orderId}")
    private String REDIS_KEY_PREFIX_ORDER_ID;
    @Autowired
    private TradeService tradeService;
    @Autowired
    private OrderMessageSender orderMessageSender;

    /**
     * 订单确认信息
     */
    @Override
    public CommonResult generateConfirmMiaoOrder(Long productId
            , Long memberId,  Long skuId) throws BusinessException {
        //【1】 进行订单金额确认前的库存与购买权限检查
        CommonResult commonResult = confirmCheck(productId,memberId);
        if(commonResult.getCode() == 500){
            return commonResult;
        }
        // 【2】调用会员服务获取会员信息
        UmsMember member = umsMemberFeignApi.getMemberById().getData();

        ProductId productId1 = new ProductId();
        productId1.setMemberIdLeve(1L);
        productId1.setProductId(productId);
        productId1.setSkuId(skuId);

        // 【3】从产品服务获取产品信息
        PmsProductParam product = getProductInfo(productId1);

        if(product == null){
            return CommonResult.failed("无效的商品！");
        }

        ConfirmOrderResult result = new ConfirmOrderResult();

        //【5】 获取用户收货地址列表
        List<UmsMemberReceiveAddress> memberReceiveAddressList = umsMemberFeignApi.list().getData();
        result.setMemberReceiveAddressList(memberReceiveAddressList);

        //【6】构建商品信息
        List<CartPromotionItem> cartPromotionItemList = new ArrayList<>();
        CartPromotionItem promotionItem = new CartPromotionItem();
        promotionItem.setProductId(product.getId());//产品ID
        promotionItem.setProductName(product.getName());//产品名称
        promotionItem.setMemberId(memberId);//会员ID
        promotionItem.setMemberNickname(member.getNickname());//昵称
        promotionItem.setProductPic(product.getPic());//产品主图
        promotionItem.setProductBrand(product.getBrandName());//品牌
        promotionItem.setQuantity(1);//购买数量
        Integer stock = redisOpsUtil.get(RedisKeyPrefixConst.NORMAL_STOCK_CACHE_PREFIX + productId,Integer.class);
        promotionItem.setRealStock(stock);//库存
        promotionItem.setProductCategoryId(product.getProductCategoryId());//产品类目ID
        promotionItem.setGrowth(product.getGiftGrowth());//送积分
        promotionItem.setIntegration(product.getGiftPoint());//送成长值
        promotionItem.setReduceAmount(product.getPrice());//计算优惠价格
        promotionItem.setPromotionMessage("活动");
        promotionItem.setProductSubTitle(product.getSubTitle());
        promotionItem.setPrice(product.getPrice());
        cartPromotionItemList.add(promotionItem);

        result.setCartPromotionItemList(cartPromotionItemList);
        //【7】 计算订单总金额
        ConfirmOrderResult.CalcAmount calcAmount = calcCartAmount(product);
        result.setCalcAmount(calcAmount);
        //【8】 会员积分
        result.setMemberIntegration(member.getIntegration());
        return CommonResult.success(result);
    }

    /**
     *订单下单
     * @param orderParam
     * @param memberId
     * @return
     */
    @Override
    //@Transactional
    public CommonResult<Map<String,Object>> generateSecOrder(OrderParam orderParam, Long memberId) throws BusinessException {
        Long productId = orderParam.getItemIds().get(0);
        CommonResult commonResult = confirmCheck(productId,memberId);
        if(commonResult.getCode() == 500){
            return commonResult;
        }
        //【4】 调用会员服务获取会员信息
        UmsMember member = umsMemberFeignApi.getMemberById().getData();

        ProductId productId1 = new ProductId();
        productId1.setMemberIdLeve(1L);
        productId1.setProductId(productId);
        productId1.setSkuId(orderParam.getSkuId());
        //【2】 从产品服务获取产品信息
        PmsProductParam product = getProductInfo(productId1);
        //【3】 验证时间是否超时
//        if(!volidateMiaoShaTime(product)){
//            return CommonResult.failed("商品已结束！");
//        }

        //【5】 通过Feign远程调用 会员地址服务
        UmsMemberReceiveAddress address = umsMemberFeignApi.getItem(orderParam.getMemberReceiveAddressId()).getData();

        //预减库存
        if(!preDecrRedisStock(productId, orderParam.getSkuId())) {
            return CommonResult.failed("下单失败,已经抢购完了");
        }

        //准备创建订单
        //生成下单商品信息
        OmsOrderItem orderItem = new OmsOrderItem();
        orderItem.setProductId(product.getId());
        orderItem.setProductName(product.getName());
        orderItem.setProductPic(product.getPic());
        orderItem.setProductBrand(product.getBrandName());
        orderItem.setProductSn(product.getProductSn());
        orderItem.setProductPrice(product.getPrice());
        orderItem.setProductQuantity(1);
        orderItem.setProductCategoryId(product.getProductCategoryId());
        orderItem.setPromotionAmount(product.getPrice());
        orderItem.setPromotionName("感谢下单");
        orderItem.setGiftIntegration(product.getGiftPoint());
        orderItem.setGiftGrowth(product.getGiftGrowth());
        orderItem.setCouponAmount(new BigDecimal(0));
        orderItem.setIntegrationAmount(new BigDecimal(0));
        orderItem.setPromotionAmount(new BigDecimal(0));
        //支付金额
        BigDecimal payAmount = product.getPrice().multiply(new BigDecimal(1));
        //优惠价格
        orderItem.setRealAmount(payAmount);

        OmsOrder order = new OmsOrder();
        order.setDiscountAmount(product.getPrice());//折扣金额
        order.setFreightAmount(new BigDecimal(0));//运费金额
        order.setPromotionAmount(new BigDecimal(0));
        order.setPromotionInfo("普通活动");
        order.setTotalAmount(payAmount);
        order.setIntegration(0);
        order.setIntegrationAmount(new BigDecimal(0));
        order.setPayAmount(payAmount);
        order.setMemberId(memberId);
        order.setMemberUsername(member.getUsername());
        order.setCreateTime(new Date());
        //设置支付方式：0->未支付,1->支付宝,2->微信
        order.setPayType(orderParam.getPayType());
        //设置支付方式：0->PC订单,1->APP订单,2->小程序
        order.setSourceType(0);
        //订单状态：0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单
        order.setStatus(0);
        //订单类型：0->正常订单；1->订单
        order.setOrderType(0);
        //用户收货信息
        order.setReceiverName(address.getName());
        order.setReceiverPhone(address.getPhoneNumber());
        order.setReceiverPostCode(address.getPostCode());
        order.setReceiverProvince(address.getProvince());
        order.setReceiverCity(address.getCity());
        order.setReceiverRegion(address.getRegion());
        order.setReceiverDetailAddress(address.getDetailAddress());
        //0->未确认；1->已确认
        order.setConfirmStatus(0);
        order.setDeleteStatus(0);
        //计算赠送积分
        order.setIntegration(product.getGiftPoint());
        //计算赠送成长值
        order.setGrowth(product.getGiftGrowth());
        //生成订单号-理论上唯一
        order.setOrderSn(generateOrderSn(order));




        /*******************************异步下单******************************************/
        OrderMessage orderMessage = new OrderMessage();
        orderMessage.setOrder(order);
        orderMessage.setOrderItem(orderItem);
        orderMessage.setFlashPromotionRelationId(orderParam.getSkuId());
        Map<String,Object> result = new HashMap<>();
        List<OmsOrderItem> itemList = new ArrayList<>();
        itemList.add(orderItem);
        result.put("order",order);
        result.put("orderItemList",itemList);

        // 使用雪花算法生成唯一的id
        Long uniqueId = SnowFlakeIDGenerator.generateSnowFlakeId();
        orderMessage.setUniqueId(uniqueId);
        try {
            boolean sendStatus = orderMessageSender.sendCreateOrderMsg(orderMessage);
            if(sendStatus){
                /*
                 * 打上排队的标记
                 */
                log.info("异步发送者这里{}", RedisKeyPrefixConst.NORMAL_ASYNC_WAITING_PREFIX + memberId + ":" + productId);
                redisOpsUtil.set(RedisKeyPrefixConst.NORMAL_ASYNC_WAITING_PREFIX + memberId + ":" + productId
                        ,Integer.toString(1),60, TimeUnit.SECONDS);
                /*
                 * 下单方式0->同步下单,1->异步下单排队中,-1->下单失败
                 */
                result.put("orderStatus",1);
            }else{
                /*
                 * 还原预减库存
                 */
                incrRedisStock(productId);
                /*
                 * 清除掉本地guavacache已经售完的标记
                 */
                //cache.remove(RedisKeyPrefixConst.NORMAL_STOCK_CACHE_PREFIX + productId);
                //通知服务群,清除本地售罄标记缓存
                if(shouldPublishCleanMsg(productId)){
                    redisOpsUtil.publish("cleanNoStockCache",productId);
                }
                result.put("orderStatus",-1);
                return CommonResult.failed(result,"下单失败");
            }
        } catch (Exception e) {
            log.error("消息发送失败:error msg:{}",e.getMessage(),e.getCause());
            /*
             * 还原预减库存
             */
            incrRedisStock(productId);
            /*
             * 清除掉本地guavacache已经售完的标记
             */
            //cache.remove(RedisKeyPrefixConst.NORMAL_STOCK_CACHE_PREFIX + productId);
            //通知服务群,清除本地售罄标记缓存
            if(shouldPublishCleanMsg(productId)) {
                redisOpsUtil.publish("cleanNoStockCache", productId);
            }
            result.put("orderStatus",-1);
            return CommonResult.failed(result,"下单失败");
        }
        return CommonResult.success(result,"下单中.....");
    }

    @Transactional
    public Long asyncCreateOrder(OrderMessage orderMessage) {
        OmsOrder order = orderMessage.getOrder();
        OmsOrderItem orderItem = orderMessage.getOrderItem();

        // 获取商品id，获取原来的stock，获取skuid,获取数量
        Long productId = orderItem.getProductId();
        Long skuId = orderMessage.getFlashPromotionRelationId();

        ProductId productId1 = new ProductId();
        productId1.setMemberIdLeve(1L);
        productId1.setProductId(productId);
        productId1.setSkuId(skuId);
        PmsProductParam product = getProductInfo(productId1);
        //减库存
        Integer result = miaoShaStockDao.descNormalStock(productId, product.getSkuStockList().get(0).getStock(),skuId, 1);
        if (result <= 0) {
            throw new RuntimeException("对不起商品没有获取到！");
        }

        //插入订单记录
        orderMapper.insertSelective(orderMessage.getOrder());
        //OrderItem关联
        orderItem.setOrderId(order.getId());
        orderItem.setOrderSn(order.getOrderSn());
        orderItem.setProductSkuId(skuId);
        //插入orderItem
        orderItemMapper.insertSelective(orderItem);
        /*
         * 如果订单创建成功,需要发送定时消息,20min后如果没有支付,则取消当前订单,释放库存
         */
        try {
            boolean sendStatus = orderMessageSender.sendTimeOutOrderMessage(order.getId() + ":" + skuId + ":" + orderItem.getProductId());
            if (!sendStatus) {
                throw new RuntimeException("订单超时取消消息发送失败！");
            }
        } catch (Exception e) {
            throw new RuntimeException("订单超时取消消息发送失败！");
        }
        return order.getId();
    }

    /**
     * Redis预减库存
     */
    private boolean preDecrRedisStock(Long productId,Long skuId) {
        Long stock = redisOpsUtil.decr(RedisKeyPrefixConst.NORMAL_STOCK_CACHE_PREFIX + productId);
        if (stock < 0) {
            /*
             * 还原缓存里的库存，思考这里加回来的目的！
             */
            incrRedisStock(productId);

            if(!redisOpsUtil.hasKey(RedisKeyPrefixConst.NORMAL_STOCK_REFRESHED_MESSAGE_PREFIX + skuId)){
                /*
                 * 这里这么做的目的非常重要：确保不会发生少卖现象
                 * 发延时消息,60s后,同步一次库存; 高并发下可能发送多条延时消息，但是没关系，可以容忍
                 */
                if(orderMessageSender.sendStockSyncMessage(productId,skuId)){
                    redisOpsUtil.set(RedisKeyPrefixConst.NORMAL_STOCK_REFRESHED_MESSAGE_PREFIX + skuId,0);
                }
            }
            return false;
        }
        return true;
    }

    //还原库存
    public void incrRedisStock(Long productId){
        if(redisOpsUtil.hasKey(RedisKeyPrefixConst.NORMAL_STOCK_CACHE_PREFIX + productId)){
            redisOpsUtil.incr(RedisKeyPrefixConst.NORMAL_STOCK_CACHE_PREFIX + productId);
        }
    }

    /*
     * 订单下单前的购买与检查
     */
    private CommonResult confirmCheck(Long productId,Long memberId) throws BusinessException {
        /*1、设置标记，如果售罄了在本地cache中设置为true*/
//        Boolean localcache = cache.getCache(RedisKeyPrefixConst.NORMAL_STOCK_CACHE_PREFIX + productId);
//        if(localcache != null && localcache){
//            return CommonResult.failed("商品已经售罄,请购买其它商品!");
//        }

        /*
         *2、 校验是否有权限购买token
         */
      /*  String redisToken = redisOpsUtil.get(RedisKeyPrefixConst.MIAOSHA_TOKEN_PREFIX + memberId + ":" + productId);
        if(StringUtils.isEmpty(redisToken) || !redisToken.equals(token)){
            return CommonResult.failed("非法请求,token无效!");
        }*/

        String s = RedisKeyPrefixConst.NORMAL_STOCK_CACHE_PREFIX + productId;

        //3、从redis缓存当中取出当前要购买的商品库存
        Integer stock = redisOpsUtil.get(RedisKeyPrefixConst.NORMAL_STOCK_CACHE_PREFIX + productId,Integer.class);

        if(stock == null || stock <= 0){
            /*设置标记，如果售罄了在本地cache中设置为true*/
           // cache.setLocalCache(RedisKeyPrefixConst.NORMAL_STOCK_CACHE_PREFIX + productId,true);
            return CommonResult.failed("商品已经售罄,请购买其它商品!");
        }

        String async = redisOpsUtil.get(RedisKeyPrefixConst.NORMAL_ASYNC_WAITING_PREFIX + memberId + ":" + productId);
        if(async != null && async.equals("1")){
            Map<String,Object> result = new HashMap<>();
            result.put("orderStatus","1");//下单方式0->同步下单,1->异步下单排队中,-1->下单失败,>1->下单成功(返回订单号)
            return CommonResult.failed(result,"异步下单排队中");
        }
        return CommonResult.success(null);
    }

    public boolean shouldPublishCleanMsg(Long productId){
        Integer stock = redisOpsUtil.get(RedisKeyPrefixConst.NORMAL_STOCK_CACHE_PREFIX + productId,Integer.class);
        return (stock == null || stock <= 0);
    }

    /**
     * 获取产品信息,http调用产品服务
     */
    public PmsProductParam getProductInfo(ProductId productId){
        //获取商品信息,判断当前商品是否为下单商品
        CommonResult<PmsProductParam> commonResult = pmsProductFeignApi.getProductInfoOnly(productId.getProductId(),
                productId.getSkuId(),productId.getMemberIdLeve());
        return commonResult.getData();
    }

    /**
     * 计算总金额
     */
    private ConfirmOrderResult.CalcAmount calcCartAmount(PmsProductParam product) {
        ConfirmOrderResult.CalcAmount calcAmount = new ConfirmOrderResult.CalcAmount();
        calcAmount.setFreightAmount(new BigDecimal(0));
        BigDecimal totalAmount = new BigDecimal("0");
        BigDecimal promotionAmount = new BigDecimal("0");
        totalAmount = totalAmount.add(product.getSkuStockList().get(0).getPrice());
        calcAmount.setTotalAmount(totalAmount);
        calcAmount.setPromotionAmount(promotionAmount);
        calcAmount.setPayAmount(totalAmount);
        return calcAmount;
    }

    //验证下单时间
    private boolean volidateMiaoShaTime(PmsProductParam product){
        //当前时间
        Date now = new Date();
        //todo 查看是否有商品,商品库存
        if(product.getFlashPromotionStatus() != 1
                || product.getFlashPromotionEndDate() == null
                || product.getFlashPromotionStartDate() == null
                || now.after(product.getFlashPromotionEndDate())
                || now.before(product.getFlashPromotionStartDate())){
            return false;
        }
        return true;
    }

    /**
     * 生成18位订单编号:8位日期+2位平台号码+2位支付方式+6位以上自增id
     */
    private String generateOrderSn(OmsOrder order) {
        StringBuilder sb = new StringBuilder();
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String key = REDIS_KEY_PREFIX_ORDER_ID + date;
        Long increment = redisOpsUtil.incr(key,1);
        sb.append(date);
        sb.append(String.format("%02d", order.getSourceType()));
        sb.append(String.format("%02d", order.getPayType()));
        String incrementStr = increment.toString();
        if (incrementStr.length() <= 6) {
            sb.append(String.format("%06d", increment));
        } else {
            sb.append(incrementStr);
        }
        return sb.toString();
    }

}