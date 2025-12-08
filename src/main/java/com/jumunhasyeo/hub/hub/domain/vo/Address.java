package com.jumunhasyeo.hub.hub.domain.vo;


import com.jumunhasyeo.common.exception.BusinessException;
import com.jumunhasyeo.common.exception.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class Address {

    @Column(name = "address")
    private String street;

    @Embedded
    private Coordinate coordinate;

    public static Address of(String street, Coordinate coordinate) {
        if (StringUtils.isAllBlank(street) || coordinate == null)
            throw new BusinessException(ErrorCode.CREATE_VALIDATE_EXCEPTION);

        return new Address(street, coordinate);
    }
}