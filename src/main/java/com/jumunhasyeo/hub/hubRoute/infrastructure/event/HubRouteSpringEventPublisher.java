package com.jumunhasyeo.hub.hubRoute.infrastructure.event;

import com.jumunhasyeo.hub.hubRoute.application.HubRouteEventPublisher;
import com.jumunhasyeo.hub.hubRoute.domain.event.HubRouteCreatedEvent;
import com.jumunhasyeo.hub.hubRoute.domain.event.HubRouteDeletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class HubRouteSpringEventPublisher implements HubRouteEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publishRouteCreatedEvent(List<HubRouteCreatedEvent> eventList) {
        applicationEventPublisher.publishEvent(eventList);
    }

    @Override
    public void publishRouteDeletedEvent(List<HubRouteDeletedEvent> eventList) {
        applicationEventPublisher.publishEvent(eventList);
    }
}
