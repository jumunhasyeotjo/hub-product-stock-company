package com.jumunhasyeo.stock.presentation.docs;

import io.swagger.v3.oas.annotations.Operation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(
        summary = "허브 재고 증가",
        description = "특정 상품의 허브 재고를 증가시킵니다. 입고 처리 시 사용됩니다."
)
public @interface ApiDocIncrementStock {
}