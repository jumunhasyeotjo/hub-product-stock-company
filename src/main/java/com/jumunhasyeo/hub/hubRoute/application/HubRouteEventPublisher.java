package com.jumunhasyeo.hub.hubRoute.application;

import com.jumunhasyeo.hub.hubRoute.domain.event.HubRouteCreatedEvent;
import com.jumunhasyeo.hub.hubRoute.domain.event.HubRouteDeletedEvent;

import java.util.List;

public interface HubRouteEventPublisher {
    void publishRouteCreatedEvent(List<HubRouteCreatedEvent> eventList);
    void publishRouteDeletedEvent(List<HubRouteDeletedEvent> eventList);
}
