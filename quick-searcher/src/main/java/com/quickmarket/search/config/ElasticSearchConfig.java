package com.quickmarket.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: quickmarket
 * @author: cx
 * @create: 2022-02-21 22:16
 * @description:
 **/
@Configuration
public class ElasticSearchConfig {

    //http://45.125.57.198/
    @Bean
    public RestHighLevelClient restHighLevelClient(){
        RestHighLevelClient client = new RestHighLevelClient(
                /*RestClient.builder(
                       new HttpHost("192.168.21.130", 9200, "http"),
                        new HttpHost("192.168.21.131", 9200, "http"),
                        new HttpHost("192.168.21.132", 9200, "http")));*/
                RestClient.builder(
                        new HttpHost("192.168.96.130", 9200, "http")));
        return client;
    }
}
