package com.jumunhasyeo.common.config;

import com.jumunhasyeo.hub.hub.application.*;
import com.jumunhasyeo.hub.hub.domain.repository.HubRepository;
import com.jumunhasyeo.hub.hub.domain.repository.HubRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * HubService 빈 설정 (고정 모드)
 * 
 * cache.dynamic=false 일 때만 동작
 * cache.config.hubService 값에 따라 단일 구현체 등록
 */
@Configuration
@Slf4j
@ConditionalOnProperty(name = "cache.dynamic", havingValue = "false", matchIfMissing = true)
public class HubServiceConfig {

    @Bean
    @ConditionalOnProperty(name = "cache.config.hubService", havingValue = "CAFFEINE")
    public HubService hubServiceCaffeine(
            HubRepository hubRepository,
            HubRepositoryCustom hubRepositoryCustom,
            HubEventPublisher hubEventPublisher
    ) {
        log.info("[FixedCache] Creating HubService with Caffeine");
        HubServiceImpl impl = new HubServiceImpl(hubRepository, hubRepositoryCustom, hubEventPublisher);
        return new HubCaffeineCachedDecoratorService(impl);
    }

    @Bean
    @ConditionalOnProperty(name = "cache.config.hubService", havingValue = "REDIS")
    public HubService hubServiceRedis(
            HubRepository hubRepository,
            HubRepositoryCustom hubRepositoryCustom,
            HubEventPublisher hubEventPublisher
    ) {
        log.info("[FixedCache] Creating HubService with Redis");
        HubServiceImpl impl = new HubServiceImpl(hubRepository, hubRepositoryCustom, hubEventPublisher);
        return new HubRedisCachedDecoratorService(impl);
    }

    @Bean
    @ConditionalOnProperty(name = "cache.config.hubService", havingValue = "NONE")
    public HubService hubServiceNone(
            HubRepository hubRepository,
            HubRepositoryCustom hubRepositoryCustom,
            HubEventPublisher hubEventPublisher
    ) {
        log.info("[FixedCache] Creating HubService without cache");
        return new HubServiceImpl(hubRepository, hubRepositoryCustom, hubEventPublisher);
    }

    @Bean
    @ConditionalOnMissingBean(HubService.class)
    public HubService hubServiceDefault(
            HubRepository hubRepository,
            HubRepositoryCustom hubRepositoryCustom,
            HubEventPublisher hubEventPublisher
    ) {
        log.warn("[FixedCache] Fallback - Creating HubService with Caffeine");
        HubServiceImpl impl = new HubServiceImpl(hubRepository, hubRepositoryCustom, hubEventPublisher);
        return new HubCaffeineCachedDecoratorService(impl);
    }
}
