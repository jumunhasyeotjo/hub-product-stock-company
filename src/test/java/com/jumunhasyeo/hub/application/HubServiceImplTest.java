package com.jumunhasyeo.hub.application;


import com.jumunhasyeo.hub.application.command.CreateHubCommand;
import com.jumunhasyeo.hub.application.command.DeleteHubCommand;
import com.jumunhasyeo.hub.application.command.UpdateHubCommand;
import com.jumunhasyeo.hub.application.dto.response.HubRes;
import com.jumunhasyeo.hub.domain.entity.Hub;
import com.jumunhasyeo.hub.domain.event.HubCreatedEvent;
import com.jumunhasyeo.hub.domain.repository.HubRepository;
import com.jumunhasyeo.hub.domain.repository.HubRepositoryCustom;
import com.jumunhasyeo.hub.domain.vo.Address;
import com.jumunhasyeo.hub.domain.vo.Coordinate;
import com.jumunhasyeo.hub.exception.BusinessException;
import com.jumunhasyeo.hub.exception.ErrorCode;
import com.jumunhasyeo.hub.presentation.dto.HubSearchCondition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HubServiceImplTest {

    @Mock
    private HubRepository hubRepository;
    @Mock
    private HubRepositoryCustom hubRepositoryCustom;
    @Mock
    private ApplicationEventPublisher eventPublisher;
    @InjectMocks
    private HubServiceImpl hubService;

    private static Hub createHub(UUID hubId) {
        return Hub.builder()
                .hubId(hubId)
                .name("송파 허브")
                .address(Address.of("street", Coordinate.of(12.6, 12.6)))
                .build();
    }

    @Test
    @DisplayName("hub를 생성할 수 있다.")
    public void create_hub_success() {
        //given
        CreateHubCommand command = new CreateHubCommand("이름", "주소", 12.7, 12.7);
        //when
        HubRes hubRes = hubService.create(command);
        //then
        assertThat(hubRes.name()).isEqualTo(command.name());
        assertThat(hubRes.address()).isEqualTo(command.address());
        assertThat(hubRes.latitude()).isEqualTo(command.latitude());
        assertThat(hubRes.longitude()).isEqualTo(command.longitude());
    }

    @Test
    @DisplayName("hub 생성 시 HubCreatedEvent가 발행된다")
    void createHub_ShouldPublishHubCreatedEvent() {
        // given
        CreateHubCommand command = new CreateHubCommand("이름", "주소", 12.7, 12.7);
        HubRes hubRes = hubService.create(command);
        //when
        ArgumentCaptor<HubCreatedEvent> eventCaptor =
                ArgumentCaptor.forClass(HubCreatedEvent.class);
        //then
        verify(eventPublisher).publishEvent(eventCaptor.capture());
    }

    @Test
    @DisplayName("hub를 수정할 수 있다.")
    public void update_hub_success() {
        //given
        UUID hubId = UUID.randomUUID();
        Hub savedHub = createHub(hubId);
        UpdateHubCommand command = new UpdateHubCommand(hubId, "이름", "주소", 12.7, 12.7);
        when(hubRepository.findById(any())).thenReturn(Optional.of(savedHub));
        //when
        HubRes hubRes = hubService.update(command);
        //then
        assertThat(hubRes.id()).isEqualTo(command.hubId());
        assertThat(hubRes.name()).isEqualTo(command.name());
        assertThat(hubRes.address()).isEqualTo(command.address());
        assertThat(hubRes.latitude()).isEqualTo(command.latitude());
        assertThat(hubRes.longitude()).isEqualTo(command.longitude());
    }

    @Test
    @DisplayName("hub를 수정할 때 없는 허브를 조회하지 못하면 예외반환")
    public void update_hubIdIsIncorrect_ShouldThrowException() {
        //given
        UUID incorrectHubId = UUID.randomUUID();
        UpdateHubCommand command = new UpdateHubCommand(incorrectHubId, "이름", "주소", 12.7, 12.7);
        when(hubRepository.findById(any())).thenReturn(Optional.empty());

        //when
        BusinessException businessException = assertThrows(
                BusinessException.class, () -> hubService.update(command)
        );

        //then
        assertThat(businessException.getErrorCode()).isEqualTo(ErrorCode.NOT_FOUND_EXCEPTION);
    }

    @Test
    @DisplayName("hub를 ID로 단건 조회할 수 있다.")
    public void getById_Hub_Success() {
        Hub hub = createHub(UUID.randomUUID());
        when(hubRepository.findById(any())).thenReturn(Optional.of(hub));

        //when
        HubRes hubRes = hubService.getById(hub.getHubId());

        //then
        assertThat(hubRes.id()).isEqualTo(hub.getHubId());
    }

    @Test
    @DisplayName("hub를 단건 조회할 때 조회하지 못하면 예외 반환")
    public void getById_hubIdIsIncorrect_ShouldThrowException() {
        UUID incorrectHubId = UUID.randomUUID();
        when(hubRepository.findById(any())).thenReturn(Optional.empty());

        //when
        BusinessException businessException = assertThrows(
                BusinessException.class, () -> hubService.getById(incorrectHubId)
        );

        //then
        assertThat(businessException.getErrorCode()).isEqualTo(ErrorCode.NOT_FOUND_EXCEPTION);
    }

    @Test
    @DisplayName("hub를 검색 조회할 수 있다.")
    public void search_Hub_Success() {
        Hub hub = createHub(UUID.randomUUID());
        HubSearchCondition condition = HubSearchCondition.builder().name(hub.getName()).build();
        Pageable pageRequest = PageRequest.of(0, 10);
        Page<HubRes> page = new PageImpl<>(List.of(HubRes.from(hub)), PageRequest.of(0, 10), 0);
        when(hubRepositoryCustom.searchHubsByCondition(any(), any())).thenReturn(page);

        //when
        Page<HubRes> hubRes = hubService.search(condition, pageRequest);

        //then
        assertThat(hubRes.getContent().get(0).id()).isEqualTo(hub.getHubId());
    }

    @Test
    @DisplayName("hub를 논리 삭제할 수 있다.")
    public void delete_Hub_Success() {
        //given
        Hub hub = createHub(UUID.randomUUID());
        DeleteHubCommand command = new DeleteHubCommand(hub.getHubId(), 1L);
        when(hubRepository.findById(any())).thenReturn(Optional.of(hub));
        //when
        UUID deletedId = hubService.delete(command);

        //then
        assertThat(deletedId).isEqualTo(hub.getHubId());
    }
}