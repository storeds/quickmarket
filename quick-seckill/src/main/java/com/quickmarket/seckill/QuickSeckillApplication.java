package com.quickmarket.seckill;

import org.apache.shardingsphere.shardingjdbc.spring.boot.SpringBootConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(exclude = {SpringBootConfiguration.class}) /*(exclude = {GlobalTransactionAutoConfiguration.class})*/
//@SpringBootApplication
@EnableFeignClients
@MapperScan(basePackages = { "com.quickmarket.seckill.dao" })
public class QuickSeckillApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuickSeckillApplication.class, args);
    }

}
