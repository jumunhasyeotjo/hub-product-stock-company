package com.jumunhasyeo;

import com.jumunhasyeo.hub.application.HubEventPublisher;
import com.jumunhasyeo.hub.application.HubService;
import com.jumunhasyeo.hub.application.HubServiceImpl;
import com.jumunhasyeo.hub.domain.repository.HubRepository;
import com.jumunhasyeo.hub.domain.repository.HubRepositoryCustom;
import com.jumunhasyeo.stock.application.StockVariationService;
import com.jumunhasyeo.stock.application.StockVariationServiceImpl;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    public HubService hubServiceNonCached(
            HubRepository hubRepository,
            HubRepositoryCustom hubRepositoryCustom,
            HubEventPublisher hubEventPublisher
    ) {

        return new HubServiceImpl(
                hubRepository,
                hubRepositoryCustom,
                hubEventPublisher
        );
    }

    @Bean
    public StockVariationService StockService(StockVariationServiceImpl stockService) {
        return stockService;
    }
}