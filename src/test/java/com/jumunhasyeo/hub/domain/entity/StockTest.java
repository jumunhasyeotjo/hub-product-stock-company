package com.jumunhasyeo.hub.domain.entity;

import com.jumunhasyeo.hub.exception.BusinessException;
import com.jumunhasyeo.hub.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StockTest {

    @Test
    @DisplayName("Stock을 Of로 생성할 수 있다.")
    public void of_stock_success() {
        Stock.of(1L, 100);
    }

    @Test
    @DisplayName("Stock을 Of로 생성할 때 productId == null일 경우 예외 반환")
    public void of_productIdIsNull_ShouldThrowException() {
        Long productid = null;
        BusinessException businessException = assertThrows(
                BusinessException.class, () -> Stock.of(productid, 100)
        );
        assertThat(businessException.getErrorCode()).isEqualTo(ErrorCode.CREATE_VALIDATE_EXCEPTION);
        assertThat(businessException.getMessage()).contains("객체 생성에 실패했습니다");
    }

    @Test
    @DisplayName("Stock을 Of로 생성할 때 quantity == null일 경우 예외 반환")
    public void of_quantityIsNull_ShouldThrowException() {
        Integer quantity = null;
        BusinessException businessException = assertThrows(
                BusinessException.class, () -> Stock.of(1L, quantity)
        );
        assertThat(businessException.getErrorCode()).isEqualTo(ErrorCode.CREATE_VALIDATE_EXCEPTION);
        assertThat(businessException.getMessage()).contains("객체 생성에 실패했습니다");
    }

    @Test
    @DisplayName("Stock을 Of로 생성할 때 quantity < 0일 경우 예외 반환")
    public void of_quantityLessThanZero_ShouldThrowException() {
        Integer quantity = -1;
        BusinessException businessException = assertThrows(
                BusinessException.class, () -> Stock.of(1L, quantity)
        );
        assertThat(businessException.getErrorCode()).isEqualTo(ErrorCode.CREATE_VALIDATE_EXCEPTION);
        assertThat(businessException.getMessage()).contains("객체 생성에 실패했습니다");
    }

    @Test
    @DisplayName("재고를 감소시킬 수 있다. ")
    public void stock_decrease_success() {
        Stock stock = Stock.of(1L, 100);
        stock.decrease(50);
        assertThat(stock.getQuantity()).isEqualTo(50);
    }

    @Test
    @DisplayName("재고 감소할 때 '감소 값 <= 0'일 경우 예외 반환")
    public void decrease_LessThanZero_ShouldThrowException() {
        Stock stock = Stock.of(1L, 100);
        BusinessException businessException = assertThrows(
                BusinessException.class, () -> stock.decrease(-1)
        );
        assertThat(businessException.getErrorCode()).isEqualTo(ErrorCode.STOCK_VALID);
        assertThat(businessException.getMessage()).contains("감소 수량은 0보다 커야 합니다.");
    }

    @Test
    @DisplayName("재고 감소할 때 '감소 값 >= 재고 값' 일 경우 예외 반환")
    public void decrease_LessThanQuantity_ShouldThrowException() {
        Stock stock = Stock.of(1L, 100);
        BusinessException businessException = assertThrows(
                BusinessException.class, () -> stock.decrease(5000)
        );
        assertThat(businessException.getErrorCode()).isEqualTo(ErrorCode.STOCK_VALID);
        assertThat(businessException.getMessage()).contains("재고가 부족합니다.");
    }

    @Test
    @DisplayName("같은 상품에 대한 재고인지 확인할 수 있다.")
    public void isSameProduct() {
        Stock stock = Stock.of(1L, 100);
        boolean isSameProduct = stock.isSameProduct(stock.getProductId());
        assertThat(isSameProduct).isTrue();
    }

    @Test
    @DisplayName("재고를 증가시킬 수 있다.")
    public void increase_stock_success() {
        Stock stock = Stock.of(1L, 100);
        stock.increase(50);
        assertThat(stock.getQuantity()).isEqualTo(150);
    }

    @Test
    @DisplayName("재고 증가할 때 '증가 값 <= 0'일 경우 예외 반환")
    public void increase_LessThanZero_ShouldThrowException() {
        Stock stock = Stock.of(1L, 100);
        BusinessException businessException = assertThrows(
                BusinessException.class, () -> stock.increase(-1)
        );
        assertThat(businessException.getErrorCode()).isEqualTo(ErrorCode.STOCK_VALID);
        assertThat(businessException.getMessage()).contains("증가 수량은 0보다 커야 합니다.");
    }
}