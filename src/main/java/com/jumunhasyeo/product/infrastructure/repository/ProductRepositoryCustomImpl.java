package com.jumunhasyeo.product.infrastructure.repository;

import com.jumunhasyeo.product.application.command.SearchProductCommand;
import com.jumunhasyeo.product.application.dto.ProductRes;
import com.jumunhasyeo.product.presentation.dto.req.ProductSearchCondition;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

import static com.jumunhasyeo.product.domain.entity.QProduct.product;


@Repository
@RequiredArgsConstructor
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ProductRes> searchProduct(SearchProductCommand command) {
        ProductSearchCondition condition = command.condition();

        // 1. 콘텐츠 조회
        List<ProductRes> content = queryFactory
                .select(Projections.constructor(
                        ProductRes.class,
                        product.id,
                        product.name,
                        product.price,
                        product.description))
                .from(product)
                .where(
                        eqCompanyId(condition.companyId()),
                        containsName(condition.name()),
                        goeMinPrice(condition.minPrice()),
                        loeMaxPrice(condition.maxPrice()),
                        isNotDeleted()
                )
                .offset(command.pageable().getOffset())
                .limit(command.pageable().getPageSize())
                .fetch();

        // 2. 카운트 쿼리
        Long total = queryFactory
                .select(product.count())
                .from(product)
                .where(
                        eqCompanyId(condition.companyId()),
                        containsName(condition.name()),
                        goeMinPrice(condition.minPrice()),
                        loeMaxPrice(condition.maxPrice()),
                        isNotDeleted()
                )
                .fetchOne();

        return new PageImpl<>(content, command.pageable(), (total != null) ? total : 0L);
    }

    private BooleanExpression eqCompanyId(UUID companyId) {
        return companyId != null ? product.companyId.companyId.eq(companyId) : null;
    }

    private BooleanExpression containsName(String name) {
        return StringUtils.hasText(name) ? product.name.name.contains(name) : null;
    }

    // 최소 가격 조건 (>= minPrice)
    private BooleanExpression goeMinPrice(Integer minPrice) {
        return minPrice != null ? product.price.price.goe(minPrice) : null;
    }

    // 최대 가격 조건 (<= maxPrice)
    private BooleanExpression loeMaxPrice(Integer maxPrice) {
        return maxPrice != null ? product.price.price.loe(maxPrice) : null;
    }

    // Soft Delete 조건 (deletedAt is null)
    private BooleanExpression isNotDeleted() {
        return product.deletedAt.isNull();
    }
}