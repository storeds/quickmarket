package com.quickmarket.communicat.netty;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.quickmarket.communicat.model.SocketMessage;
import com.quickmarket.communicat.reposity.UserGroupRepository;
import com.quickmarket.communicat.util.SpringUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: quickmarket
 * @author: cx
 * @create: 2022-03-12 10:28
 * @description:
 **/
@Slf4j
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {


    /** 存储已经登录的用户 **/
    public static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /** 存储用户id和用户的channelId绑定 **/
    public static ConcurrentHashMap<Integer, ChannelId> userMap = new ConcurrentHashMap<>();

    /** 用于存储群聊房间号和群聊成员的channel信息 **/
    public static ConcurrentHashMap<Integer, ChannelGroup> groupMap = new ConcurrentHashMap<>();

    /** 获取用户拥有的群聊id号 **/
//    UserGroupRepository userGroupRepositor = SpringUtil.getBean(UserGroupRepository.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("与客户端建立连接，通道开启！");
        channelGroup.add(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("与客户端断开连接，通道关闭！");
        // 去除channel
        channelGroup.remove(ctx.channel());
    }

    /**
     * 读取消息并处理
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        // 首次连接是FullHttpRequest，把用户id和对应的channel对象存储起来
        if (null != msg && msg instanceof FullHttpRequest) {
            // 取出信息并存储到缓存中
            FullHttpRequest request = (FullHttpRequest) msg;
            String uri = request.uri();
            Integer userId = getUrlParams(uri);

            // 存入缓存
            userMap.put(getUrlParams(uri), ctx.channel().id());
            log.info("登录的用户id是：{}", userId);

            // 第一次登录查看是不是已经加入过群信息
//            List<Integer> groupIds = userGroupRepositor.findGroupIdByUserId(userId);
            ChannelGroup cGroup = null;

            // TODO 查询用户拥有的组是否已经创建了

            // 处理请求中的参数
            if (uri.contains("?")) {
                String newUri = uri.substring(0, uri.indexOf("?"));
                request.setUri(newUri);
            }
        } else if (msg instanceof TextWebSocketFrame) {

            // 获取text消息的类型，转化获取的消息
            TextWebSocketFrame frame = (TextWebSocketFrame) msg;
            SocketMessage socketMessage = JSON.parseObject(frame.text(), SocketMessage.class);
            log.info("客户端收到服务器数据：{}", frame.text());

            // 处理群聊的任务
            if ("group".equals(socketMessage.getMessageType())) {
                // TODO 推送群聊的消息
            }else {
                // 推送消息
                ChannelId channelId = userMap.get(socketMessage.getChatId());
                if (channelId != null) {
                    Channel ct = channelGroup.find(channelId);
                    if (ct != null) {
                        ct.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(socketMessage)));
                    }
                }
            }
        }else {
//            // 获取请求中的ip地址
//            log.error("出现非法请求");
//            throw new RuntimeException("服务接收的信息出现了异常，出现了非法的请求信息");
        }
        super.channelRead(ctx, msg);
    }

    private static Integer getUrlParams(String url) {
        if (!url.contains("=")) {
            return null;
        }
        String userId = url.substring(url.indexOf("=") + 1);
        return Integer.parseInt(userId);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {

    }
}
