package com.jumunhasyeo.stock.infrastructure.external;

import com.jumunhasyeo.stock.application.service.ProductClient;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProductClientImpl implements ProductClient {
    @Override
    public boolean existProduct(UUID productId) {
        return true; //TODO 외부 시스템 연동 로직 구현
    }
}
