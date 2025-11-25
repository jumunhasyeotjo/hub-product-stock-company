package com.jumunhasyeo.common.Idempotency.db.infrastructure.repository;

import com.jumunhasyeo.common.Idempotency.db.domain.DbIdempotentKey;
import com.jumunhasyeo.common.Idempotency.db.domain.repository.IdempotencyKeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class IdempotentKeyRepositoryAdapter implements IdempotencyKeyRepository {
    private final JpaIdempotentKeyRepository jpaIdempotentKeyRepository;

    @Override
    public Optional<DbIdempotentKey> findByIdempotencyKeyAndNotExpired(String key, LocalDateTime now) {
        return jpaIdempotentKeyRepository.findByIdempotencyKeyAndNotExpired(key, now);
    }

    @Override
    public DbIdempotentKey save(DbIdempotentKey dbIdempotentKey) {
        return jpaIdempotentKeyRepository.save(dbIdempotentKey);
    }
}
