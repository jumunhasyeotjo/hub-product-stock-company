package com.jumunhasyeo.common.Idempotency;

public interface IdempotencyService {
    void saveStatus(String statusKey, IdempotentStatus status, long ttlSeconds);
    Boolean setIfAbsent(String statusKey, IdempotentStatus processing, long ttlSeconds);
    IdempotentStatus getCurrentStatus(String statusKey);
    void saveError(String statusKey, String errorMsg, long ttlSeconds);
}
