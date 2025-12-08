package com.jumunhasyeo.hub.hubRoute.domain.event;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class HubRouteDomainEvent {
    private final LocalDateTime occurredAt;

    protected HubRouteDomainEvent() {
        this.occurredAt = LocalDateTime.now();
    }
}
