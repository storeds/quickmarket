package com.quickmarket.communicat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class QuickCommunicatApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuickCommunicatApplication.class, args);
    }

}
