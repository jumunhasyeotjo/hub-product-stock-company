package com.jumunhasyeo.hub.hub.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class HubCaffeineCachedEvictService {

    private static final String CACHE_NAME = "hub";
    private static final String CACHE_MANAGER_NAME = "caffeineCacheManager";

    @Caching(
            evict = {
                    @CacheEvict(value = "hub", key = "#hubId", beforeInvocation = false, cacheManager = CACHE_MANAGER_NAME),
                    @CacheEvict(value = "hub", key = "'all'", beforeInvocation = false, cacheManager = CACHE_MANAGER_NAME)
            }
    )
    public void evictEveryThings(UUID hubId) {
        log.info("Hub Caffeine Cache evictEveryThings");
    }

    @Caching(evict = {@CacheEvict(value = "hub", key = "'all'", beforeInvocation = false, cacheManager = CACHE_MANAGER_NAME)})
    public void evictAll() {
        log.info("Hub Caffeine Cache all");
    }
}
