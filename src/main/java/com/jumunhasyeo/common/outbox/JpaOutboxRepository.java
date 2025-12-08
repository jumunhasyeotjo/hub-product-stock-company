package com.jumunhasyeo.common.outbox;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface JpaOutboxRepository extends JpaRepository<OutboxEvent, UUID> {
    // PENDING 상태인 이벤트 상위 100개 조회
    List<OutboxEvent> findTop100ByStatusOrderByIdAsc(OutboxStatus status);
    int deleteByStatusAndCreatedAtBefore(OutboxStatus outboxStatus, LocalDateTime localDateTime);
    Optional<OutboxEvent> findByEventKey(String eventKey);
}
