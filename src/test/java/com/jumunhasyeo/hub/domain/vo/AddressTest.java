package com.jumunhasyeo.hub.domain.vo;

import com.jumunhasyeo.hub.hub.domain.vo.Address;
import com.jumunhasyeo.hub.hub.domain.vo.Coordinate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AddressTest {
    @Test
    @DisplayName("address를 of로 생성할 수 있다.")
    public void of_create_success() {
        Address.of("도로명 주소", Coordinate.of(12.7, 12.7));
    }
}