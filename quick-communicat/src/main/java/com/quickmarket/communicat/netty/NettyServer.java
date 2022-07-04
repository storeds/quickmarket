package com.quickmarket.communicat.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @program: quickmarket
 * @author: cx
 * @create: 2022-03-12 11:05
 * @description: netty服务初始化
 **/
@Slf4j
public class NettyServer {

    private final int port;

    public NettyServer(int port) {
        this.port = port;
    }

    public void start() throws InterruptedException {

        // 获取工作线程和连接线程
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup group = new NioEventLoopGroup();
        ServerBootstrap sb = new ServerBootstrap();

        // 设置接收的大小和添加连接池
        sb.group(group, bossGroup);
        sb.channel(NioServerSocketChannel.class);
        sb.option(ChannelOption.SO_BACKLOG, 1024);


        try{
        sb.childHandler(new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                //log.info("接收到新的连接{}", socketChannel.localAddress());
                ChannelPipeline cp = socketChannel.pipeline();

                // 添加处理器 websocket使用的协议是http形式的，所以也要添加http编解码器
                cp.addLast(new HttpServerCodec());
                // 使用块的方式写入信息
                cp.addLast(new ChunkedWriteHandler());
                cp.addLast(new HttpObjectAggregator(8192));

                // 添加聊天处理器，和websocket的处理器
                cp.addLast(new WebSocketHandler());
                cp.addLast(new WebSocketServerProtocolHandler("/ws", null, true, 65536 * 10));

            }
        });

            // 服务器的异步创建绑定
            ChannelFuture cf = sb.bind(port).sync();
            log.info(NettyServer.class + " 启动正在监听： " + cf.channel().localAddress());
            // 关闭服务器通道
            cf.channel().closeFuture().sync();
        }catch (Exception e){
            log.error("在服务器端出现的异常{}", e);
        }finally {
            // 释放线程池资源
            ReentrantLock reentrantLock = new ReentrantLock();
            group.shutdownGracefully().sync();
            bossGroup.shutdownGracefully().sync();
        }
    }
}
