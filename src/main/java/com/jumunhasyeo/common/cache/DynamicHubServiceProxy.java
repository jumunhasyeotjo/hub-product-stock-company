package com.jumunhasyeo.common.cache;

import com.jumunhasyeo.hub.hub.application.HubService;
import com.jumunhasyeo.hub.hub.application.command.CreateHubCommand;
import com.jumunhasyeo.hub.hub.application.command.DeleteHubCommand;
import com.jumunhasyeo.hub.hub.application.command.UpdateHubCommand;
import com.jumunhasyeo.hub.hub.application.dto.response.HubRes;
import com.jumunhasyeo.hub.hub.presentation.dto.HubSearchCondition;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 동적 캐시 스위칭용 HubService 프록시
 * 
 * 런타임에 cache.active 값에 따라 구현체 전환
 */
@Slf4j
@Service
@Primary
@ConditionalOnProperty(name = "cache.dynamic", havingValue = "true")
public class DynamicHubServiceProxy implements HubService {
    
    private final DynamicCacheConfig cacheConfig;
    private final Map<String, HubService> implementations;
    
    public DynamicHubServiceProxy(
            DynamicCacheConfig cacheConfig,
            List<HubService> hubServices
    ) {
        this.cacheConfig = cacheConfig;
        this.implementations = new java.util.HashMap<>();
        
        for (HubService service : hubServices) {
            if (service == this) continue; // 자기 자신 제외
            
            String className = service.getClass().getSimpleName();
            if (className.contains("Caffeine")) {
                implementations.put("CAFFEINE", service);
            } else if (className.contains("Redis")) {
                implementations.put("REDIS", service);
            } else if (className.equals("HubServiceImpl")) {
                implementations.put("NONE", service);
            }
        }
        
        log.info("[DynamicCache] Proxy initialized: {}", implementations.keySet());
    }
    
    private HubService resolve() {
        String active = cacheConfig.getActive().toUpperCase();
        HubService impl = implementations.get(active);
        
        if (impl == null) {
            log.warn("[DynamicCache] Unknown type '{}', fallback to CAFFEINE", active);
            impl = implementations.get("CAFFEINE");
        }
        
        return impl;
    }
    
    public String getCurrentType() {
        return cacheConfig.getActive();
    }
    
    // ==================== HubService 위임 ====================
    
    @Override
    public HubRes create(CreateHubCommand command) {
        return resolve().create(command);
    }
    
    @Override
    public HubRes update(UpdateHubCommand command) {
        return resolve().update(command);
    }
    
    @Override
    public UUID delete(DeleteHubCommand command) {
        return resolve().delete(command);
    }
    
    @Override
    public HubRes getById(UUID hubId) {
        return resolve().getById(hubId);
    }
    
    @Override
    public Page<HubRes> search(HubSearchCondition condition, Pageable pageable) {
        return resolve().search(condition, pageable);
    }
    
    @Override
    public Boolean existById(UUID hubId) {
        return resolve().existById(hubId);
    }
    
    @Override
    public List<HubRes> getAll() {
        return resolve().getAll();
    }
}
