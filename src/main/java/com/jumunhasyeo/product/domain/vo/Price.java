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
public class Price {

    private int price;

    public Price(int price) {
        if (price <= 0) {
            throw new BusinessException(ErrorCode.PRODUCT_VALID);
        }
        this.price = price;
    }

    public static Price of(int price) {
        return new Price(price);
    }
}
