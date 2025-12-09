package com.jumunhasyeo.product.domain.vo;

import com.jumunhasyeo.common.exception.BusinessException;
import com.jumunhasyeo.common.exception.ErrorCode;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CompanyId {

    private UUID companyId;

    public CompanyId(UUID companyId) {
        if (companyId == null) {
            throw new BusinessException(ErrorCode.PRODUCT_VALID_FAIL);
        }

        this.companyId = companyId;
    }

    public static CompanyId of(UUID companyId) {
        return new CompanyId(companyId);
    }
}
