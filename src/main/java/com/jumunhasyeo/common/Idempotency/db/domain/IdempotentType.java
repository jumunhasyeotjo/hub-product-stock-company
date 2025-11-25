package com.jumunhasyeo.common.Idempotency.db.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum IdempotentType {
    STOCK("재고");
    private final String description;
}
