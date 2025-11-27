package com.jumunhasyeo.common;

/**
 * Redis의 키 생성 책임을 가집니다.
 * @param value
 */
public record RedisKey(String value) {
    private static final String idempotentKey = "idempotency:status:";
    private static final String idempotentErrorKey = "idempotency:error:";

    public static RedisKey idempotentKey(String idempotencyKey) {
        return new RedisKey(idempotentKey + idempotencyKey);
    }

    public static RedisKey idempotentErrorKey(String idempotencyKey) {
        return new RedisKey(idempotentErrorKey + idempotencyKey);
    }
}
