package com.jumunhasyeo.hubRoute.infrastructure.external;

import com.jumunhasyeo.common.exception.BusinessException;
import com.jumunhasyeo.common.exception.ErrorCode;
import com.jumunhasyeo.hub.domain.vo.Coordinate;
import com.jumunhasyeo.hubRoute.application.dto.response.RouteWeightRes;
import com.jumunhasyeo.hubRoute.infrastructure.external.client.map.KakaoMobilityClient;
import com.jumunhasyeo.hubRoute.infrastructure.response.KakaoRouteResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KakaoWeightRouteApiServiceImplTest {

    @Mock
    private KakaoMobilityClient kakaoMobilityClient;
    @InjectMocks
    private KakaoWeightRouteApiServiceImpl kakaoWeightRouteApiService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(kakaoWeightRouteApiService, "apiKey", "test-key");
    }

    @Test
    @DisplayName("경로 정보를 조회할 수 있다.")
    void getRouteInfo_success() {
        //given
        when(kakaoMobilityClient.getDirections(any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(createMockResponse(10000, 600, 0));
        //when
        RouteWeightRes result = kakaoWeightRouteApiService.getRouteInfo(
                Coordinate.of(37.5, 127.0), Coordinate.of(37.4, 127.1));
        //then
        assertThat(result.durationMinutes()).isEqualTo(10);
    }

    @Test
    @DisplayName("API 응답이 비어있으면 예외가 발생한다.")
    void getRouteInfo_emptyRoutes_throwsException() {
        //given
        when(kakaoMobilityClient.getDirections(any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(new KakaoRouteResponse());
        //when
        BusinessException exception = assertThrows(BusinessException.class,
                () -> kakaoWeightRouteApiService.getRouteInfo(Coordinate.of(37.5, 127.0), Coordinate.of(37.4, 127.1)));
        //then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.MAP_API_EXCEPTION);
    }

    @Test
    @DisplayName("API 호출 실패 시 예외가 발생한다.")
    void getRouteInfo_apiException_throwsBusinessException() {
        //given
        when(kakaoMobilityClient.getDirections(any(), any(), any(), any(), any(), any(), any(), any(), any()))
                .thenThrow(new RuntimeException("API 실패"));
        //when
        BusinessException exception = assertThrows(BusinessException.class,
                () -> kakaoWeightRouteApiService.getRouteInfo(Coordinate.of(37.5, 127.0), Coordinate.of(37.4, 127.1)));
        //then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.MAP_API_EXCEPTION);
    }

    private KakaoRouteResponse createMockResponse(int distanceMeters, int durationSeconds, int resultCode) {
        KakaoRouteResponse response = new KakaoRouteResponse();
        KakaoRouteResponse.Route route = new KakaoRouteResponse.Route();
        KakaoRouteResponse.Summary summary = new KakaoRouteResponse.Summary();
        ReflectionTestUtils.setField(summary, "distance", distanceMeters);
        ReflectionTestUtils.setField(summary, "duration", durationSeconds);
        ReflectionTestUtils.setField(route, "resultCode", resultCode);
        ReflectionTestUtils.setField(route, "summary", summary);
        ReflectionTestUtils.setField(response, "routes", List.of(route));
        return response;
    }
}
