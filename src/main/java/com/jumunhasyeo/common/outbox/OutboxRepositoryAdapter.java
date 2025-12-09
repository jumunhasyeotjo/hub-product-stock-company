package com.jumunhasyeo.common.outbox;

import com.jumunhasyeo.common.exception.BusinessException;
import com.jumunhasyeo.common.exception.ErrorCode;
import com.jumunhasyeo.hub.hub.domain.event.HubNameUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OutboxRepositoryAdapter implements OutboxRepository{
    private final JpaOutboxRepository jpaOutboxRepository;

    @Override
    public Outbox save(Outbox outbox) {
        return jpaOutboxRepository.save(outbox);
    }

    @Override
    public List<Outbox> findTop100ByStatusOrderByIdAsc(OutboxStatus status) {
        return jpaOutboxRepository.findTop100ByStatusOrderByIdAsc(status);
    }

    @Override
    public int deleteByStatusAndCreatedAtBefore(OutboxStatus outboxStatus, LocalDateTime localDateTime) {
        return jpaOutboxRepository.deleteByStatusAndCreatedAtBefore(outboxStatus, localDateTime);
    }

    @Override
    public Outbox findByEventKey(String eventKey) {
        return jpaOutboxRepository.findByEventKey(eventKey)
                .orElseThrow(() -> new BusinessException(ErrorCode.OUTBOX_EVENT_NOT_FOUND));
    }
}
