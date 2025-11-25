package com.jumunhasyeo.common.Idempotency.db.application;

import com.jumunhasyeo.common.Idempotency.db.domain.IdempotentStatus;

public interface IdempotentService {
    void saveStatus(String statusKey, IdempotentStatus status, long ttlSeconds);
    Boolean setIfAbsent(String statusKey, IdempotentStatus processing, long ttlSeconds);
    IdempotentStatus getCurrentStatus(String statusKey);
    void saveError(String statusKey, String errorMsg, long ttlSeconds);
}
