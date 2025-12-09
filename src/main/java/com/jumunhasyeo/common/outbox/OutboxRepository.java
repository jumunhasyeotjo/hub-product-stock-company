package com.jumunhasyeo.common.outbox;

import com.jumunhasyeo.hub.hub.domain.event.HubNameUpdatedEvent;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OutboxRepository {
    Outbox save(Outbox outbox);
    List<Outbox> findTop100ByStatusOrderByIdAsc(OutboxStatus status);
    int deleteByStatusAndCreatedAtBefore(OutboxStatus outboxStatus, LocalDateTime localDateTime);
    Outbox findByEventKey(String eventKey);
}
