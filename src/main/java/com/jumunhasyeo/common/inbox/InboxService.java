package com.jumunhasyeo.common.inbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jumunhasyeo.stock.infrastructure.event.OrderCompensationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.jumunhasyeo.stock.infrastructure.event.ListenEventRegistry.ORDER_CANCEL_EVENT;

@Service
@RequiredArgsConstructor
@Slf4j
public class InboxService {
    private final InboxRepository inboxRepository;
    private final InboxDispatcher inboxDispatcher;
    private final ObjectMapper objectMapper;

    public void save(OrderCompensationEvent event) throws JsonProcessingException {
        InboxEvent inboxEvent = InboxEvent.builder()
                .eventKey(event.getKey())
                .eventName(ORDER_CANCEL_EVENT.name())
                .payload(objectMapper.writeValueAsString(event))
                .status(InboxStatus.RECEIVED)
                .receivedAt(LocalDateTime.now())
                .build();
        inboxRepository.save(inboxEvent);
    }

    public List<InboxEvent> findByStatusAndModifiedAtBefore(InboxStatus inboxStatus, LocalDateTime threshold) {
        return inboxRepository.findByStatusAndModifiedAtBefore(inboxStatus, threshold);
    }

    public void inboxProcess(InboxEvent event) {
        try {
            if (!event.canRetry()) {
                event.markFailed("Max retry count exceeded");
                return;
            }
            inboxDispatcher.dispatch(event);

            // 처리 성공
            event.dispatchSuccess();

        } catch (Exception e) {
            event.dispatchFail(e.getMessage());
            inboxRepository.save(event);
        }
    }
}
