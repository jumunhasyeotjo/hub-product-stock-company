package com.jumunhasyeo.common.dynamic;

import com.jumunhasyeo.stock.application.StockVariationService;
import com.jumunhasyeo.stock.application.command.DecreaseStockCommand;
import com.jumunhasyeo.stock.application.command.IncreaseStockCommand;
import com.jumunhasyeo.stock.application.dto.response.StockRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 동적 StockVariationService 프록시
 * 
 * DEFAULT: 일반 구현체 (atomic update)
 * PESSIMISTIC_LOCK: 비관적 락 구현체
 */
@Slf4j
@Service
@Primary
@ConditionalOnProperty(name = "dynamic.enabled", havingValue = "true")
public class DynamicStockVariationServiceProxy implements StockVariationService {
    
    private final DynamicConfig config;
    private final Map<String, StockVariationService> implementations;
    
    public DynamicStockVariationServiceProxy(
            DynamicConfig config,
            List<StockVariationService> stockServices
    ) {
        this.config = config;
        this.implementations = new java.util.HashMap<>();
        
        for (StockVariationService service : stockServices) {
            if (service == this) continue;
            
            String className = service.getClass().getSimpleName();
            if (className.contains("PessimisticLock")) {
                implementations.put("PESSIMISTIC_LOCK", service);
            } else if (className.equals("StockVariationServiceImpl")) {
                implementations.put("DEFAULT", service);
            }
        }
        
        log.info("[Dynamic] StockVariationService Proxy initialized: {}", implementations.keySet());
    }
    
    private StockVariationService resolve() {
        String active = config.getStockLock().toUpperCase();
        StockVariationService impl = implementations.get(active);
        
        if (impl == null) {
            log.warn("[Dynamic] Unknown stock type '{}', fallback to DEFAULT", active);
            impl = implementations.get("DEFAULT");
        }
        
        return impl;
    }
    
    @Override
    public StockRes decrement(DecreaseStockCommand command) {
        return resolve().decrement(command);
    }
    
    @Override
    public StockRes increment(IncreaseStockCommand command) {
        return resolve().increment(command);
    }
}
