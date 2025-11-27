package com.jumunhasyeo.product.infrastructure.repository;

import com.jumunhasyeo.product.application.command.SearchProductCommand;
import com.jumunhasyeo.product.application.dto.ProductRes;
import org.springframework.data.domain.Page;

public interface ProductRepositoryCustom {
    Page<ProductRes> searchProduct(SearchProductCommand command);
}
