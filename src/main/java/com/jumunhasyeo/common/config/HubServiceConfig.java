package com.jumunhasyeo.common.config;

import com.jumunhasyeo.hub.application.HubCachedDecoratorService;
import com.jumunhasyeo.hub.application.HubService;
import com.jumunhasyeo.hub.application.HubServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@Slf4j
public class HubServiceConfig {

    @Bean
    @Primary
    @ConditionalOnProperty(name = "cache.config.hubService", havingValue = "true", matchIfMissing = true)
    public HubService hubServiceWithCache(HubServiceImpl hubService) {
        log.info("apply HubCachedDecoratorService");
        return new HubCachedDecoratorService(hubService);
    }

    @Bean
    @Primary
    @ConditionalOnProperty(name = "cache.config.hubService", havingValue = "false")
    public HubService hubServiceWithoutCache(HubServiceImpl hubService) {
        log.info("apply HubServiceImpl");
        return hubService;
    }
}