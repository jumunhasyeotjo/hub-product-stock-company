package com.jumunhasyeo.hub.hub.infrastructure.event;

import com.jumunhasyeo.hub.hub.application.HubEventPublisher;
import com.jumunhasyeo.hub.hub.domain.event.HubCreatedEvent;
import com.jumunhasyeo.hub.hub.domain.event.HubDeletedEvent;
import com.jumunhasyeo.hub.hub.domain.event.HubNameUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class SpringHubEventPublisher implements HubEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publishEvent(HubCreatedEvent event) {
        log.info("[HubCreatedEvent] Publish - 허브가 생성되었습니다.");
        applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publishEvent(HubDeletedEvent event) {
        log.info("[HubDeletedEvent] Publish - 허브가 삭제되었습니다.");
        applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publishEvent(HubNameUpdatedEvent event) {
        log.info("[HubNameUpdatedEvent] Publish - 허브이름이 갱신되었습니다.");
        applicationEventPublisher.publishEvent(event);
    }
}
