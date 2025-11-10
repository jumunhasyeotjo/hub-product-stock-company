package com.jumunhasyeo.hub.application.dto.response;

import com.jumunhasyeo.hub.domain.entity.Hub;
import com.jumunhasyeo.hub.domain.vo.Address;
import com.jumunhasyeo.hub.domain.vo.Coordinate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class HubResTest {

    @Test
    @DisplayName("Hub를 이용해 HubRes를 생성할 수 있다")
    public void create_hubRes_success() {
        Coordinate coordinate = Coordinate.of(12.7, 12.7);
        Address address = Address.of("street", coordinate);
        Hub hub = Hub.builder()
                .hubId(UUID.randomUUID())
                .name("송파 허브")
                .address(address)
                .build();

        HubRes hubRes = HubRes.from(hub);

        assertThat(hubRes.id()).isEqualTo(hub.getHubId());
        assertThat(hubRes.name()).isEqualTo(hub.getName());
        assertThat(hubRes.address()).isEqualTo(address.getStreet());
        assertThat(hubRes.longitude()).isEqualTo(coordinate.getLongitude());
        assertThat(hubRes.latitude()).isEqualTo(coordinate.getLatitude());
    }
}