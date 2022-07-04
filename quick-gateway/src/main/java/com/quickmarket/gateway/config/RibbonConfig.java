package com.quickmarket.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

/**
 * @program: quickmarket
 * @author: cx
 * @create: 2022-02-16 19:29
 * @description:
 **/
@Configuration
public class RibbonConfig {


    @Autowired
    private LoadBalancerClient loadBalancer;

    @Bean
    public RestTemplate restTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(
                Collections.singletonList(
                        new LoadBalancerInterceptor(loadBalancer)));

        return restTemplate;
    }


}
