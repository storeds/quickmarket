package com.quickmarket.product.service.impl;

import com.github.pagehelper.PageHelper;
import com.quickmarket.common.constant.RedisKeyPrefixConst;
import com.quickmarket.product.component.BloomRedisService;
import com.quickmarket.product.component.LocalCache;
import com.quickmarket.product.component.zklock.ZKLock;
import com.quickmarket.product.dao.FlashPromotionProductDao;
import com.quickmarket.product.dao.PortalProductDao;
import com.quickmarket.product.domain.*;
import com.quickmarket.mbg.mapper.SmsFlashPromotionMapper;
import com.quickmarket.mbg.mapper.SmsFlashPromotionSessionMapper;
import com.quickmarket.mbg.model.SmsFlashPromotion;
import com.quickmarket.mbg.model.SmsFlashPromotionExample;
import com.quickmarket.mbg.model.SmsFlashPromotionSession;
import com.quickmarket.mbg.model.SmsFlashPromotionSessionExample;
import com.quickmarket.product.service.PmsProductService;
import com.quickmarket.product.util.DateUtil;
import com.quickmarket.product.util.RedisOpsUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;


@Slf4j
@Service
public class PmsProductServiceImpl implements PmsProductService {

    @Autowired
    private PortalProductDao portalProductDao;

    @Autowired
    private FlashPromotionProductDao flashPromotionProductDao;

    @Autowired
    private SmsFlashPromotionMapper flashPromotionMapper;

    @Autowired
    private SmsFlashPromotionSessionMapper promotionSessionMapper;

    @Autowired
    private RedisOpsUtil redisOpsUtil;

    private Map<String, PmsProductParam> cacheMap = new ConcurrentHashMap<>();

    @Autowired
    private LocalCache cache;

    @Autowired
    RedissonClient redission;

    @Autowired
    BloomRedisService bloomRedisService;


    public PmsProductParam getProductInfoold(Long id){
        return portalProductDao.getProductInfo(id);
    }

    /**
     * 查询商品信息
     * @param id 产品ID
     * @return
     */
    @Override
    public PmsProductParam getProductInfo(Long id) {
        // 如果布隆过滤器中有这个值就从mysql中获取
        if(bloomRedisService.includeByBloomFilter(RedisKeyPrefixConst.MIAOSHA_STOCK_CACHE_PREFIX , id)){
            return portalProductDao.getProductInfo(id);
        }
       return null;
    }

//    /**
//     * 获取商品详情信息
//     *
//     * @param id 产品ID
//     */
//    @Override
//    public PmsProductParam getProductInfo(Long id) {
//        PmsProductParam productInfo = redisOpsUtil.get(RedisKeyPrefixConst.PRODUCT_DETAIL_CACHE + id, PmsProductParam.class);
//        if (null != productInfo) {
//            return productInfo;
//        }
//        RLock lock = redission.getLock(lockPath + id);
//        try {
//            if (lock.tryLock()) {
//                productInfo = portalProductDao.getProductInfo(id);
//                System.out.println("走数据库" + id);
//                if (null == productInfo) {
//                    return null;
//                }
//                List<FlashPromotionParam> promotions = flashPromotionProductDao.getFlashPromotion(id);
//                FlashPromotionParam promotion = promotions.get(0);
//                if (!ObjectUtils.isEmpty(promotion)) {
//                    productInfo.setFlashPromotionCount(promotion.getRelation().get(0).getFlashPromotionCount());
//                    productInfo.setFlashPromotionLimit(promotion.getRelation().get(0).getFlashPromotionLimit());
//                    productInfo.setFlashPromotionPrice(promotion.getRelation().get(0).getFlashPromotionPrice());
//                    productInfo.setFlashPromotionRelationId(promotion.getRelation().get(0).getId());
//                    productInfo.setFlashPromotionEndDate(promotion.getEndDate());
//                    productInfo.setFlashPromotionStartDate(promotion.getStartDate());
//                    productInfo.setFlashPromotionStatus(promotion.getStatus());
//                }
//                redisOpsUtil.set(RedisKeyPrefixConst.PRODUCT_DETAIL_CACHE + id, productInfo, 360, TimeUnit.SECONDS);
//            } else {
//                productInfo = redisOpsUtil.get(RedisKeyPrefixConst.PRODUCT_DETAIL_CACHE + id, PmsProductParam.class);
//            }
//        } finally {
//            if (lock.isLocked()){
//                if (lock.isHeldByCurrentThread()){
//                    lock.unlock();
//                }
//            }
//
//        }
//        return productInfo;
//    }


    /**
     * 直接访问数据库
     * @param id
     * @return
     */
    public PmsProductParam getProductInfo1(Long id) {
        PmsProductParam productInfo = portalProductDao.getProductInfo(id);
        if (null == productInfo) {
            return null;
        }
        List<FlashPromotionParam> promotion = flashPromotionProductDao.getFlashPromotion(id);
        if (!ObjectUtils.isEmpty(promotion)) {
            productInfo.setFlashPromotionCount(promotion.get(0).getRelation().get(0).getFlashPromotionCount());
            productInfo.setFlashPromotionLimit(promotion.get(0).getRelation().get(0).getFlashPromotionLimit());
            productInfo.setFlashPromotionPrice(promotion.get(0).getRelation().get(0).getFlashPromotionPrice());
            productInfo.setFlashPromotionRelationId(promotion.get(0).getRelation().get(0).getId());
            productInfo.setFlashPromotionEndDate(promotion.get(0).getEndDate());
            productInfo.setFlashPromotionStartDate(promotion.get(0).getStartDate());
            productInfo.setFlashPromotionStatus(promotion.get(0).getStatus());
        }
        return productInfo;
    }

    /**
     * 获取商品详情信息  加入redis
     *
     * @param id 产品ID
     */
    public PmsProductParam getProductInfo2(Long id) {
        PmsProductParam productInfo = null;
        //从缓存Redis里找
        productInfo = redisOpsUtil.get(RedisKeyPrefixConst.PRODUCT_DETAIL_CACHE + id, PmsProductParam.class);
        if (null != productInfo) {
            return productInfo;
        }
        productInfo = portalProductDao.getProductInfo(id);
        if (null == productInfo) {
            log.warn("没有查询到商品信息,id:" + id);
            return null;
        }
        checkFlash(id, productInfo);
        redisOpsUtil.set(RedisKeyPrefixConst.PRODUCT_DETAIL_CACHE + id, productInfo, 3600, TimeUnit.SECONDS);
        return productInfo;
    }



    private void checkFlash(Long id, PmsProductParam productInfo) {
        List<FlashPromotionParam> promotion = flashPromotionProductDao.getFlashPromotion(id);
        if (!ObjectUtils.isEmpty(promotion)) {
            productInfo.setFlashPromotionCount(promotion.get(0).getRelation().get(0).getFlashPromotionCount());
            productInfo.setFlashPromotionLimit(promotion.get(0).getRelation().get(0).getFlashPromotionLimit());
            productInfo.setFlashPromotionPrice(promotion.get(0).getRelation().get(0).getFlashPromotionPrice());
            productInfo.setFlashPromotionRelationId(promotion.get(0).getRelation().get(0).getId());
            productInfo.setFlashPromotionEndDate(promotion.get(0).getEndDate());
            productInfo.setFlashPromotionStartDate(promotion.get(0).getStartDate());
            productInfo.setFlashPromotionStatus(promotion.get(0).getStatus());
        }
    }


    /**
     * add by yangguo
     * 获取秒杀商品列表
     *
     * @param flashPromotionId 秒杀活动ID，关联秒杀活动设置
     * @param sessionId        场次活动ID，for example：13:00-14:00场等
     */
    @Override
    public List<FlashPromotionProduct> getFlashProductList(Integer pageSize, Integer pageNum, Long flashPromotionId, Long sessionId) {
        PageHelper.startPage(pageNum, pageSize, "sort desc");
        return flashPromotionProductDao.getFlashProductList(flashPromotionId, sessionId);
    }

    /**
     * 获取当前日期秒杀活动所有场次
     *
     * @return
     */
    @Override
    public List<FlashPromotionSessionExt> getFlashPromotionSessionList() {
        Date now = new Date();
        SmsFlashPromotion promotion = getFlashPromotion(now);
        if (promotion != null) {
            SmsFlashPromotionSessionExample sessionExample = new SmsFlashPromotionSessionExample();
            //获取时间段内的秒杀场次
            sessionExample.createCriteria().andStatusEqualTo(1);//启用状态
            sessionExample.setOrderByClause("start_time asc");
            List<SmsFlashPromotionSession> promotionSessionList = promotionSessionMapper.selectByExample(sessionExample);
            List<FlashPromotionSessionExt> extList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(promotionSessionList)) {
                promotionSessionList.stream().forEach((item) -> {
                    FlashPromotionSessionExt ext = new FlashPromotionSessionExt();
                    BeanUtils.copyProperties(item, ext);
                    ext.setFlashPromotionId(promotion.getId());
                    if (DateUtil.getTime(now).after(DateUtil.getTime(ext.getStartTime()))
                            && DateUtil.getTime(now).before(DateUtil.getTime(ext.getEndTime()))) {
                        //活动进行中
                        ext.setSessionStatus(0);
                    } else if (DateUtil.getTime(now).after(DateUtil.getTime(ext.getEndTime()))) {
                        //活动即将开始
                        ext.setSessionStatus(1);
                    } else if (DateUtil.getTime(now).before(DateUtil.getTime(ext.getStartTime()))) {
                        //活动已结束
                        ext.setSessionStatus(2);
                    }
                    extList.add(ext);
                });
                return extList;
            }
        }
        return null;
    }

    //根据时间获取秒杀活动
    public SmsFlashPromotion getFlashPromotion(Date date) {
        Date currDate = DateUtil.getDate(date);
        SmsFlashPromotionExample example = new SmsFlashPromotionExample();
        example.createCriteria()
                .andStatusEqualTo(1)
                .andStartDateLessThanOrEqualTo(currDate)
                .andEndDateGreaterThanOrEqualTo(currDate);
        List<SmsFlashPromotion> flashPromotionList = flashPromotionMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(flashPromotionList)) {
            return flashPromotionList.get(0);
        }
        return null;
    }

    /**
     * 获取首页的秒杀商品列表
     *
     * @return
     */
    @Override
    public List<FlashPromotionProduct> getHomeSecKillProductList() {
//        PageHelper.startPage(1, 8, "sort desc");
//        List<FlashPromotionParam> flashPromotionParam = flashPromotionProductDao.getFlashPromotion(null);
//        if (flashPromotionParam == null || CollectionUtils.isEmpty(flashPromotionParam.get(0).getRelation())) {
//            return null;
//        }
//        List<Long> promotionIds = new ArrayList<>();
//        flashPromotionParam.get(0).getRelation().stream().forEach(item -> {
//            promotionIds.add(item.getId());
//        });
//        PageHelper.clearPage();
//        return flashPromotionProductDao.getHomePromotionProductList(promotionIds);
        return null;
    }

    @Override
    public CartProduct getCartProduct(Long productId) {
        return portalProductDao.getCartProduct(productId);
    }

    @Override
    public List<PromotionProduct> getPromotionProductList(List<Long> ids) {
        return portalProductDao.getPromotionProductList(ids);
    }

    /**
     * 查找出所有的产品ID
     *
     * @return
     */
    @Override
    public List<Long> getAllProductId() {
        return portalProductDao.getAllProductId();
    }

    /**
     * 确定商品
     */
    @Override
    public PmsProductParam getProductInfoOnly(ProductId productId){
        return portalProductDao.getProductInfoOnly(productId);
    }

    /*
     * zk分布式锁
     */
    @Autowired
    private ZKLock zkLock;
    private String lockPath = "/load_db";

    /**
     * 获取商品详情信息 分布式锁、 本地缓存、redis缓存
     *
     * @param id 产品ID
     */
    @Override
    public PmsProductParam getProductInfo3(Long id) {
        PmsProductParam productInfo = null;
        productInfo = cache.get(RedisKeyPrefixConst.PRODUCT_DETAIL_CACHE + id);
        if (null != productInfo) {
            return productInfo;
        }
        productInfo = redisOpsUtil.get(RedisKeyPrefixConst.PRODUCT_DETAIL_CACHE + id, PmsProductParam.class);
        if (productInfo != null) {
            log.info("get redis productId:" + productInfo);
            cache.setLocalCache(RedisKeyPrefixConst.PRODUCT_DETAIL_CACHE + id, productInfo);
            return productInfo;
        }
        try {
            if (zkLock.lock(lockPath + "_" + id)) {
                productInfo = portalProductDao.getProductInfo(id);
                if (null == productInfo) {
                    return null;
                }
                checkFlash(id, productInfo);
                log.info("set db productId:" + productInfo);
                redisOpsUtil.set(RedisKeyPrefixConst.PRODUCT_DETAIL_CACHE + id, productInfo, 3600, TimeUnit.SECONDS);
                cache.setLocalCache(RedisKeyPrefixConst.PRODUCT_DETAIL_CACHE + id, productInfo);
            } else {
                log.info("get redis2 productId:" + productInfo);
                productInfo = redisOpsUtil.get(RedisKeyPrefixConst.PRODUCT_DETAIL_CACHE + id, PmsProductParam.class);
                if (productInfo != null) {
                    cache.setLocalCache(RedisKeyPrefixConst.PRODUCT_DETAIL_CACHE + id, productInfo);
                }
            }
        } finally {
            log.info("unlock :" + productInfo);
            zkLock.unlock(lockPath + "_" + id);
        }
        return productInfo;
    }


}
