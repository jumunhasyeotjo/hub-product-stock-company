package com.jumunhasyeo.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // 공통
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E001", "서버 에러가 발생했습니다."),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "E002", "잘못된 요청입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "E003", "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "E004", "접근이 거부되었습니다."),
    CREATE_VALIDATE_EXCEPTION(HttpStatus.BAD_REQUEST, "E005", "객체 생성에 실패했습니다."),
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "E006", "입력값 검증에 실패했습니다."),
    INVALID_JSON(HttpStatus.BAD_REQUEST, "E007", "잘못된 JSON 형식입니다."),
    MUST_NOT_NULL(HttpStatus.BAD_REQUEST, "E008", "는(은) null일 수 없습니다."),
    NOT_FOUND_EXCEPTION(HttpStatus.BAD_REQUEST, "EN001", "조회에 실패했습니다."),
    STOCK_VALID(HttpStatus.BAD_REQUEST, "ES", "Stock 유효성 검사에 실패했습니다."),
    ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "E009", "이미 존재합니다."),
    PROCESSING_CONFLICT_EXCEPTION(HttpStatus.CONFLICT, "C001", "요청이 진행중입니다."),
    SUCCESS_CONFLICT_EXCEPTION(HttpStatus.CONFLICT, "C002", "요청이 이미 완료되었습니다."),
    //product
    PRODUCT_VALID(HttpStatus.BAD_REQUEST, "EP", "Product 유효성 검사에 실패했습니다");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
