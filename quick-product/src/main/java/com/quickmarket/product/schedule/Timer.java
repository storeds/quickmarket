package com.quickmarket.product.schedule;

import com.quickmarket.common.constant.RedisKeyPrefixConst;
import com.quickmarket.product.component.BloomRedisService;
import com.quickmarket.product.service.PmsProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @program: quickmarket
 * @author: cx
 * @create: 2022-04-03 11:52
 * @description:
 **/
@Component
@Slf4j
public class Timer {

    @Autowired
    private PmsProductService productService;

    @Autowired
    private RedisTemplate<String,Object> template;

    @Autowired
    private BloomRedisService bloomRedisService;

    @Scheduled(cron = "0/8 * * * * ?")
    public void getDataToRedis(){
        List<Long> list = productService.getAllProductId();
        log.info("加载产品到布隆过滤器当中,size:{}",list.size());
        if(!CollectionUtils.isEmpty(list)){
            list.stream().forEach(item->{
                bloomRedisService.setRedisTemplate(template);
                bloomRedisService.addByBloomFilter(RedisKeyPrefixConst.PRODUCT_REDIS_BLOOM_FILTER,item+"");
            });
        }
    }

}
