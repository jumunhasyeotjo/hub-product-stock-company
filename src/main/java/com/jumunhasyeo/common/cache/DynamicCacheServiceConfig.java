package com.jumunhasyeo.common.cache;

import com.jumunhasyeo.hub.hub.application.*;
import com.jumunhasyeo.hub.hub.domain.repository.HubRepository;
import com.jumunhasyeo.hub.hub.domain.repository.HubRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 동적 캐시 모드용 빈 설정
 * 
 * cache.dynamic=true 일 때 모든 구현체를 빈으로 등록
 */
@Slf4j
@Configuration
@ConditionalOnProperty(name = "cache.dynamic", havingValue = "true")
public class DynamicCacheServiceConfig {
    
    @Bean
    public HubServiceImpl hubServiceImpl(
            HubRepository hubRepository,
            HubRepositoryCustom hubRepositoryCustom,
            HubEventPublisher hubEventPublisher
    ) {
        log.info("[DynamicCache] Creating HubServiceImpl");
        return new HubServiceImpl(hubRepository, hubRepositoryCustom, hubEventPublisher);
    }
    
    @Bean
    public HubCaffeineCachedDecoratorService hubCaffeineCached(HubServiceImpl hubServiceImpl) {
        log.info("[DynamicCache] Creating HubCaffeineCachedDecoratorService");
        return new HubCaffeineCachedDecoratorService(hubServiceImpl);
    }
    
    @Bean
    public HubRedisCachedDecoratorService hubRedisCached(HubServiceImpl hubServiceImpl) {
        log.info("[DynamicCache] Creating HubRedisCachedDecoratorService");
        return new HubRedisCachedDecoratorService(hubServiceImpl);
    }
}
