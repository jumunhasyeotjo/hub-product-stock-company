package com.jumunhasyeo.product.domain.entity;


import com.jumunhasyeo.common.BaseEntity;
import com.jumunhasyeo.product.domain.vo.CompanyId;
import com.jumunhasyeo.product.domain.vo.Price;
import com.jumunhasyeo.product.domain.vo.ProductDescription;
import com.jumunhasyeo.product.domain.vo.ProductName;
import com.jumunhasyeo.common.exception.BusinessException;
import com.jumunhasyeo.common.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_product")
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "company_id"))
    private CompanyId companyId;

    @Embedded
    @AttributeOverride(name = "name", column = @Column(name = "name"))
    private ProductName name;

    @Embedded
    @AttributeOverride(name = "description", column = @Column(name = "description"))
    private ProductDescription description;

    @Embedded
    @AttributeOverride(name = "price", column = @Column(name = "price"))
    private Price price;

    @Builder
    public Product(CompanyId companyId, ProductName name, ProductDescription description, Price price) {
        this.companyId = companyId;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public static Product create(CompanyId companyId, ProductName name, ProductDescription description, Price price) {
        return Product.builder()
                .companyId(companyId)
                .name(name)
                .description(description)
                .price(price)
                .build();
    }

    public void update(ProductName name, ProductDescription description, Price price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public void delete(Long userId) {
        if (userId == null)
            throw new BusinessException(ErrorCode.MUST_NOT_NULL, "userId");
        markDeleted(userId);
    }
}
