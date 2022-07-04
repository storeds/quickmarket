package com.quickmarket.order.component;

import com.quickmarket.common.constant.RedisKeyPrefixConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.lang.Nullable;

import java.nio.charset.StandardCharsets;


@Slf4j
public class RedisChannelListener implements MessageListener {

    @Autowired
    private LocalCache localCache;

    @Override
    public void onMessage(Message message, @Nullable byte[] pattern) {
        log.info("sub message :) channel[cleanNoStockCache] !");
        String productId = new String(message.getBody(), StandardCharsets.UTF_8);
        localCache.remove(RedisKeyPrefixConst.NORMAL_STOCK_CACHE_PREFIX + productId);
    }

}
