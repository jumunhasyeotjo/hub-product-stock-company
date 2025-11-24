package com.jumunhasyeo.common.Idempotency;

import com.jumunhasyeo.CommonTestContainer;
import com.jumunhasyeo.TestConfig;
import com.jumunhasyeo.common.RedisKey;
import com.jumunhasyeo.common.config.CacheConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.StringRedisTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({TestConfig.class, CacheConfig.class})
class RedisIdempotencyServiceTest extends CommonTestContainer {

    @Autowired
    private RedisIdempotencyService idempotencyService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String TEST_KEY = "test-order-123";
    private static final long TTL_SECONDS = 60L;

    @AfterEach
    void tearDown() {
        // Redis 데이터 정리
        redisTemplate.delete(RedisKey.idempotentKey(TEST_KEY).value());
        redisTemplate.delete(RedisKey.idempotentErrorKey(TEST_KEY).value());
    }

    @Test
    @DisplayName("상태 저장 및 조회 성공")
    void saveStatus_AndGetCurrentStatus_Success() {
        // given
        IdempotentStatus expectedStatus = IdempotentStatus.SUCCESS;

        // when
        idempotencyService.saveStatus(TEST_KEY, expectedStatus, TTL_SECONDS);
        IdempotentStatus actualStatus = idempotencyService.getCurrentStatus(TEST_KEY);

        // then
        assertThat(actualStatus).isEqualTo(expectedStatus);
    }

    @Test
    @DisplayName("setIfAbsent로 PROCESSING 설정 성공")
    void setIfAbsent_WhenKeyNotExists_ReturnsTrue() {
        // when
        Boolean result = idempotencyService.setIfAbsent(TEST_KEY, IdempotentStatus.PROCESSING, TTL_SECONDS);

        // then
        assertThat(result).isTrue();
        assertThat(idempotencyService.getCurrentStatus(TEST_KEY)).isEqualTo(IdempotentStatus.PROCESSING);
    }

    @Test
    @DisplayName("setIfAbsent로 이미 존재하는 키 설정 실패")
    void setIfAbsent_WhenKeyExists_ReturnsFalse() {
        // given
        idempotencyService.saveStatus(TEST_KEY, IdempotentStatus.PROCESSING, TTL_SECONDS);

        // when
        Boolean result = idempotencyService.setIfAbsent(TEST_KEY, IdempotentStatus.PROCESSING, TTL_SECONDS);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("에러 메시지 저장 및 조회 성공")
    void saveError_Success() {
        // given
        String errorMsg = "Test error message";

        // when
        idempotencyService.saveError(TEST_KEY, errorMsg, TTL_SECONDS);

        // then
        String savedError = redisTemplate.opsForValue()
                .get(RedisKey.idempotentErrorKey(TEST_KEY).value());
        assertThat(savedError).isEqualTo(errorMsg);
    }

    @Test
    @DisplayName("존재하지 않는 키 조회 시 NONE 반환")
    void getCurrentStatus_WhenKeyNotExists_ReturnsNone() {
        // when
        IdempotentStatus status = idempotencyService.getCurrentStatus("non-existent-key");

        // then
        assertThat(status).isEqualTo(IdempotentStatus.NONE);
    }
}
