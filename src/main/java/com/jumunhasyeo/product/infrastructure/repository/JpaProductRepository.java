package com.jumunhasyeo.product.infrastructure.repository;

import com.jumunhasyeo.product.domain.entity.Product;
import com.jumunhasyeo.product.domain.vo.ProductName;
import com.jumunhasyeo.product.presentation.dto.res.OrderProductRes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface JpaProductRepository extends JpaRepository<Product, UUID>, ProductRepositoryCustom {
    Boolean existsByName(ProductName name);
    Boolean existsByNameAndId(ProductName name, UUID id);

    @Query("SELECT new com.jumunhasyeo.product.presentation.dto.res.OrderProductRes(p.id, p.companyId.companyId, p.name.name, p.price.price) " +
            "FROM Product p " +
            "WHERE p.id IN :ids")
    List<OrderProductRes> findAllByIds(@Param("ids") List<UUID> ids);
}
