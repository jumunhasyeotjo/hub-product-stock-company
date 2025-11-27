package com.jumunhasyeo.product.application;

import com.jumunhasyeo.product.application.command.*;
import com.jumunhasyeo.product.application.dto.ProductRes;
import com.jumunhasyeo.product.presentation.dto.res.OrderProductRes;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    ProductRes createProduct(CreateProductCommand command);
    ProductRes updateProduct(UpdateProductCommand command);
    void deleteProduct(DeleteProductCommand command);
    ProductRes getProduct(GetProductCommand command);
    Page<ProductRes> searchProduct(SearchProductCommand command);
    Boolean existsProduct(UUID productId);
    List<OrderProductRes> searchOrderProduct(List<UUID> orderProductIds);
}
