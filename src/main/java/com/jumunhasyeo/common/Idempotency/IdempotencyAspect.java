package com.jumunhasyeo.common.Idempotency;

import com.jumunhasyeo.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import static com.jumunhasyeo.common.exception.ErrorCode.PROCESSING_CONFLICT_EXCEPTION;
import static com.jumunhasyeo.common.exception.ErrorCode.SUCCESS_CONFLICT_EXCEPTION;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class IdempotencyAspect {
    private final IdempotencyService idempotencyService;

    @Around("@annotation(idempotent)")
    public Object handleIdempotency(
            ProceedingJoinPoint joinPoint,
            Idempotent idempotent
    ) throws Throwable {

        Object[] args = joinPoint.getArgs();
        // 첫 번째 파라미터 = 멱등키
        String statusKey = (String) args[0];
        long ttlSeconds = getTtlSeconds(idempotent);
        log.info("Idempotent request - key: {}, ttl: {} days", statusKey, idempotent.ttlDays());

        // 1. 상태 확인
        IdempotentStatus idempotentStatus = idempotencyService.getCurrentStatus(statusKey);

        // 2-1. COMPLETED:
        if (isSuccess(idempotentStatus)) {
            throw new BusinessException(SUCCESS_CONFLICT_EXCEPTION);
        }

        // 2-2. PROCESSING: SET NX (없을 때만 PROCESSING 설정)
        Boolean acquired = idempotencyService.setIfAbsent(statusKey, IdempotentStatus.PROCESSING, ttlSeconds);
        if (isProcessing(acquired)){
            throw new BusinessException(PROCESSING_CONFLICT_EXCEPTION);
        }

        // 3. FAILED 또는 없음 → 처리 시작
        try {
            return proceed(joinPoint, statusKey, ttlSeconds);
        } catch (Exception e) { // 5. 실패 → FAILED + 에러 저장 (재시도 가능)
            fail(e, statusKey, ttlSeconds);
            throw e;
        }
    }

    private boolean isSuccess(IdempotentStatus idempotentStatus) {
        return idempotentStatus.isSuccess();
    }

    private boolean isProcessing(Boolean acquired) {
        // 이미 processing이 존재 한다면 or set을 실패 했다면
        return Boolean.FALSE.equals(acquired);
    }

    private Object proceed(ProceedingJoinPoint joinPoint, String statusKey, long ttlSeconds) throws Throwable {
        log.info("Executing business logic for key: {}", statusKey);
        Object result = joinPoint.proceed();
        idempotencyService.saveStatus(statusKey, IdempotentStatus.SUCCESS, ttlSeconds);
        log.info("Successfully completed and cached result for key: {}", statusKey);
        return result;
    }

    private void fail(Exception e, String statusKey, long ttlSeconds) {
        log.error("Business logic failed for key: {}", statusKey, e);
        idempotencyService.saveStatus(statusKey, IdempotentStatus.FAIL, ttlSeconds);
        idempotencyService.saveError(statusKey, getErrorMsg(e), ttlSeconds);
    }

    private String getErrorMsg(Exception e) {
        return e.getMessage() != null ? e.getMessage() : "Unknown error";
    }

    private long getTtlSeconds(Idempotent idempotent) {
        return idempotent.ttlDays() * 24 * 3600L;
    }
}