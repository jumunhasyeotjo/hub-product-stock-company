package com.jumunhasyeo.product.domain.vo;

import com.jumunhasyeo.common.exception.BusinessException;
import com.jumunhasyeo.common.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


class PriceTest {

    @Test
    @DisplayName("가격은 0 보다 작은 값이 입력될 수 없다.")
    void of_WhenPriceZero_shouldThrowException() {
        // given & when
        BusinessException businessException =
                assertThrows(BusinessException.class, () -> {
                    Price.of(-1);
        });
        
        // then
        assertThat(businessException.getErrorCode()).isEqualTo(ErrorCode.PRODUCT_VALID);
    }
}