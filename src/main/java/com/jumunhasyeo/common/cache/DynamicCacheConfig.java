package com.jumunhasyeo.common.cache;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 동적 캐시 설정
 * 
 * 런타임에 API로 변경 가능
 */
@Data
@Component
@ConfigurationProperties(prefix = "cache")
public class DynamicCacheConfig {
    
    /**
     * 활성화할 캐시 타입
     * CAFFEINE | REDIS | NONE
     */
    private String active = "CAFFEINE";
}
