package com.quickmarket.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.quickmarket.common.api.ResultCode;
import com.quickmarket.common.exception.GateWayException;
import com.quickmarket.gateway.properties.NotAuthUrlProperties;
import com.quickmarket.gateway.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import java.security.PublicKey;
import java.util.Map;

/**
 * @program: quickmarket
 * @author: cx
 * @create: 2022-02-16 19:03
 * @description:
 **/
@Slf4j
@Component
@Order(0)
@EnableConfigurationProperties(value = NotAuthUrlProperties.class)
public class AuthenticationFilter implements GlobalFilter, InitializingBean {




    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private NotAuthUrlProperties notAuthUrlProperties;

    void s( HttpServletRequest request){
        String s = request.getHeader("Authorization");
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String currentUrl = exchange.getRequest().getURI().getPath();

        //过滤不需要认证的url
        if(shouldSkip(currentUrl)) {
            //log.info("跳过认证的URL:{}",currentUrl);
            return chain.filter(exchange);
        }
        //log.info("需要认证的URL:{}",currentUrl);
        //第一步:解析出我们Authorization的请求头  value为: “bearer XXXXXXXXXXXXXX”
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        //第二步:判断Authorization的请求头是否为空
        if(StringUtils.isEmpty(authHeader)) {
            log.warn("需要认证的url,请求头为空");
            throw new GateWayException(ResultCode.AUTHORIZATION_HEADER_IS_EMPTY);
        }

        //3. 校验token
        // 拿到token后，通过公钥（需要从授权服务获取公钥）校验
        // 校验失败或超时抛出异常
        //第三步 校验我们的jwt 若jwt不对或者超时都会抛出异常
        // TODO 后续将从reids中获取token
        Claims claims = JwtUtils.validateJwtToken(authHeader,publicKey);

        //4. 校验通过后，从token中获取的用户登录信息存储到请求头中
        // 第四步 把从jwt中解析出来的 用户登陆信息存储到请求头中
        ServerWebExchange webExchange = wrapHeader(exchange,claims);

        return chain.filter(webExchange);
    }


    private static final String AUTH_HEADER = "bearer ";

    public static Claims validateJwtToken(String authHeader, PublicKey publicKey) {
        String token =null ;
        try{
            token = StringUtils.substringAfter(authHeader, AUTH_HEADER);

            Jwt<JwsHeader, Claims> parseClaimsJwt = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token);

            Claims claims = parseClaimsJwt.getBody();

            //log.info("claims:{}",claims);

            return claims;

        }catch(Exception e){

            log.error("校验token异常:{},异常信息:{}",token,e.getMessage());

            throw new GateWayException(ResultCode.JWT_TOKEN_EXPIRE);
        }
    }


    /**
     * 获取请求头并构造
     * @param serverWebExchange
     * @param claims
     * @return
     */
    private ServerWebExchange wrapHeader(ServerWebExchange serverWebExchange,Claims claims) {

        String loginUserInfo = JSON.toJSONString(claims);
        log.info("jwt的用户信息:{}",loginUserInfo);

        String memberId = claims.get("additionalInfo", Map.class).get("memberId").toString();
        String nickName = claims.get("additionalInfo", Map.class).get("nickName").toString();

        //向headers中放文件，记得build
        ServerHttpRequest request = serverWebExchange.getRequest().mutate()
                .header("username",claims.get("user_name",String.class))
                .header("memberId",memberId)
                .header("nickName",nickName)
                .build();

        //将现在的request 变成 change对象
        return serverWebExchange.mutate().request(request).build();
    }

    private boolean shouldSkip(String currentUrl) {
        //路径匹配器(简介SpringMvc拦截器的匹配器)
        //比如/oauth/** 可以匹配/oauth/token    /oauth/check_token等
        PathMatcher pathMatcher = new AntPathMatcher();
        for(String skipPath:notAuthUrlProperties.getShouldSkipUrls()) {
            if(pathMatcher.match(skipPath,currentUrl)) {
                return true;
            }
        }
        return false;
    }


    /**
     * jwt的公钥,需要网关启动,远程调用认证中心去获取公钥
     */
    private PublicKey publicKey;

    /**
     * 获取公钥
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
            // 发一个rpc请求获取公钥
        this.publicKey = JwtUtils.genPulicKey(restTemplate);
    }

}
