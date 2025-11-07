package com.jumunhasyeo.hub.domain.entity;

import com.jumunhasyeo.hub.domain.vo.Address;
import com.jumunhasyeo.hub.domain.vo.Coordinate;
import com.jumunhasyeo.hub.exception.BusinessException;
import com.jumunhasyeo.hub.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class HubTest {

    private static Hub createHub() {
        return Hub.builder()
                .name("송파 허브")
                .address(Address.of("street", Coordinate.of(12.6, 12.6)))
                .stockList(new ArrayList<>())
                .build();
    }

    private static void assertValidationFailed(Executable executable) {
        BusinessException businessException = assertThrows(
                BusinessException.class, executable
        );

        assertThat(businessException.getErrorCode()).isEqualTo(ErrorCode.CREATE_VALIDATE_EXCEPTION);
    }

    @Test
    @DisplayName("hub를 생성할 수 있다.")
    public void of_hub_success() {
        Address address = Address.of("주소", Coordinate.of(12.7, 12.7));
        String name = "홍길동";

        Hub hub = Hub.of(name, address);

        assertThat(hub.getAddress()).isEqualTo(address);
        assertThat(hub.getName()).isEqualTo(name);
    }

    @Test
    @DisplayName("hub를 생성할 떄 name == null일 경우 예외 반환")
    public void of_NameIsNull_ShouldThrowException() {
        String name = null;
        Address address = Address.of("주소", Coordinate.of(12.7, 12.7));
        assertValidationFailed(() -> Hub.of(name, address));
    }

    @Test
    @DisplayName("hub를 생성할 떄 address == null일 경우 예외 반환")
    public void of_AddressIsNull_ShouldThrowException() {
        Address address = null;
        String name = "name";
        assertValidationFailed(() -> Hub.of(name, address));
    }

    @Test
    @DisplayName("hub를 수정할 수 있다.")
    public void update_hub_success() {
        Hub hub = createHub();

        Address changedAddress = Address.of("변경주소", Coordinate.of(1.1, 1.1));
        hub.update("변경이름", changedAddress);

        assertThat(hub.getName()).isEqualTo("변경이름");
        assertThat(hub.getAddress()).isEqualTo(changedAddress);
    }

    @Test
    @DisplayName("hub를 수정할 때 name == null일 경우 예외 반환")
    public void update_NameIsNull_ShouldThrowException() {
        String name = null;
        Address address = Address.of("주소", Coordinate.of(12.7, 12.7));
        Hub hub = createHub();
        assertValidationFailed(() -> hub.update(name, address));
    }

    @Test
    @DisplayName("hub를 삭제할 수 있다.")
    public void delete_hub_success() {
        Hub hub = createHub();
        Long userId = 1L;

        hub.delete(userId);

        assertThat(hub.getDeletedBy()).isEqualTo(userId);
        assertThat(hub.getDeletedAt()).isNotNull();
    }

    @Test
    @DisplayName("hub를 삭제할 때 userId == null일 경우 예외 반환")
    public void delete_NameIsNull_ShouldThrowException() {
        Hub hub = createHub();
        Long userId = null;
        BusinessException businessException = assertThrows(
                BusinessException.class, () -> hub.delete(userId)
        );

        assertThat(businessException.getErrorCode()).isEqualTo(ErrorCode.MUST_NOT_NULL);
        assertThat(businessException.getMessage()).contains("userId는(은) null일 수 없습니다.");
    }

    @Test
    @DisplayName("hub에 상품 재고를 추가할 수 있다.")
    public void registerStock_hub_success() {
        Hub hub = createHub();
        Stock stock = createStock(1L, 100);

        hub.addProductStock(stock);

        assertThat(hub.getStockList().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("hub에 상품 재고를 추가할 때 Stock == null일 경우 예외 반환")
    public void registerStock_StockIsNull_ShouldThrowException() {
        Hub hub = createHub();
        Stock stock = null;

        BusinessException businessException = assertThrows(
                BusinessException.class, () -> hub.addProductStock(stock)
        );

        assertThat(businessException.getErrorCode()).isEqualTo(ErrorCode.MUST_NOT_NULL);
        assertThat(businessException.getMessage()).contains("stock는(은) null일 수 없습니다.");
    }

    @Test
    @DisplayName("hub에서 재고를 감소시킬 수 있다.")
    public void stockDecrease_hub_success() {
        Hub hub = createHub();
        Long productId = 1L;
        Stock stock = createStock(productId, 100);
        hub.addProductStock(stock);

        hub.stockDecrease(productId, 10);

        assertThat(hub.getStock(productId)).isPresent();
        assertThat(hub.getStock(productId).get().getQuantity()).isEqualTo(90);
    }

    @Test
    @DisplayName("hub에서 재고를 증가시킬 수 있다.")
    public void stockIncrease_hub_success() {
        Hub hub = createHub();
        Long productId = 1L;
        Stock stock = createStock(productId, 100);
        hub.addProductStock(stock);

        hub.stockIncrease(productId, 10);

        assertThat(hub.getStock(productId)).isPresent();
        assertThat(hub.getStock(productId).get().getQuantity()).isEqualTo(110);
    }

    @Test
    @DisplayName("hub에서 재고를 조회할 수 있다.")
    public void getStock_hub_success() {
        Hub hub = createHub();
        Long productId = 1L;
        Stock stock = createStock(productId, 100);
        hub.addProductStock(stock);

        Optional<Stock> stockOpt = hub.getStock(productId);

        assertThat(stockOpt).isPresent();
        assertThat(stockOpt.get().getProductId()).isEqualTo(productId);
        assertThat(stockOpt.get().getQuantity()).isEqualTo(100);
    }

    private Stock createStock(Long productId, Integer quantity) {
        return Stock.builder()
                .id(UUID.randomUUID())
                .productId(productId)
                .quantity(quantity)
                .build();
    }
}