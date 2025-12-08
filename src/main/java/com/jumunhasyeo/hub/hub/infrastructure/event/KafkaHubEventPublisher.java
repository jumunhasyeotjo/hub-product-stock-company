package com.jumunhasyeo.hub.hub.infrastructure.event;

import com.jumunhasyeo.hub.hub.application.HubEventPublisher;
import com.jumunhasyeo.hub.hub.domain.event.HubCreatedEvent;
import com.jumunhasyeo.hub.hub.domain.event.HubDeletedEvent;
import com.jumunhasyeo.hub.hub.domain.event.HubNameUpdateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Primary
@RequiredArgsConstructor
public class KafkaHubEventPublisher implements HubEventPublisher {
    private final KafkaTemplate<String, Object> template;

    @Value("${spring.kafka.topics.hub}")
    private String hubTopic;

    @Override
    public void publishEvent(HubCreatedEvent event) {
        try{
            template.send(hubTopic, event);
        }catch (Exception e){
            log.error("Failed to publish HubCreatedEvent to Kafka", e);
        }
    }

    @Override
    public void publishEvent(HubDeletedEvent event) {
        try{
            template.send(hubTopic, event);
        }catch (Exception e){
            log.error("Failed to publish HubDeletedEvent to Kafka", e);
        }
    }

    @Override
    public void publishEvent(HubNameUpdateEvent event) {
        try{
            template.send(hubTopic, event);
        }catch (Exception e){
            log.error("Failed to publish HubNameUpdateEvent to Kafka", e);
        }
    }
}
