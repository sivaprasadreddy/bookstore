package com.sivalabs.bookstore.config;

import static tools.jackson.core.StreamWriteFeature.IGNORE_UNKNOWN;
import static tools.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

import java.time.Duration;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.*;
import org.springframework.security.jackson.SecurityJacksonModules;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;

@Configuration
@EnableCaching
public class CacheConfig implements BeanClassLoaderAware {
    private ClassLoader loader;

    @Override
    public void setBeanClassLoader(@NonNull ClassLoader classLoader) {
        this.loader = classLoader;
    }

    @Bean(name = {"redisSerializer"})
    public RedisSerializer<Object> redisSerializer() {
        return new GenericJacksonJsonRedisSerializer(jsonMapper());
    }

    @Bean
    public CacheManager cacheManager(
            RedisConnectionFactory redisConnectionFactory, RedisSerializer<Object> serializer) {
        var keySerializationPair =
                RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer());
        var valueSerializationPair = RedisSerializationContext.SerializationPair.fromSerializer(serializer);
        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(keySerializationPair)
                .serializeValuesWith(valueSerializationPair)
                .entryTtl(Duration.ofMinutes(1));

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(cacheConfiguration)
                .build();
    }

    @Bean
    @Primary
    public JsonMapper jsonMapper() {
        var typeValidatorBbuilder = BasicPolymorphicTypeValidator.builder()
                .allowIfBaseType(Object.class)
                .allowIfSubTypeIsArray();

        return JsonMapper.builder()
                .addModules(SecurityJacksonModules.getModules(this.loader, typeValidatorBbuilder))
                .enable(IGNORE_UNKNOWN)
                .disable(FAIL_ON_UNKNOWN_PROPERTIES)
                .build();
    }
}
