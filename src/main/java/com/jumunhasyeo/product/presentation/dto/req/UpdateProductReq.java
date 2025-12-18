package com.jumunhasyeo.product.presentation.dto.req;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UpdateProductReq(
        @NotEmpty(message = "상품명은 필수 입력값 입니다.")
        String name,
        @NotNull(message = "가격은 필수 입력값 입니다.")
        Integer price,
        @NotEmpty(message = "상품 설명은 필수 입력값 입니다.")
        String description
) {
}
