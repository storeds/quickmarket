package com.quickmarket;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@MapperScan(basePackages = { "com.quickmarket.mbg.mapper" })
public class QuickAuthcenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuickAuthcenterApplication.class, args);
    }

}
