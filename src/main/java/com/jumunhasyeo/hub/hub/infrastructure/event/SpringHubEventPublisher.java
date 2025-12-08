package com.jumunhasyeo.hub.hub.infrastructure.event;

import com.jumunhasyeo.hub.hub.application.HubEventPublisher;
import com.jumunhasyeo.hub.hub.domain.event.HubCreatedEvent;
import com.jumunhasyeo.hub.hub.domain.event.HubDeletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * 25.12.8 / kafka 도입으로 deprecated 처리
 */
@Deprecated
@Component
@RequiredArgsConstructor
@Slf4j
public abstract class SpringHubEventPublisher implements HubEventPublisher {
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
}
