package com.jumunhasyeo.common.Idempotency.db.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "p_db_idempotency_keys")
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class DbIdempotentKey {
    @Id
    private String idempotencyKey;

    @Enumerated(EnumType.STRING)
    @Column(name = "idempotent_status")
    private IdempotentStatus status;

    @Column(name = "error_message")
    private String errorMessage;

    @Enumerated(EnumType.STRING)
    private IdempotentType type;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "expiresAt", nullable = false)
    private LocalDateTime expiresAt;

    public static DbIdempotentKey create(String idempotencyKey, long ttlSeconds, IdempotentType type){
            return new DbIdempotentKey(
                    idempotencyKey,
                    IdempotentStatus.PROCESSING,
                    "",
                    type,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusSeconds(ttlSeconds));
        }

    public void updateStatus(IdempotentStatus status) {
        this.status = status;
    }

    public void applyError(String errorMessage) {
        this.status = IdempotentStatus.FAIL;
        this.errorMessage = errorMessage;
    }
}

