package com.jumunhasyeo.hub.hub.domain.vo;

import com.jumunhasyeo.common.exception.BusinessException;
import com.jumunhasyeo.common.exception.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class Coordinate {
    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    public static Coordinate of(Double latitude, Double longitude) {
        if (latitude == null || longitude == null)
            throw new BusinessException(ErrorCode.CREATE_VALIDATE_EXCEPTION);

        return new Coordinate(latitude, longitude);
    }
}