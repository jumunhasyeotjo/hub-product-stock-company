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
public class ProductName {

    String name;

    public ProductName(String name) {
        if (name == null || name.isEmpty()) {
            throw new BusinessException(ErrorCode.MUST_NOT_NULL, "product name");
        }
        this.name = name;
    }

    public static ProductName of(String name) {
        return new ProductName(name);
    }
}
