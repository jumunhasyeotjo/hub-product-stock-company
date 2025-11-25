package com.jumunhasyeo.common.Idempotency.db.domain.repository;

import com.jumunhasyeo.common.Idempotency.db.domain.DbIdempotentKey;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IdempotencyKeyRepository{
    Optional<DbIdempotentKey> findByIdempotencyKeyAndNotExpired(String key, LocalDateTime now);
    DbIdempotentKey save(DbIdempotentKey dbIdempotentKey);
    List<DbIdempotentKey> findStaleProcessingKeys(LocalDateTime threshold);
    List<DbIdempotentKey> findExpiredKeys(LocalDateTime now);
    void deleteAll(List<DbIdempotentKey> expiredKeys);
}
