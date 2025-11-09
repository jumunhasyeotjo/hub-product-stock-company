package com.jumunhasyeo.hub.domain.entity;

import com.jumunhasyeo.hub.exception.BusinessException;
import com.jumunhasyeo.hub.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StockTest {

    @Test
    @DisplayName("quantity >=0 인 경우 생성할 수 있다.")
    public void of_stock_success() {
        // given
        Hub hub = createHub();
        // when
        Stock stock = hub.registerNewStock(UUID.randomUUID(), 100);
        // then
        assertThat(stock.getQuantity()).isEqualTo(100);
    }

    @Test
    @DisplayName("Stock을 Of로 생성할 때 productId == null일 경우 예외 반환")
    public void of_productIdIsNull_ShouldThrowException() {
        // given
        UUID productid = null;
        Hub hub = Hub.builder().build();
        // when
        BusinessException businessException = assertThrows(
                BusinessException.class, () -> Stock.of(hub, productid, 100)
        );
        // then
        assertThat(businessException.getErrorCode()).isEqualTo(ErrorCode.CREATE_VALIDATE_EXCEPTION);
        assertThat(businessException.getMessage()).contains("객체 생성에 실패했습니다");
    }

    @Test
    @DisplayName("Stock을 Of로 생성할 때 quantity == null일 경우 예외 반환")
    public void of_quantityIsNull_ShouldThrowException() {
        // given
        Integer quantity = null;
        UUID productId = UUID.randomUUID();
        Hub hub = Hub.builder().build();
        // when
        BusinessException businessException = assertThrows(
                BusinessException.class, () -> Stock.of(hub, productId, quantity)
        );
        // then
        assertThat(businessException.getErrorCode()).isEqualTo(ErrorCode.CREATE_VALIDATE_EXCEPTION);
        assertThat(businessException.getMessage()).contains("객체 생성에 실패했습니다");
    }

    @Test
    @DisplayName("Stock을 Of로 생성할 때 quantity < 0일 경우 예외 반환")
    public void of_quantityLessThanZero_ShouldThrowException() {
        // given
        Integer quantity = -1;
        UUID productId = UUID.randomUUID();
        Hub hub = Hub.builder().build();
        // when
        BusinessException businessException = assertThrows(
                BusinessException.class, () -> Stock.of(hub, productId, quantity)
        );
        // then
        assertThat(businessException.getErrorCode()).isEqualTo(ErrorCode.CREATE_VALIDATE_EXCEPTION);
        assertThat(businessException.getMessage()).contains("객체 생성에 실패했습니다");
    }

    @Test
    @DisplayName("재고를 감소시킬 수 있다.")
    public void stock_decrease_success() {
        // given
        Stock stock = createStock(100);
        // when
        stock.decrease(50);
        // then
        assertThat(stock.getQuantity()).isEqualTo(50);
    }

    @Test
    @DisplayName("재고 감소할 때 '감소 값 <= 0'일 경우 예외 반환")
    public void decrease_LessThanZero_ShouldThrowException() {
        // given
        Stock stock = createStock(100);
        // when
        BusinessException businessException = assertThrows(
                BusinessException.class, () -> stock.decrease(-1)
        );
        // then
        assertThat(businessException.getErrorCode()).isEqualTo(ErrorCode.STOCK_VALID);
        assertThat(businessException.getMessage()).contains("감소 수량은 0보다 커야 합니다.");
    }

    @Test
    @DisplayName("재고 감소할 때 '감소 값 >= 재고 값' 일 경우 예외 반환")
    public void decrease_LessThanQuantity_ShouldThrowException() {
        // given
        Stock stock = createStock(100);
        // when
        BusinessException businessException = assertThrows(
                BusinessException.class, () -> stock.decrease(5000)
        );
        // then
        assertThat(businessException.getErrorCode()).isEqualTo(ErrorCode.STOCK_VALID);
        assertThat(businessException.getMessage()).contains("재고가 부족합니다.");
    }

    @Test
    @DisplayName("같은 상품에 대한 재고인지 확인할 수 있다.")
    public void isSameProduct() {
        // given
        Stock stock = createStock(100);
        // when
        boolean isSameProduct = stock.isSameProduct(stock.getProductId());
        // then
        assertThat(isSameProduct).isTrue();
    }

    @Test
    @DisplayName("재고를 증가시킬 수 있다.")
    public void increase_stock_success() {
        // given
        Stock stock = createStock(100);
        // when
        stock.increase(50);
        // then
        assertThat(stock.getQuantity()).isEqualTo(150);
    }

    @Test
    @DisplayName("재고 증가할 때 '증가 값 <= 0'일 경우 예외 반환")
    public void increase_LessThanZero_ShouldThrowException() {
        // given
        Stock stock = createStock(100);
        // when
        BusinessException businessException = assertThrows(
                BusinessException.class, () -> stock.increase(-1)
        );
        // then
        assertThat(businessException.getErrorCode()).isEqualTo(ErrorCode.STOCK_VALID);
        assertThat(businessException.getMessage()).contains("증가 수량은 0보다 커야 합니다.");
    }

    @Test
    @DisplayName("재고를 정확히 0까지 감소시킬 수 있다")
    public void decrease_ToZero_success() {
        // given
        Stock stock = createStock(100);
        // when
        stock.decrease(100);
        // then
        assertThat(stock.getQuantity()).isEqualTo(0);
    }

    @Test
    @DisplayName("재고 0으로 생성 가능")
    public void of_quantityIsZero_success() {
        // given
        Hub hub = createHub();
        // when
        Stock stock = hub.registerNewStock(UUID.randomUUID(), 0);
        // then
        assertThat(stock.getQuantity()).isEqualTo(0);
    }

    // 헬퍼 메서드
    private static Stock createStock(int quantity) {
        Hub hub = createHub();
        return hub.registerNewStock(UUID.randomUUID(), quantity);
    }

    private static Hub createHub() {
        return Hub.builder().stockList(new ArrayList<>()).build();
    }
}