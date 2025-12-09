package com.jumunhasyeo.stock.presentation.docs;
import com.jumunhasyeo.common.ApiRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(
        summary = "허브 재고 감소",
        description = "특정 상품의 허브 재고를 감소시킵니다. 출고 처리 또는 주문 시 사용됩니다. " +
                "재고가 부족한 경우 예외가 발생합니다. (Atomic 연산 보장)"
)
public @interface ApiDocDecrementStock {
}
