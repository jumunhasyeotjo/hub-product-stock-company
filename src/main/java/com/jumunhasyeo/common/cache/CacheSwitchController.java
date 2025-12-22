package com.jumunhasyeo.common.cache;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

/**
 * 캐시 동적 전환 API
 */
@Slf4j
@RestController
@RequestMapping("/internal/api/v1/cache")
@RequiredArgsConstructor
public class CacheSwitchController {
    
    private final DynamicCacheConfig cacheConfig;
    
    private static final Set<String> VALID_TYPES = Set.of("CAFFEINE", "REDIS", "NONE");
    
    /**
     * 현재 캐시 타입 조회
     */
    @GetMapping
    public ResponseEntity<Map<String, String>> getCurrent() {
        return ResponseEntity.ok(Map.of("active", cacheConfig.getActive()));
    }
    
    /**
     * 캐시 타입 전환
     * 
     * PUT /internal/api/v1/cache/switch?type=REDIS
     */
    @PutMapping("/switch")
    public ResponseEntity<Map<String, Object>> switchCache(@RequestParam String type) {
        String normalized = type.toUpperCase();
        
        if (!VALID_TYPES.contains(normalized)) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Invalid type. Must be: " + VALID_TYPES
            ));
        }
        
        String previous = cacheConfig.getActive();
        cacheConfig.setActive(normalized);
        
        log.info("[CacheSwitch] {} -> {}", previous, normalized);
        
        return ResponseEntity.ok(Map.of(
                "previous", previous,
                "current", normalized,
                "message", "Cache switched successfully"
        ));
    }
}
