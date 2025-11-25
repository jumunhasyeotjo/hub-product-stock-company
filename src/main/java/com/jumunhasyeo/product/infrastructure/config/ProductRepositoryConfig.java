package com.jumunhasyeo.product.infrastructure.config;

import com.jumunhasyeo.product.domain.repository.ProductRepository;
import com.jumunhasyeo.product.infrastructure.repository.JpaProductRepository;
import com.jumunhasyeo.product.infrastructure.repository.ProductRepositoryAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductRepositoryConfig {

    @Bean
    public ProductRepository productRepository(JpaProductRepository jpaProductRepository) {
        return new ProductRepositoryAdapter(jpaProductRepository);
    }
}
