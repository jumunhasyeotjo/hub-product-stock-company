package com.jumunhasyeo.hub.hub.domain.event;

import com.jumunhasyeo.hub.hub.domain.entity.Hub;
import com.jumunhasyeo.hub.hub.domain.entity.HubType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class HubDeletedEvent extends HubDomainEvent {
    private final UUID hubId;
    private final String hubName;
    private final HubType hubType;
    private final Long deletedBy;

    public static HubDeletedEvent of(UUID hubId, String hubName, HubType hubType, Long deletedBy) {
        return new HubDeletedEvent(hubId, hubName, hubType, deletedBy);
    }

    public static HubDeletedEvent from(Hub hub, Long userId) {
        return new HubDeletedEvent(
                hub.getHubId(),
                hub.getName(),
                hub.getHubType(),
                userId
        );
    }
}
