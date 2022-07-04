package com.quickmarket.search.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis配置类
 * Created on 2019/4/8.
 */
@Configuration
@MapperScan({"com.quickmarket.mapper","com.quickmarket.search.dao"})
public class MyBatisConfig {
}
