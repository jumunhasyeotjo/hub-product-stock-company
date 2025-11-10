package com.jumunhasyeo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jumunhasyeo.hub.application.HubService;
import com.jumunhasyeo.hub.application.HubServiceImpl;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public HubService hubService(HubServiceImpl hubServiceImpl) {
        return hubServiceImpl;
    }
}