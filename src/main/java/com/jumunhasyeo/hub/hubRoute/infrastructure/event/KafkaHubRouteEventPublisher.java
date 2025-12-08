package com.jumunhasyeo.hub.hubRoute.infrastructure.event;

import com.jumunhasyeo.hub.hubRoute.application.HubRouteEventPublisher;
import com.jumunhasyeo.hub.hubRoute.domain.event.HubRouteCreatedEvent;
import com.jumunhasyeo.hub.hubRoute.domain.event.HubRouteDeletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@Primary
@RequiredArgsConstructor
public class KafkaHubRouteEventPublisher  implements HubRouteEventPublisher {
    private final KafkaTemplate<String, Object> template;

    @Value("${spring.kafka.topics.hub}")
    private String hubTopic;

    @Override
    public void publishRouteCreatedEvent(List<HubRouteCreatedEvent> eventList) {
        try{
            template.send(hubTopic, eventList);
        }catch (Exception e){
            log.error("Failed to publish HubCreatedEvent to Kafka", e);
        }
    }

    @Override
    public void publishRouteDeletedEvent(List<HubRouteDeletedEvent> eventList) {
        try{
            template.send(hubTopic, eventList);
        }catch (Exception e){
            log.error("Failed to publish HubDeletedEvent to Kafka", e);
        }
    }
}
