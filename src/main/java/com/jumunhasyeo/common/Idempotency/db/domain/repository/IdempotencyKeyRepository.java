package com.jumunhasyeo.common.Idempotency.db.domain.repository;

import com.jumunhasyeo.common.Idempotency.db.domain.DbIdempotentKey;

import java.time.LocalDateTime;
import java.util.Optional;

public interface IdempotencyKeyRepository{
    Optional<DbIdempotentKey> findByIdempotencyKeyAndNotExpired(String key, LocalDateTime now);
    DbIdempotentKey save(DbIdempotentKey dbIdempotentKey);
}
