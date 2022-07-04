package com.quickmarket.order;

import org.apache.shardingsphere.shardingjdbc.spring.boot.SpringBootConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;

//不使用ShardingSphere。SpringBootConfiguration会往Spring中注入ShardingDataSource
@SpringBootApplication(exclude = {SpringBootConfiguration.class}) /*(exclude = {GlobalTransactionAutoConfiguration.class})*/
//@SpringBootApplication
@EnableFeignClients
@MapperScan(basePackages = { "com.quickmarket.order.dao"})
public class QuickOrderApplication {


    private static ApplicationContext applicationContext;

    public static void main(String[] args) {
        applicationContext = SpringApplication.run(QuickOrderApplication.class, args);
    }

    public static <T> T getBean(String beanName) {
        return applicationContext != null ? (T) applicationContext.getBean(beanName) : null;
    }
}
