package com.jumunhasyeo.common.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@EnableCaching
public class CaffeineCacheConfig {
    /**
     * /actuator/metrics/cache.gets
     * /actuator/metrics/cache.puts
     * /actuator/metrics/cache.evictions
     * /actuator/metrics/cache.gets?tag=cache:hub&tag=result:hit
     * /actuator/metrics/cache.gets?tag=cache:hub&tag=result:miss
     */
    @Bean
    public Caffeine<Object, Object> caffeineConfig() {
        return Caffeine.newBuilder()
                .recordStats()
                .maximumSize(100)
                .expireAfterWrite(Duration.ofHours(1));
    }

    @Bean
    public CacheManager cacheManager(Caffeine<Object, Object> caffeine) {
        return new CaffeineCacheManager("hub") {{
            setCaffeine(caffeine);
        }};
    }
}
