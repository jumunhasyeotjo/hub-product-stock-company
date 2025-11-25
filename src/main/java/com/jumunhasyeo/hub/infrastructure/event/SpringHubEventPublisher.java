package com.jumunhasyeo.hub.infrastructure.event;

import com.jumunhasyeo.hub.application.HubEventPublisher;
import com.jumunhasyeo.hub.application.command.CreateHubCommand;
import com.jumunhasyeo.hub.domain.event.HubCreatedEvent;
import com.jumunhasyeo.hub.domain.event.HubDeletedEvent;
import com.jumunhasyeo.hub.domain.event.HubDomainEvent;
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
}
