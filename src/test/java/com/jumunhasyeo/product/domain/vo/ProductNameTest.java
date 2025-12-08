package com.jumunhasyeo.product.domain.vo;

import com.jumunhasyeo.common.exception.BusinessException;
import com.jumunhasyeo.common.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductNameTest {

    @Test
    @DisplayName("상품 이름은 null 일수 없다")
    void of_WhenProductNameNull_shouldThrowException() {
        // given & when
        BusinessException businessException =
                assertThrows(BusinessException.class, () -> {
                    ProductName.of(null);
                });

        // then
        assertThat(businessException.getErrorCode()).isEqualTo(ErrorCode.PRODUCT_VALID_FAIL);
    }
}