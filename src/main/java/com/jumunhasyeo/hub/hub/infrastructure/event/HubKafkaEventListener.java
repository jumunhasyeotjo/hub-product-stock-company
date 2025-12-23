package com.jumunhasyeo.hub.hub.infrastructure.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jumunhasyeo.common.dynamic.DynamicConfig;
import com.jumunhasyeo.common.util.KafkaUtil;
import com.jumunhasyeo.hub.hub.application.HubCaffeineCachedDecoratorService;
import com.jumunhasyeo.hub.hub.application.HubCaffeineCachedEvictService;
import com.jumunhasyeo.hub.hub.application.HubService;
import com.jumunhasyeo.hub.hub.domain.event.HubCreatedEvent;
import com.jumunhasyeo.hub.hub.domain.event.HubDeletedEvent;
import com.jumunhasyeo.hub.hub.domain.event.HubUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import static com.jumunhasyeo.common.dynamic.HubServiceCacheType.CAFFEINE;
import static com.jumunhasyeo.hub.hub.infrastructure.event.ListenEventRegistry.*;


@Slf4j
@Component
@RequiredArgsConstructor
public class HubKafkaEventListener {

    private final DynamicConfig dynamicConfig;
    private final HubCaffeineCachedEvictService evictService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "${spring.kafka.topics.hub}",
            groupId = "${spring.kafka.consumer.hub}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listen(
            @Payload String event,
            @Header(name = "eventType", required = false) String fullTypeName
    ) {
        try {
            String simpleClassName = KafkaUtil.getClassName(fullTypeName);
            dispatch(event, simpleClassName);
        } catch (Exception e) {
            log.error("Error processing event: {}", e.getMessage(), e);
        }
    }

    public void dispatch(String payload, String simpleClassName) throws JsonProcessingException {
        if (simpleClassName.equals(HUB_CREATED_EVENT.getEventName())) {
            HubCreatedEvent hubCreatedEvent = objectMapper.readValue(payload, HubCreatedEvent.class);
            if (CAFFEINE.name().equals(dynamicConfig.getHubCache())) {
                evictService.evictAll();
            }
        } else if (simpleClassName.equals(HUB_DELETED_EVENT.getEventName())) {
            HubDeletedEvent hubDeletedEvent = objectMapper.readValue(payload, HubDeletedEvent.class);
            if (CAFFEINE.name().equals(dynamicConfig.getHubCache())) {
                evictService.evictEveryThings(hubDeletedEvent.getHubId());
            }
        } else if (simpleClassName.equals(HUB_UPDATE_EVENT.getEventName())) {
            HubUpdatedEvent hubUpdatedEvent = objectMapper.readValue(payload, HubUpdatedEvent.class);
            if (CAFFEINE.name().equals(dynamicConfig.getHubCache())) {
                evictService.evictAll();
            }
        } else {
            log.info("Unhandled event type: {}", simpleClassName);
            log.info("Unhandled event payload: {}", payload);
        }
    }}

