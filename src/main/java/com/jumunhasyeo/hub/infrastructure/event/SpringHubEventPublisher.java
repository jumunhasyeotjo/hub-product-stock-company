package com.jumunhasyeo.hub.infrastructure.event;

import com.jumunhasyeo.hub.application.HubEventPublisher;
import com.jumunhasyeo.hub.domain.event.HubDomainEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SpringHubEventPublisher implements HubEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publishEvent(HubDomainEvent event) {
        applicationEventPublisher.publishEvent(event);
    }
}
