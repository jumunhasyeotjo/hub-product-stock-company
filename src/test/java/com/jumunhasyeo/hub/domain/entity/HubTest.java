package com.jumunhasyeo.hub.domain.entity;

import com.jumunhasyeo.common.exception.BusinessException;
import com.jumunhasyeo.common.exception.ErrorCode;
import com.jumunhasyeo.hub.domain.vo.Address;
import com.jumunhasyeo.hub.domain.vo.Coordinate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class HubTest {
    @Test
    @DisplayName("hub를 생성할 수 있다.")
    public void of_hub_success() {
        //given
        Address address = Address.of("주소", Coordinate.of(12.7, 12.7));
        String name = "홍길동";
        //when
        Hub hub = Hub.of(name, address);
        //then
        assertThat(hub.getAddress()).isEqualTo(address);
        assertThat(hub.getName()).isEqualTo(name);
    }

    @Test
    @DisplayName("hub를 생성할 떄 name == null일 경우 예외 반환")
    public void of_NameIsNull_ShouldThrowException() {
        //given
        String name = null;
        Address address = Address.of("주소", Coordinate.of(12.7, 12.7));
        //when & then
        assertValidationFailed(() -> Hub.of(name, address));
    }

    @Test
    @DisplayName("hub를 생성할 떄 address == null일 경우 예외 반환")
    public void of_AddressIsNull_ShouldThrowException() {
        //given
        Address address = null;
        String name = "name";
        //when & then
        assertValidationFailed(() -> Hub.of(name, address));
    }

    @Test
    @DisplayName("hub를 수정할 수 있다.")
    public void update_hub_success() {
        //given
        Hub hub = createHub();
        Address changedAddress = Address.of("변경주소", Coordinate.of(1.1, 1.1));
        //when
        hub.update("변경이름", changedAddress);
        //then
        assertThat(hub.getName()).isEqualTo("변경이름");
        assertThat(hub.getAddress()).isEqualTo(changedAddress);
    }

    @Test
    @DisplayName("hub를 수정할 때 name == null일 경우 예외 반환")
    public void update_NameIsNull_ShouldThrowException() {
        //given
        String name = null;
        Address address = Address.of("주소", Coordinate.of(12.7, 12.7));
        Hub hub = createHub();
        //when & then
        assertValidationFailed(() -> hub.update(name, address));
    }

    @Test
    @DisplayName("hub를 삭제할 수 있다.")
    public void delete_hub_success() {
        //given
        Hub hub = createHub();
        Long userId = 1L;
        //when
        hub.delete(userId);
        //then
        assertThat(hub.getDeletedBy()).isEqualTo(userId);
        assertThat(hub.getDeletedAt()).isNotNull();
    }

    @Test
    @DisplayName("hub를 삭제할 때 userId == null일 경우 예외 반환")
    public void delete_NameIsNull_ShouldThrowException() {
        //given
        Hub hub = createHub();
        Long userId = null;
        //when
        BusinessException businessException = assertThrows(
                BusinessException.class, () -> hub.delete(userId)
        );
        //then
        assertThat(businessException.getErrorCode()).isEqualTo(ErrorCode.MUST_NOT_NULL);
        assertThat(businessException.getMessage()).contains("userId는(은) null일 수 없습니다.");
    }

    private static Hub createHub() {
        return Hub.builder()
                .name("송파 허브")
                .address(Address.of("street", Coordinate.of(12.6, 12.6)))
                .build();
    }

    private static void assertValidationFailed(Executable executable) {
        BusinessException businessException = assertThrows(
                BusinessException.class, executable
        );

        assertThat(businessException.getErrorCode()).isEqualTo(ErrorCode.CREATE_VALIDATE_EXCEPTION);
    }
}