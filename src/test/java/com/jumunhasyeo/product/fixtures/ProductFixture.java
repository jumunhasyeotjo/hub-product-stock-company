package com.jumunhasyeo.product.fixtures;

import com.jumunhasyeo.product.domain.entity.Product;
import com.jumunhasyeo.product.domain.vo.CompanyId;
import com.jumunhasyeo.product.domain.vo.Price;
import com.jumunhasyeo.product.domain.vo.ProductDescription;
import com.jumunhasyeo.product.domain.vo.ProductName;

import java.util.UUID;

public class ProductFixture {

    public static Product getProduct() {
        return Product.create(
                CompanyId.of(UUID.randomUUID()),
                ProductName.of("상품"),
                ProductDescription.of("상품 설명"),
                Price.of(10000));
    }
}
