package com.quickmarket.portal.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * MyBatis配置类
 * Created by quick on 2019/4/8.
 */
@Configuration
@EnableTransactionManagement
@MapperScan({"com.quickmarket.mbg.mapper","com.quickmarket.portal.dao"})
public class MyBatisConfig {
}
