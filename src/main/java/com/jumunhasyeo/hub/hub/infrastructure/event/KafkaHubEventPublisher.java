package com.jumunhasyeo.hub.hub.infrastructure.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jumunhasyeo.hub.hub.domain.event.HubDomainEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@Primary
@RequiredArgsConstructor
public class KafkaHubEventPublisher {
    private final KafkaTemplate<String, String> template;
    private final ObjectMapper objectMapper;

    @Value("${spring.kafka.topics.hub}")
    private String hubTopic;

    public CompletableFuture<SendResult<String, String>> publishEvent(HubDomainEvent event) {
        try {
            return template.send(hubTopic,  objectMapper.writeValueAsString(event));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
