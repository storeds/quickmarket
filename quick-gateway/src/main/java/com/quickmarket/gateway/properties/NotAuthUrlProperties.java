package com.quickmarket.gateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;

/**
 * @program: quickmarket
 * @author: cx
 * @create: 2022-02-16 19:05
 * @description:
 **/
@Data
@ConfigurationProperties("quick.gateway")
@Component
public class NotAuthUrlProperties {

    private LinkedHashSet<String> shouldSkipUrls;

}
