package com.finance.accounts.config;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

/**
 * Configuration for Redis cache.
 */
@Configuration
@EnableCaching
public class CacheConfig {

  @Value("${spring.cache.redis.time-to-live:86400000}")
  private long timeToLiveMs;

  /**
   * Creates a RedisCacheManager bean.
   *
   * @param connectionFactory the Redis connection factory
   * @return the Redis cache manager
   */
  @Bean
  public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
    RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
        .entryTtl(Duration.ofMillis(timeToLiveMs))
        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
            new GenericJackson2JsonRedisSerializer()));

    return RedisCacheManager.builder(connectionFactory)
        .cacheDefaults(cacheConfig)
        .withCacheConfiguration("accountTypes", 
            cacheConfig.entryTtl(Duration.ofHours(24)))
        .build();
  }
}
