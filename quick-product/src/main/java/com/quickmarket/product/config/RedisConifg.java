package com.quickmarket.product.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quickmarket.common.constant.RedisKeyPrefixConst;
import com.quickmarket.mbg.model.PmsProduct;
import com.quickmarket.product.dao.FlashPromotionProductDao;
import com.quickmarket.product.domain.FlashPromotionParam;
import com.quickmarket.product.util.RedisOpsUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Slf4j
@Configuration
public class RedisConifg implements InitializingBean {

    @Autowired
    private RedisConnectionFactory connectionFactory;

    @Bean
    @Primary
    public RedisTemplate<String,Object> redisTemplate(){
        RedisTemplate<String,Object> template = new RedisTemplate();
        template.setConnectionFactory(connectionFactory);
        // 序列化后会产生java类型说明，如果不需要用“Jackson2JsonRedisSerializer”和“ObjectMapper ”配合效果更好
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringRedisSerializer);
        template.setValueSerializer(jackson2JsonRedisSerializer);

        template.setHashKeySerializer(jackson2JsonRedisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);

        template.afterPropertiesSet();
        return template;
    }




    @Autowired
    private FlashPromotionProductDao flashPromotionProductDao;

    /**
     * 加载所有的商品库存到缓存redis中
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {

//        List<FlashPromotionParam> promotion = flashPromotionProductDao.getFlashPromotion(null);

        List<PmsProduct> productParams = flashPromotionProductDao.getProducts();

//        if (null==promotion){
//            return;
//        }
//        Date now = new Date();
//        //结束时间
//        Date endDate = promotion.get(0).getEndDate();
//        //剩余时间
//        final Long expired = endDate.getTime()-now.getTime();
//
//        // 活动商品库存缓存到redis
//        promotion.get(0).getRelation().stream().forEach((item)->{
//            redisOpsUtil().setIfAbsent(
//                    RedisKeyPrefixConst.MIAOSHA_STOCK_CACHE_PREFIX + item.getProductId()
//                    , item.getFlashPromotionCount()
//                    , expired
//                    , TimeUnit.MILLISECONDS);
//        });

        // 商品库存缓存到redis
        productParams.stream().forEach((item) -> {
            redisOpsUtil().setIfAbsent(RedisKeyPrefixConst.NORMAL_STOCK_CACHE_PREFIX + item.getId()
            ,item.getStock());
        });
    }
    @Bean
    public RedisOpsUtil redisOpsUtil(){
        return new RedisOpsUtil();
    }


    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();
        config.useSingleServer().setAddress("redis://192.168.96.132:6379").setPassword("123456").setDatabase(1);
        return Redisson.create(config);
    }

}
