package com.jumunhasyeo;

import com.jumunhasyeo.stock.application.StockVariationService;
import com.jumunhasyeo.stock.application.StockVariationServiceImpl;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@TestConfiguration
@EnableFeignClients
public class InternalIntegrationTestConfig {

    @Bean
    public StockVariationService StockService(StockVariationServiceImpl stockService) {
        return stockService;
    }
}