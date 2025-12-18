package com.jumunhasyeo.product.application;

import com.jumunhasyeo.common.exception.BusinessException;
import com.jumunhasyeo.common.exception.ErrorCode;
import com.jumunhasyeo.product.application.command.*;
import com.jumunhasyeo.product.application.dto.ProductRes;
import com.jumunhasyeo.product.application.service.CompanyClient;
import com.jumunhasyeo.product.domain.entity.Product;
import com.jumunhasyeo.product.domain.repository.ProductRepository;
import com.jumunhasyeo.product.domain.vo.CompanyId;
import com.jumunhasyeo.product.domain.vo.Price;
import com.jumunhasyeo.product.domain.vo.ProductDescription;
import com.jumunhasyeo.product.domain.vo.ProductName;
import com.jumunhasyeo.product.presentation.dto.res.OrderProductRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final CompanyClient companyClient;

    private final static String CACHE_NAME = "product";

    @Transactional
    public ProductRes createProduct(CreateProductCommand req) {
        validateCompanyId(req.organizationId());

        ProductName productName = ProductName.of(req.name());
        validateCreateProductName(productName);

        Price price = Price.of(req.price());
        ProductDescription productDescription = ProductDescription.of(req.description());

        Product product = Product.create(CompanyId.of(req.organizationId()), productName, productDescription, price);
        productRepository.save(product);

        return ProductRes.of(product);
    }

    // 업체 ID 검증
    private void validateCompanyId(UUID companyId) {
        if (!companyClient.existsCompany(companyId)) {
            throw new BusinessException(ErrorCode.COMPANY_NOT_FOUND);
        }
    }

    // 중복 상품명 검증
    private void validateCreateProductName(ProductName productName) {
        if (productRepository.existsByName(productName)) {
            throw new BusinessException(ErrorCode.NAME_ALREADY_EXISTS);
        }
    }

    @Transactional
    @CacheEvict(value = CACHE_NAME, key = "#req.productId()")
    public ProductRes updateProduct(UpdateProductCommand req) {
        Product product = getProduct(req.productId());

        validateUpdate(product, req.organizationId());

        ProductName name = ProductName.of(req.name());
        validateUpdateProductName(req);

        product.update(name, ProductDescription.of(req.description()), Price.of(req.price()));
        return ProductRes.of(product);
    }

    // 중복 상품명 검증
    private void validateUpdateProductName(UpdateProductCommand req) {
        if (productRepository.existsByNameAndIdNot(ProductName.of(req.name()), req.productId())) {
            throw new BusinessException(ErrorCode.NAME_ALREADY_EXISTS);
        }
    }

    // 상품 조회
    private Product getProduct(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    // 수정 가능한 CompanyManager 검증
    private static void validateUpdate(Product product, UUID companyId) {
        if (!product.getCompanyId().getCompanyId().equals(companyId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }
    }

    @Transactional
    @CacheEvict(value = CACHE_NAME, key = "#req.productId()")
    public void deleteProduct(DeleteProductCommand req) {
        Product product = getProduct(req.productId());

        validateCompanyManager(req, product);

        product.delete(req.userId());
    }

    // 업체 담당자 검증
    private void validateCompanyManager(DeleteProductCommand req, Product product) {
        if (req.role().equals("COMPANY_MANAGER")) {
            if (!product.getCompanyId().getCompanyId().equals(req.organizationId())) {
                throw new BusinessException(ErrorCode.FORBIDDEN);
            }
        }
    }

    @Transactional(readOnly = true)
    @Cacheable(value = CACHE_NAME, key = "#req.productId()", unless = "#result == null")
    public ProductRes getProduct(GetProductCommand req) {
        Product product = getProduct(req.productId());
        return ProductRes.of(product);
    }

    @Transactional(readOnly = true)
    public Page<ProductRes> searchProduct(SearchProductCommand req) {
        return productRepository.searchProduct(req);
    }

    @Transactional(readOnly = true)
    public Boolean existsProduct(UUID productId) {
        return productRepository.existsById(productId);
    }

    @Transactional(readOnly = true)
    public List<OrderProductRes> searchOrderProduct(List<UUID> orderProductIds) {
        return productRepository.findAllByIds(orderProductIds);
    }
}
