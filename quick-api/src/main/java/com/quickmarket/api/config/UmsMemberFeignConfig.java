package com.quickmarket.api.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;

/**
* @desc: 类的描述:会员中心 Feign 配置类
* @version: 1.0
*/
public class UmsMemberFeignConfig {

    @Bean
    public Logger.Level level() {
        return Logger.Level.FULL;
    }

}
