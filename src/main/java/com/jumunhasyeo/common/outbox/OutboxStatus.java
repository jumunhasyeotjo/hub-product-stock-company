package com.jumunhasyeo.common.outbox;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OutboxStatus {
    PENDING("대기중"),
    COMPLETE("완료");

    private final String description;
}
