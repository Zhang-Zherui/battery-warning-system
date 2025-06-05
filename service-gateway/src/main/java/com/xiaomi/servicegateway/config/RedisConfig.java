package com.xiaomi.servicegateway.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiaomi.domain.warning.dto.AlertMessageDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;

@Configuration
public class RedisConfig {

    // 创建 RedisTemplate 的 Bean，用于与 Redis 交互
    @Bean
    public RedisTemplate<String, AlertMessageDTO> alertMessageRedisTemplate(RedisConnectionFactory factory) {
        // 创建一个 RedisTemplate 对象，指定 key 类型为 String，value 类型为 AlertMessageDTO
        RedisTemplate<String, AlertMessageDTO> template = new RedisTemplate<>();

        // 设置连接工厂，用于建立与 Redis 的连接
        template.setConnectionFactory(factory);

        // 使用 StringRedisSerializer 来序列化和反序列化 Redis 中的 key
        StringRedisSerializer stringSerializer = new StringRedisSerializer();

        // 设置 key 和 hash key 的序列化方式为 StringRedisSerializer
        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);

        // 创建 Jackson2JsonRedisSerializer，用于将 AlertMessageDTO 对象序列化成 JSON 格式存储
        Jackson2JsonRedisSerializer<AlertMessageDTO> jacksonSerializer =
                new Jackson2JsonRedisSerializer<>(AlertMessageDTO.class);

        // 配置 ObjectMapper 来控制 JSON 序列化和反序列化的行为
        jacksonSerializer.setObjectMapper(new ObjectMapper());

        // 设置 value 和 hash value 的序列化方式为 Jackson2JsonRedisSerializer
        template.setValueSerializer(jacksonSerializer);
        template.setHashValueSerializer(jacksonSerializer);

        // 初始化 RedisTemplate 的属性
        template.afterPropertiesSet();

        // 返回 RedisTemplate 实例，供 Spring 容器管理
        return template;
    }
}
