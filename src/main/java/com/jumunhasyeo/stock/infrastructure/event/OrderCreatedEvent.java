package com.jumunhasyeo.stock.infrastructure.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@Getter
@NoArgsConstructor
public class OrderCreatedEvent {
    private UUID orderId;
    private LocalDateTime orderCreatedTime;
    private String requestMessage;
    private UUID receiverCompanyId;
    private LocalDateTime occurredAt;
    private List<VendingOrder> vendingOrders;

    public OrderCreatedEvent(UUID orderId,
                             LocalDateTime orderCreatedTime,
                             String requestMessage,
                             UUID receiverCompanyId,
                             List<VendingOrder> vendingOrders) {
        this.orderId = orderId;
        this.orderCreatedTime = orderCreatedTime;
        this.requestMessage = requestMessage;
        this.receiverCompanyId = receiverCompanyId;
        this.vendingOrders = vendingOrders;
        this.occurredAt = LocalDateTime.now();
    }

    // 이벤트 내부에서 사용할 VendingOrder 구조
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class VendingOrder {
        private UUID vendingOrderId;
        private UUID supplierCompanyId;
        private String productInfo;
    }
}
