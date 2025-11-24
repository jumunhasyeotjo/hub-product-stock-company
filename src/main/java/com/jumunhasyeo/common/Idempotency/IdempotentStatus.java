package com.jumunhasyeo.common.Idempotency;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum IdempotentStatus {
    SUCCESS("SUCCESS", "성공"),
    FAIL("FAIL", "실패"),
    PROCESSING("PROCESSING", "진행중"),
    NONE("NONE", "상태없음");

    private final String value;
    private final String description;

    public boolean equals(String value){
        return this.value.equals(value);
    }

    public boolean isSuccess() {
        return this.equals(SUCCESS);
    }

    public boolean isProcessing() {
        return this.equals(PROCESSING);
    }
}
