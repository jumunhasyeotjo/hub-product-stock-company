package com.jumunhasyeo.product.infrastructure.repository;

import com.jumunhasyeo.product.domain.entity.Product;
import com.jumunhasyeo.product.domain.vo.ProductName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaProductRepository extends JpaRepository<Product, UUID>, ProductRepositoryCustom {
    Boolean existsByName(ProductName name);
    Boolean existsByNameAndId(ProductName name, UUID id);
}
