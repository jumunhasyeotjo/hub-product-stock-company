package com.jumunhasyeo.hub.domain.event;

import com.jumunhasyeo.hub.domain.entity.Hub;
import com.jumunhasyeo.hub.domain.vo.Address;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public final class HubCreatedEvent extends HubDomainEvent {
    private final UUID hubId;
    private final String name;
    private final Address address;
    private final LocalDateTime occurredAt;

    public HubCreatedEvent(UUID hubId, String name, Address address, LocalDateTime occurredAt) {
        this.hubId = hubId;
        this.name = name;
        this.address = address;
        this.occurredAt = occurredAt;
    }

    public static HubCreatedEvent of(Hub hub) {
        return new HubCreatedEvent(
                hub.getHubId(),
                hub.getName(),
                hub.getAddress(),
                LocalDateTime.now()
        );
    }
}