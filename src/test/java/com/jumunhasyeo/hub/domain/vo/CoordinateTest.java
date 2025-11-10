package com.jumunhasyeo.hub.domain.vo;

import com.jumunhasyeo.hub.exception.BusinessException;
import com.jumunhasyeo.hub.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CoordinateTest {

    @Test
    @DisplayName("Coordinate를 생성할 수 있다.")
    public void of_coordinate_success() {
        Double latitude = 12.6;
        Double longitude = 1.26;
        Coordinate coordinate = Coordinate.of(latitude, longitude);

        assertThat(coordinate.getLatitude()).isEqualTo(latitude);
        assertThat(coordinate.getLongitude()).isEqualTo(longitude);
    }

    @Test
    @DisplayName("Coordinate를 생성할 떄 latitude == null일 경우 예외 반환")
    public void of_latitudeIsNull_ShouldThrowException() {
        Double latitude = null;
        Double longitude = 1.26;

        BusinessException businessException = assertThrows(
                BusinessException.class, () -> Coordinate.of(latitude, longitude)
        );

        assertThat(businessException.getErrorCode()).isEqualTo(ErrorCode.CREATE_VALIDATE_EXCEPTION);
    }

    @Test
    @DisplayName("Coordinate를 생성할 떄 longitude == null일 경우 예외 반환")
    public void of_longitudeIsNull_ShouldThrowException() {
        Double longitude = null;
        Double latitude = 1.26;

        BusinessException businessException = assertThrows(
                BusinessException.class, () -> Coordinate.of(latitude, longitude)
        );

        assertThat(businessException.getErrorCode()).isEqualTo(ErrorCode.CREATE_VALIDATE_EXCEPTION);
    }
}