package com.jumunhasyeo.common.config;

import com.jumunhasyeo.hub.hub.application.*;
import com.jumunhasyeo.hub.hub.domain.repository.HubRepository;
import com.jumunhasyeo.hub.hub.domain.repository.HubRepositoryCustom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class HubServiceConfig {

    /**
     * 1순위: 캐시 명시적 활성화
     */
    @Bean
    @ConditionalOnProperty(
            name = "cache.config.hubService",
            havingValue = "CAFFEINE"
    )
    public HubService hubServiceCaffeineCached(
            HubRepository hubRepository,
            HubRepositoryCustom hubRepositoryCustom,
            HubEventPublisher hubEventPublisher
    ) {
        log.info("[1] Creating HubService WITH Caffeine Cache");

        HubServiceImpl impl = new HubServiceImpl(
                hubRepository,
                hubRepositoryCustom,
                hubEventPublisher
        );

        return new HubCaffeineCachedDecoratorService(impl);
    }

    /**
     * 1순위: 캐시 명시적 활성화
     */
    @Bean
    @ConditionalOnProperty(
            name = "cache.config.hubService",
            havingValue = "REDIS"
    )
    public HubService hubServiceRedisCached(
            HubRepository hubRepository,
            HubRepositoryCustom hubRepositoryCustom,
            HubEventPublisher hubEventPublisher
    ) {
        log.info("2] Creating HubService WITH Redis Cache");

        HubServiceImpl impl = new HubServiceImpl(
                hubRepository,
                hubRepositoryCustom,
                hubEventPublisher
        );

        return new HubRedisCachedDecoratorService(impl);
    }

    /**
     * 2순위: 캐시 명시적 비활성화
     */
    @Bean
    @ConditionalOnProperty(
            name = "cache.config.hubService",
            havingValue = "NONE"
    )
    public HubService hubServiceNonCached(
            HubRepository hubRepository,
            HubRepositoryCustom hubRepositoryCustom,
            HubEventPublisher hubEventPublisher
    ) {
        log.info("[2] Creating HubService WITHOUT Cache");

        return new HubServiceImpl(
                hubRepository,
                hubRepositoryCustom,
                hubEventPublisher
        );
    }

    /**
     * 3순위: 폴백 (설정이 없거나 이상한 값일 때)
     * 위 두 Bean이 없으면 자동으로 생성
     */
    @Bean
    @ConditionalOnMissingBean(HubService.class)  // HubService Bean이 없을 때만
    public HubService hubServiceDefault(
            HubRepository hubRepository,
            HubRepositoryCustom hubRepositoryCustom,
            HubEventPublisher hubEventPublisher
    ) {
        log.warn("[3] No cache config found, creating DEFAULT HubService WITH Caffeine Cache");

        HubServiceImpl impl = new HubServiceImpl(
                hubRepository,
                hubRepositoryCustom,
                hubEventPublisher
        );

        return new HubCaffeineCachedDecoratorService(impl);
    }
}