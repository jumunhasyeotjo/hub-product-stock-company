package com.jumunhasyeo.common.outbox;

import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OutboxRepository {
    OutboxEvent save(OutboxEvent outboxEvent);
    List<OutboxEvent> findTop100ByStatusOrderByIdAsc(OutboxStatus status);
    int deleteByStatusAndCreatedAtBefore(OutboxStatus outboxStatus, LocalDateTime localDateTime);
    OutboxEvent findByEventKey(String eventKey);
}
