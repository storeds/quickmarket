package com.quickmarket.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 *
* @desc: 类的描述: jwt 证书配置
* @createDate: 2022/1/21 21:56
* @version: 1.0
*/
@Data
@ConfigurationProperties(prefix = "quickmall.jwt")
public class JwtCAProperties {

    /**
     * 证书名称
     */
    private String keyPairName;

    /**
     * 证书别名
     */
    private String keyPairAlias;

    /**
     * 证书私钥
     */
    private String keyPairSecret;

    /**
     * 证书存储密钥
     */
    private String keyPairStoreSecret;



}
