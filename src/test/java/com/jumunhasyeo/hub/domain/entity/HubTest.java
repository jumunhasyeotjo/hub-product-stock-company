package com.jumunhasyeo.hub.domain.entity;

import com.jumunhasyeo.common.exception.BusinessException;
import com.jumunhasyeo.common.exception.ErrorCode;
import com.jumunhasyeo.hub.hub.domain.entity.Hub;
import com.jumunhasyeo.hub.hub.domain.entity.HubRelation;
import com.jumunhasyeo.hub.hub.domain.entity.HubType;
import com.jumunhasyeo.hub.hub.domain.vo.Address;
import com.jumunhasyeo.hub.hub.domain.vo.Coordinate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class HubTest {
    @Test
    @DisplayName("hub를 생성할 수 있다.")
    public void createBranchHub_hub_success() {
        //given
        Address address = Address.of("주소", Coordinate.of(12.7, 12.7));
        String name = "홍길동";
        //when
        Hub hub = Hub.createBranchHub(name, address);
        //then
        assertThat(hub.getAddress()).isEqualTo(address);
        assertThat(hub.getName()).isEqualTo(name);
    }

    @Test
    @DisplayName("hub를 생성할 떄 name == null일 경우 예외 반환")
    public void createBranchHub_NameIsNull_ShouldThrowException() {
        //given
        String name = null;
        Address address = Address.of("주소", Coordinate.of(12.7, 12.7));
        //when & then
        assertValidationFailed(() -> Hub.createBranchHub(name, address));
    }

    @Test
    @DisplayName("hub를 생성할 떄 address == null일 경우 예외 반환")
    public void createBranchHub_AddressIsNull_ShouldThrowException() {
        //given
        Address address = null;
        String name = "name";
        //when & then
        assertValidationFailed(() -> Hub.createBranchHub(name, address));
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
                .hubType(HubType.BRANCH)
                .address(Address.of("street", Coordinate.of(12.6, 12.6)))
                .build();
    }

    private static Hub createCenterHub() {
        return Hub.builder()
                .name("서울 중앙 허브")
                .hubType(HubType.CENTER)
                .centerHubRelations(new HashSet<>())
                .branchHubRelations(new HashSet<>())
                .address(Address.of("서울시 송파구", Coordinate.of(37.5, 127.0)))
                .build();
    }

    private static Hub createBranchHubEntity() {
        return Hub.builder()
                .name("강남 지점")
                .hubType(HubType.BRANCH)
                .centerHubRelations(new HashSet<>())
                .branchHubRelations(new HashSet<>())
                .address(Address.of("강남구", Coordinate.of(37.4, 127.1)))
                .build();
    }

    private static void assertValidationFailed(Executable executable) {
        BusinessException businessException = assertThrows(
                BusinessException.class, executable
        );

        assertThat(businessException.getErrorCode()).isEqualTo(ErrorCode.CREATE_VALIDATE_EXCEPTION);
    }

    // ===================== HubType 관련 테스트 =====================

    @Test
    @DisplayName("CENTER 타입 허브를 생성할 수 있다.")
    public void of_CenterHub_success() {
        //given
        Address address = Address.of("서울시 송파구", Coordinate.of(37.5, 127.0));
        String name = "서울 중앙 허브";
        //when
        Hub hub = Hub.of(name, address, HubType.CENTER);
        //then
        assertThat(hub.getName()).isEqualTo(name);
        assertThat(hub.getAddress()).isEqualTo(address);
        assertThat(hub.getHubType()).isEqualTo(HubType.CENTER);
        assertThat(hub.isCenterHub()).isTrue();
        assertThat(hub.isBranchHub()).isFalse();
    }

    @Test
    @DisplayName("BRANCH 타입 허브를 생성할 수 있다.")
    public void of_BranchHub_success() {
        //given
        Address address = Address.of("강남구", Coordinate.of(37.4, 127.1));
        String name = "강남 지점";
        //when
        Hub hub = Hub.of(name, address, HubType.BRANCH);
        //then
        assertThat(hub.getName()).isEqualTo(name);
        assertThat(hub.getAddress()).isEqualTo(address);
        assertThat(hub.getHubType()).isEqualTo(HubType.BRANCH);
        assertThat(hub.isBranchHub()).isTrue();
        assertThat(hub.isCenterHub()).isFalse();
    }

    // ===================== addCenterHub 관련 테스트 =====================

    @Test
    @DisplayName("지점 허브에 센터 허브를 추가할 수 있다.")
    public void addCenterHub_success() {
        //given
        Hub centerHub = createCenterHub();
        Hub branchHub = createBranchHubEntity();
        //when
        HubRelation relation = branchHub.addCenterHub(centerHub);
        //then
        assertThat(relation).isNotNull();
        assertThat(relation.getCenterHub()).isEqualTo(centerHub);
        assertThat(relation.getBranchHub()).isEqualTo(branchHub);
        assertThat(branchHub.getCenterHubs()).contains(centerHub);
        assertThat(centerHub.getBranchHubs()).contains(branchHub);
    }

    @Test
    @DisplayName("이미 등록된 센터 허브를 다시 추가하면 기존 관계를 반환한다.")
    public void addCenterHub_alreadyExists_returnExistingRelation() {
        //given
        Hub centerHub = createCenterHub();
        Hub branchHub = createBranchHubEntity();
        HubRelation firstRelation = branchHub.addCenterHub(centerHub);
        //when
        HubRelation secondRelation = branchHub.addCenterHub(centerHub);
        //then
        assertThat(secondRelation).isEqualTo(firstRelation);
        assertThat(branchHub.getCenterHubs()).hasSize(1);
    }

    @Test
    @DisplayName("센터 허브가 센터 허브를 추가하려고 하면 예외가 발생한다.")
    public void addCenterHub_centerHubCannotAddCenter_ShouldThrowException() {
        //given
        Hub centerHub1 = createCenterHub();
        Hub centerHub2 = Hub.builder()
                .name("부산 중앙 허브")
                .hubType(HubType.CENTER)
                .address(Address.of("부산시", Coordinate.of(35.1, 129.0)))
                .build();
        //when
        BusinessException exception = assertThrows(
                BusinessException.class, () -> centerHub1.addCenterHub(centerHub2)
        );
        //then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.BRANCH_HUB_ONLY_CAN_ADD_CENTER_HUB);
    }

    // ===================== delete 관련 테스트 (CENTER/BRANCH 분기) =====================

    @Test
    @DisplayName("지점 허브가 없는 센터 허브를 삭제할 수 있다.")
    public void delete_centerHubWithoutBranches_success() {
        //given
        Hub centerHub = createCenterHub();
        Long userId = 1L;
        //when
        centerHub.delete(userId);
        //then
        assertThat(centerHub.getDeletedBy()).isEqualTo(userId);
        assertThat(centerHub.getDeletedAt()).isNotNull();
    }

    @Test
    @DisplayName("지점 허브가 있는 센터 허브를 삭제하려고 하면 예외가 발생한다.")
    public void delete_centerHubWithBranches_ShouldThrowException() {
        //given
        Hub centerHub = createCenterHub();
        Hub branchHub = createBranchHubEntity();
        branchHub.addCenterHub(centerHub);
        Long userId = 1L;
        //when
        BusinessException exception = assertThrows(
                BusinessException.class, () -> centerHub.delete(userId)
        );
        //then
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.CANNOT_DELETE_CENTER_HUB_WITH_BRANCHES);
    }

    @Test
    @DisplayName("지점 허브는 검증 없이 삭제할 수 있다.")
    public void delete_branchHub_success() {
        //given
        Hub centerHub = createCenterHub();
        Hub branchHub = createBranchHubEntity();
        branchHub.addCenterHub(centerHub);
        Long userId = 1L;
        //when
        branchHub.delete(userId);
        //then
        assertThat(branchHub.getDeletedBy()).isEqualTo(userId);
        assertThat(branchHub.getDeletedAt()).isNotNull();
    }

    // ===================== getBranchHubs / getCenterHubs 테스트 =====================

    @Test
    @DisplayName("센터 허브에서 연결된 지점 허브 목록을 조회할 수 있다.")
    public void getBranchHubs_success() {
        //given
        Hub centerHub = createCenterHub();
        Hub branchHub1 = createBranchHubEntity();
        Hub branchHub2 = Hub.builder()
                .name("서초 지점")
                .centerHubRelations(new HashSet<>())
                .branchHubRelations(new HashSet<>())
                .hubType(HubType.BRANCH)
                .address(Address.of("서초구", Coordinate.of(37.3, 127.0)))
                .build();
        branchHub1.addCenterHub(centerHub);
        branchHub2.addCenterHub(centerHub);
        //when
        var branchHubs = centerHub.getBranchHubs();
        //then
        assertThat(branchHubs).hasSize(2);
        assertThat(branchHubs).contains(branchHub1, branchHub2);
    }

    @Test
    @DisplayName("지점 허브에서 연결된 센터 허브 목록을 조회할 수 있다.")
    public void getCenterHubs_success() {
        //given
        Hub centerHub = createCenterHub();
        Hub branchHub = createBranchHubEntity();
        branchHub.addCenterHub(centerHub);
        //when
        var centerHubs = branchHub.getCenterHubs();
        //then
        assertThat(centerHubs).hasSize(1);
        assertThat(centerHubs).contains(centerHub);
    }

    // ===================== getCoordinate 테스트 =====================

    @Test
    @DisplayName("허브의 좌표를 조회할 수 있다.")
    public void getCoordinate_success() {
        //given
        Coordinate coordinate = Coordinate.of(37.5, 127.0);
        Address address = Address.of("서울시", coordinate);
        Hub hub = Hub.of("테스트 허브", address, HubType.CENTER);
        //when
        Coordinate result = hub.getCoordinate();
        //then
        assertThat(result).isEqualTo(coordinate);
    }
}