package com.jumunhasyeo.common.Idempotency.db.application;

import com.jumunhasyeo.common.Idempotency.db.domain.DbIdempotentKey;
import com.jumunhasyeo.common.Idempotency.db.domain.IdempotentStatus;
import com.jumunhasyeo.common.Idempotency.db.domain.IdempotentType;
import com.jumunhasyeo.common.Idempotency.db.domain.repository.IdempotencyKeyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class DbIdempotentService implements IdempotentService {
    private final IdempotencyKeyRepository repository;

    @Transactional
    public IdempotentStatus getCurrentStatus(String key) {
        return repository.findByIdempotencyKeyAndNotExpired(key, LocalDateTime.now())
                .map(DbIdempotentKey::getStatus)
                .orElse(IdempotentStatus.NONE);
    }

    @Transactional
    public Boolean setIfAbsent(String key, IdempotentStatus status, long ttlSeconds) {
        try {
            DbIdempotentKey entity = DbIdempotentKey.create(key, ttlSeconds, IdempotentType.STOCK);
            repository.save(entity);
            return true;
        } catch (DataIntegrityViolationException e) {
            // Unique constraint 위반 - 이미 존재하는 경우
            log.info("Key already exists (race condition): {}", key);
            return false;
        } catch (Exception e) {
            log.error("Unexpected error while setting key: {}", key, e);
            throw e;
        }
    }

    @Transactional
    public void saveStatus(String key, IdempotentStatus status, long ttlSeconds) {
        repository.findByIdempotencyKeyAndNotExpired(key, LocalDateTime.now())
                .ifPresent(entity -> {
                    entity.updateStatus(status);
                    repository.save(entity);
                });
    }

    @Transactional
    public void saveError(String key, String errorMessage, long ttlSeconds) {
        repository.findByIdempotencyKeyAndNotExpired(key, LocalDateTime.now())
                .ifPresent(entity -> {
                    entity.applyError(errorMessage);
                    repository.save(entity);
                });
    }
}
