package com.quickmarket.coupons;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@MapperScan(basePackages = { "com.quickmarket.mbg.mapper" ,"com.quickmarket.coupons.dao"})
public class QuickCouponsApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuickCouponsApplication.class, args);
    }

}
