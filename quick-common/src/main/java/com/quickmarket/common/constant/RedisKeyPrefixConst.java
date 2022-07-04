package com.quickmarket.common.constant;


public interface RedisKeyPrefixConst {
    /**
     * 产品详情内容缓存前缀1
     */
    String PRODUCT_DETAIL_CACHE = "product:detail:cache:";

    /**
     * 产品库存缓存1
     */
    String MIAOSHA_STOCK_CACHE_PREFIX = "miaosha:stock:cache:";

    /**
     * 已购买过商品的用户不能再次购买
     */
    String MEMBER_BUYED_MIAOSHA_PREFIX = "buyed:miaosha:";

    /**
     * 秒杀用验证码限流
     */
    String MIAOSHA_VERIFY_CODE_PREFIX =  "miaosha:verifycode:";

    /**
     * 秒杀token
     */
    String MIAOSHA_TOKEN_PREFIX = "miaosha:token:";

    /**
     * 秒杀异步下单排队中1
     * 0->排队中,-1->秒杀失败,>0 ->秒杀成功,对应的是订单编号
     */
    String MIAOSHA_ASYNC_WAITING_PREFIX = "miaosha:async:waiting:";

    /**
     * 当前正在进行的秒杀商品缓存hash - key1
     */
    String FLASH_PROMOTION_PRODUCT_KEY = "flash:promotion:hashtable";

    /**
     * 存储在redis的hashtable当中秒杀活动信息1
     */
    String ACTIVE_FLASH_PROMOTION_KEY = "flash:promotion:info";

    /**
     * 当库存减到0时,需要做一次库存同步,存在预减1
     */
    String STOCK_REFRESHED_MESSAGE_PREFIX = "stock:refreshed:message:";

    /**
     * redis布隆过滤器key
     */
    String PRODUCT_REDIS_BLOOM_FILTER = "product:redis:bloom:filter";

    /**
     * 普通的下单的状态
     */
    String NORMAL_ASYNC_WAITING_PREFIX = "normal:async:waiting:";

    /**
     * 将商品存放到redis中
     */
    String PROMOTION_PRODUCT_KEY = "normal:promotion:hashtable";

    /**
     * 将活动信息添加到redis中
     */
    String ACTIVE_PROMOTION_KEY = "normal:promotion:info";

    /**
     * 产品库存缓存
     */
    String NORMAL_STOCK_CACHE_PREFIX = "normal:stock:cache:";

    /**
     * 当普通商品库存减为零的情况下，进行处理需要做一次库存同步,存在预减1
     */
    String NORMAL_STOCK_REFRESHED_MESSAGE_PREFIX = "normalstock:refreshed:message:";

}
