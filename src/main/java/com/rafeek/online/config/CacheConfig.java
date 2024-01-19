package com.rafeek.online.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {
    @Bean
    public Cache<String, String> caffeineCache() {
        return Caffeine.newBuilder()
                // Set a fixed time to expire after the last write or access.
                .expireAfterWrite(23, TimeUnit.HOURS)
                // The initial cache size
                .initialCapacity(100)
                // The maximum of cached entries
                .maximumSize(88)
                .build();
    }
}