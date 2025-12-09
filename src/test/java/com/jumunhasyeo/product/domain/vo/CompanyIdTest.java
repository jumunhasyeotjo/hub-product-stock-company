package com.jumunhasyeo.product.domain.vo;

import com.jumunhasyeo.common.exception.BusinessException;
import com.jumunhasyeo.common.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CompanyIdTest {

    @Test
    @DisplayName("CompanyId는 null 일수 없다.")
    void of_WhenProductDescriptionNull_shouldThrowException() {
        // given & when
        BusinessException businessException =
                assertThrows(BusinessException.class, () -> {
                    CompanyId.of(null);
                });

        // then
        assertThat(businessException.getErrorCode()).isEqualTo(ErrorCode.PRODUCT_VALID_FAIL);
    }
}