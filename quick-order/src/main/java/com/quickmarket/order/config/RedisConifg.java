package com.quickmarket.order.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quickmarket.order.component.RedisChannelListener;
import com.quickmarket.order.component.WebSocketListen;
import com.quickmarket.order.util.RedisOpsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


@Slf4j
@Configuration
public class RedisConifg {

    @Autowired
    private RedisConnectionFactory connectionFactory;

    @Bean
    @Primary
    public RedisTemplate<String,Object> redisTemplate(){
        RedisTemplate<String,Object> template = new RedisTemplate();
        template.setConnectionFactory(connectionFactory);
        // 序列化工具
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringRedisSerializer);
        template.setValueSerializer(jackson2JsonRedisSerializer);

        template.setHashKeySerializer(jackson2JsonRedisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);

        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public RedisOpsUtil redisOpsUtil(){
        return new RedisOpsUtil();
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(){
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(messageListenerAdapter(),channelTopic());
        return container;
    }

    @Bean
    public TaskExecutor executor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(Runtime.getRuntime().availableProcessors());
        executor.setMaxPoolSize(Runtime.getRuntime().availableProcessors() * 2);
        executor.setQueueCapacity(100);
        executor.initialize();
        return executor;
    }

    @Bean
    MessageListenerAdapter messageListenerAdapter(){
        return new MessageListenerAdapter(redisChannelListener());
    }

    @Bean
    RedisChannelListener redisChannelListener(){
        return new RedisChannelListener();
    }

    @Bean
    ChannelTopic channelTopic(){
        return new ChannelTopic("cleanNoStockCache");
    }


//    @Bean
//    ChannelTopic channelTopicByWeb(){
//        return new ChannelTopic("ordersucess");
//    }
//
//    @Bean
//    WebSocketListen webSocketListen(){
//        return new WebSocketListen();
//    }
//
//    @Bean
//    MessageListenerAdapter webSocketmessageListenerAdapter(){
//        return new MessageListenerAdapter(webSocketListen());
//    }


    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory, MessageListenerAdapter adapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        //可以添加多个 messageListener
        container.addMessageListener(adapter,new PatternTopic("ordersucess"));
        return container;
    }

    /*
     * 消息监听器适配器，绑定消息处理器，利用反射技术调用消息处理器的业务方法
     * 将MessageReceiver注册为一个消息监听器，可以自定义消息接收的方法（handleMessage）
     * 如果不指定消息接收的方法，消息监听器会默认的寻找MessageReceiver中的onMessage这个方法作为消息接收的方法
     */
    @Bean
    public MessageListenerAdapter adapter(WebSocketListen messageReceiver) {
        return new MessageListenerAdapter(messageReceiver, "onMessage");
    }


}
