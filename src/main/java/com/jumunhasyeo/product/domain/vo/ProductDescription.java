package com.jumunhasyeo.product.domain.vo;

import com.jumunhasyeo.common.exception.BusinessException;
import com.jumunhasyeo.common.exception.ErrorCode;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductDescription {

    String description;

    public ProductDescription(String description) {
        if (description == null || description.isEmpty()) {
            throw new BusinessException(ErrorCode.MUST_NOT_NULL, "description");
        }
        this.description = description;
    }

    public static ProductDescription of(String description) {
        return new ProductDescription(description);
    }
}
