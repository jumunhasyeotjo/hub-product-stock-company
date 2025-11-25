package com.jumunhasyeo.product.domain.repository;

import com.jumunhasyeo.product.application.command.SearchProductCommand;
import com.jumunhasyeo.product.application.dto.ProductRes;
import com.jumunhasyeo.product.domain.entity.Product;
import com.jumunhasyeo.product.domain.vo.ProductName;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;


public interface ProductRepository {
    Product save(Product product);
    Boolean existsByName(ProductName name);
    Optional<Product> findById(UUID id);
    Page<ProductRes> searchProduct(SearchProductCommand condition);
    Boolean existsById(UUID productId);
    Boolean existsByNameAndIdNot(ProductName name, UUID id);
}
