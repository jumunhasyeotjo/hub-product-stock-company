package com.jumunhasyeo.stock.infrastructure.external;

import com.jumunhasyeo.product.application.ProductService;
import com.jumunhasyeo.stock.application.service.ProductClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProductInternalCallClientImpl implements ProductClient {
    private final ProductService productService;

    @Override
    public boolean existProduct(UUID productId) {
        return productService.existsProduct(productId);
    }
}
