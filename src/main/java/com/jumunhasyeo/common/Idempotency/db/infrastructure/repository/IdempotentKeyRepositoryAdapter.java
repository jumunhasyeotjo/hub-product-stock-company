package com.jumunhasyeo.common.Idempotency.db.infrastructure.repository;

import com.jumunhasyeo.common.Idempotency.db.domain.DbIdempotentKey;
import com.jumunhasyeo.common.Idempotency.db.domain.repository.IdempotencyKeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class IdempotentKeyRepositoryAdapter implements IdempotencyKeyRepository {
    private final JpaIdempotentKeyRepository repository;

    @Override
    public Optional<DbIdempotentKey> findByIdempotencyKeyAndNotExpired(String key, LocalDateTime now) {
        return repository.findByIdempotencyKeyAndNotExpired(key, now);
    }

    @Override
    public DbIdempotentKey save(DbIdempotentKey dbIdempotentKey) {
        return repository.save(dbIdempotentKey);
    }

    @Override
    public List<DbIdempotentKey> findStaleProcessingKeys(LocalDateTime threshold) {
        return repository.findStaleProcessingKeys(threshold);
    }

    @Override
    public List<DbIdempotentKey> findExpiredKeys(LocalDateTime now) {
        return repository.findExpiredKeys(now);
    }

    @Override
    public void deleteAll(List<DbIdempotentKey> expiredKeys) {
        repository.deleteAll(expiredKeys);
    }
}
