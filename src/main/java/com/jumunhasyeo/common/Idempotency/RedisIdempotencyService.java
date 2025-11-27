package com.jumunhasyeo.common.Idempotency;

import com.jumunhasyeo.common.RedisKey;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisIdempotencyService implements IdempotencyService {
    private final StringRedisTemplate redisTemplate;

    public void saveStatus(String statusKey, IdempotentStatus status, long ttlSeconds) {
        RedisKey key = getKey(statusKey);
        redisTemplate.opsForValue().set(
                key.value(),
                status.getValue(),
                ttlSeconds,
                TimeUnit.SECONDS
        );
    }

    public Boolean setIfAbsent(String statusKey, IdempotentStatus processing, long ttlSeconds) {
        RedisKey key = getKey(statusKey);
        return redisTemplate.opsForValue()
                .setIfAbsent(key.value(), processing.getValue(), ttlSeconds, TimeUnit.SECONDS);
    }

    public IdempotentStatus getCurrentStatus(String statusKey) {
        RedisKey key = getKey(statusKey);
        String value = redisTemplate.opsForValue().get(key.value());
        if(StringUtils.isBlank(value))
            return IdempotentStatus.NONE;
        return IdempotentStatus.valueOf(value);
    }

    public void saveError(String statusKey, String errorMsg, long ttlSeconds) {
        RedisKey key = RedisKey.idempotentErrorKey(statusKey);
        redisTemplate.opsForValue().set(
                key.value(),
                errorMsg,
                ttlSeconds,
                TimeUnit.SECONDS
        );
    }

    private RedisKey getKey(String idempotencyKey) {
        return RedisKey.idempotentKey(idempotencyKey);
    }
}
