package com.jumunhasyeo.product.presentation.dto.res;

import java.util.UUID;

public record OrderProductRes(
        UUID productId,
        UUID companyId,
        String name,
        int price
) {
}
