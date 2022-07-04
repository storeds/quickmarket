package com.quickmarket.portal;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@MapperScan(basePackages = {"com.quickmarket.portal.dao"})
public class QuickPortalApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuickPortalApplication.class, args);
    }

}
