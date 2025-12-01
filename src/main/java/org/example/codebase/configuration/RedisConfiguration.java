package org.example.codebase.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisConfiguration {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(100);                         // Tổng connection tối đa
        poolConfig.setMaxIdle(50);                           // Số connection idle cao nhất
        poolConfig.setMinIdle(10);                           // Số connection idle thấp nhất
        poolConfig.setMaxWaitMillis(3000L);                  // Thời gian chờ lấy connection nếu hết pool
        poolConfig.setTestOnBorrow(true);                    // Kiểm tra connection trước khi lấy
        poolConfig.setTestOnReturn(true);                    // Kiểm tra khi trả về pool
        poolConfig.setTestWhileIdle(true);                   // Kiểm tra connection idle
        poolConfig.setMinEvictableIdleTimeMillis(60000L);

        return poolConfig;
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory(JedisPoolConfig jedisPoolConfig) {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setDatabase(Protocol.DEFAULT_DATABASE);
        JedisClientConfiguration.JedisClientConfigurationBuilder jedisClientConfigurationBuilder = JedisClientConfiguration.builder();
        jedisClientConfigurationBuilder.connectTimeout(Duration.ofMillis(Protocol.DEFAULT_TIMEOUT));
        jedisClientConfigurationBuilder.connectTimeout(Duration.ofSeconds(5));
        jedisClientConfigurationBuilder.readTimeout(Duration.ofSeconds(5));
        jedisClientConfigurationBuilder.usePooling().poolConfig(jedisPoolConfig); // <- THÊM POOL VÀO ĐÂY

        return new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfigurationBuilder.build());
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate() {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory(jedisPoolConfig()));
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }
}
