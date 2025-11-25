package com.jumunhasyeo.product.infrastructure.repository;

import com.jumunhasyeo.product.application.command.SearchProductCommand;
import com.jumunhasyeo.product.application.dto.ProductRes;
import com.jumunhasyeo.product.domain.entity.Product;
import com.jumunhasyeo.product.domain.repository.ProductRepository;
import com.jumunhasyeo.product.domain.vo.ProductName;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepository {

    private final JpaProductRepository jpaProductRepository;

    @Override
    public Product save(Product product) {
        return jpaProductRepository.save(product);
    }

    @Override
    public Boolean existsByName(ProductName name) {
        return jpaProductRepository.existsByName(name);
    }

    @Override
    public Optional<Product> findById(UUID id) {
        return jpaProductRepository.findById(id);
    }

    @Override
    public Page<ProductRes> searchProduct(SearchProductCommand req) {
        return jpaProductRepository.searchProduct(req);
    }

    @Override
    public Boolean existsById(UUID id) {
        return jpaProductRepository.existsById(id);
    }

    @Override
    public Boolean existsByNameAndIdNot(ProductName name, UUID id) {
        return jpaProductRepository.existsByNameAndId(name, id);
    }
}
