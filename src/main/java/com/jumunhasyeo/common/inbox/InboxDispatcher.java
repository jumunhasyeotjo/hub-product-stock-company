package com.jumunhasyeo.common.inbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jumunhasyeo.common.Idempotency.db.application.DbIdempotentService;
import com.jumunhasyeo.common.Idempotency.db.domain.DbIdempotentKey;
import com.jumunhasyeo.stock.application.StockService;
import com.jumunhasyeo.stock.application.command.IncreaseStockCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.jumunhasyeo.stock.infrastructure.event.ListenEventRegistry.ORDER_CANCEL_EVENT;
import static com.jumunhasyeo.stock.infrastructure.event.ListenEventRegistry.ORDER_ROLLED_BACK_EVENT;

@Service
@RequiredArgsConstructor
@Slf4j
public class InboxDispatcher {

    private final DbIdempotentService dbIdempotentService;
    private final StockService stockService;
    private final ObjectMapper objectMapper;

    public void dispatch(InboxEvent event) {
        log.info("Retrying event: {} (type: {}, attempt: {})",
                event.getEventKey(), event.getEventName(), event.getRetryCount() + 1);
        DbIdempotentKey dbIdempotentKey = dbIdempotentService.get(event.getEventKey());

        try {
            // 이벤트 타입에 따라 처리
            if (event.getEventName().equals(ORDER_CANCEL_EVENT.getEventName())) {
                stockService.increment(dbIdempotentKey.genCancelKey(), getPayload(dbIdempotentKey));
            } else if (event.getEventName().equals(ORDER_ROLLED_BACK_EVENT.getEventName())){
                stockService.increment(dbIdempotentKey.genCancelKey(), getPayload(dbIdempotentKey));
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to process event", e);
        }
    }

    private List<IncreaseStockCommand> getPayload(DbIdempotentKey dbIdempotentKey) throws JsonProcessingException {
        return objectMapper.readValue(dbIdempotentKey.getPayload(), new TypeReference<>() {});
    }
}
