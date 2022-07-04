package com.quickmarket.communicat.listen;

import com.quickmarket.communicat.netty.NettyServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @program: quickmarket
 * @author: cx
 * @create: 2022-03-12 11:27
 * @description: 容器启动后初始化netty-websocket服务
 **/
@Component
@Slf4j
public class NettyInitListen implements CommandLineRunner {

    @Value("${netty.port}")
    Integer nettyPort;
    @Value("${server.port}")
    Integer serverPort;

    @Override
    public void run(String... args) throws Exception {
        log.info("服务器启动");
        try {
        new NettyServer(nettyPort).start();
        } catch (Exception e) {
            System.out.println("NettyServer错误:" + e.getMessage());
        }
    }
}
