package com.quickmarket.search;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = { "com.quickmarket.search.dao" })
public class QuickSearcherApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuickSearcherApplication.class, args);
    }

}
