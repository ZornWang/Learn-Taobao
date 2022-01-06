package com.zorn.taobao.config;

import com.zorn.taobao.pojo.ResponseData;
import com.zorn.taobao.pojo.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import java.net.UnknownHostException;

@Configuration
public class RedisConfiguration {
    @Bean
    public RedisTemplate<Object, User> redisTemplate(RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
        RedisTemplate<Object, User> template = new RedisTemplate<Object, User>();
        template.setConnectionFactory(redisConnectionFactory);
        Jackson2JsonRedisSerializer<User> serializer = new Jackson2JsonRedisSerializer<User>(User.class);
        template.setDefaultSerializer(serializer);
        return template;
    }

    @Bean
    public RedisTemplate<Object, ResponseData> redisTemplate2(RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
        RedisTemplate<Object, ResponseData> template = new RedisTemplate<Object, ResponseData>();
        template.setConnectionFactory(redisConnectionFactory);
        Jackson2JsonRedisSerializer<ResponseData> serializer = new Jackson2JsonRedisSerializer<ResponseData>(ResponseData.class);
        template.setDefaultSerializer(serializer);
        return template;
    }
}
