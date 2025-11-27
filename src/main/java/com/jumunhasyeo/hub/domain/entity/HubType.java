package com.jumunhasyeo.hub.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HubType {
    CENTER("중앙 허브"),
    BRANCH("지점 허브");

    private final String description;
}
