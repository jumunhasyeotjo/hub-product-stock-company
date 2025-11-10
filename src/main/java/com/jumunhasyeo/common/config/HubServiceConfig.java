package com.jumunhasyeo.common.config;

import com.jumunhasyeo.hub.application.HubCachedProxyService;
import com.jumunhasyeo.hub.application.HubNonCachedProxyService;
import com.jumunhasyeo.hub.application.HubService;
import com.jumunhasyeo.hub.application.HubServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class HubServiceConfig {

    @Bean
    @ConditionalOnProperty(name = "cache.config.hubService", havingValue = "true")
    public HubService hubServiceWithCache(HubServiceImpl hubService) {
        return new HubCachedProxyService(hubService);
    }

    @Bean
    @ConditionalOnProperty(name = "cache.config.hubService", havingValue = "false", matchIfMissing = true)
    public HubService hubServiceWithoutCache(HubServiceImpl hubService) {
        return new HubNonCachedProxyService(hubService);
    }
}

