package com.quickmarket.member;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@MapperScan(basePackages = { "com.quickmarket.mbg.mapper"})
@EnableDiscoveryClient
public class QuickMemberApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuickMemberApplication.class, args);
    }

}
