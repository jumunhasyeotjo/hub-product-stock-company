package com.jumunhasyeo.product.presentation;

import com.jumunhasyeo.product.application.ProductService;
import com.jumunhasyeo.product.application.command.*;
import com.jumunhasyeo.product.application.dto.ProductRes;
import com.jumunhasyeo.product.presentation.docs.*;
import com.jumunhasyeo.product.presentation.dto.req.CreateProductReq;
import com.jumunhasyeo.product.presentation.dto.req.ProductSearchCondition;
import com.jumunhasyeo.product.presentation.dto.req.UpdateProductReq;
import com.library.passport.annotation.PassportAuthorize;
import com.library.passport.annotation.PassportUser;
import com.library.passport.entity.ApiRes;
import com.library.passport.entity.PassportUserRole;
import com.library.passport.proto.PassportProto.Passport;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Product", description = "상품 관리 (등록, 수정, 삭제, 조회) API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @ApiDocCreateProduct
    @PassportAuthorize(allowedRoles = PassportUserRole.COMPANY_MANAGER)
    @PostMapping
    public ResponseEntity<ApiRes<ProductRes>> createProduct(@RequestBody CreateProductReq req,
                                                            @PassportUser Passport passport
    ) {
        CreateProductCommand command = new CreateProductCommand(
                req.name(),
                UUID.fromString(passport.getBelong()),
                req.price(),
                req.description(),
                passport.getUserId()
        );

        ProductRes response = productService.createProduct(command);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiRes.success(response));
    }

    @ApiDocUpdateProduct
    @PassportAuthorize(allowedRoles = PassportUserRole.COMPANY_MANAGER)
    @PutMapping("/{productId}")
    public ResponseEntity<ApiRes<ProductRes>> updateProduct(@RequestBody UpdateProductReq req,
                                                            @PassportUser Passport passport,
                                                            @PathVariable UUID productId) {
        UpdateProductCommand command = new UpdateProductCommand(
                productId,
                UUID.fromString(passport.getBelong()),
                passport.getUserId(),
                req.name(),
                req.price(),
                req.description()
        );

        ProductRes response = productService.updateProduct(command);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiRes.success(response));
    }

    @ApiDocDeleteProduct
    @PassportAuthorize(allowedRoles = {PassportUserRole.COMPANY_MANAGER, PassportUserRole.MASTER})
    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiRes<?>> deleteProduct(@PathVariable UUID productId,
                                                   @PassportUser Passport passport
    ) {
        DeleteProductCommand command = new DeleteProductCommand(
                productId,
                UUID.fromString(passport.getBelong()),
                passport.getUserId(),
                passport.getRole());

        productService.deleteProduct(command);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @ApiDocGetProduct
    @GetMapping("/{productId}")
    public ResponseEntity<ApiRes<ProductRes>> getProduct(@PathVariable UUID productId) {
        GetProductCommand command = new GetProductCommand(productId);

        ProductRes response = productService.getProduct(command);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiRes.success(response));
    }


    @ApiDocSearchProduct
    @GetMapping
    public ResponseEntity<ApiRes<Page<ProductRes>>> searchProducts(@ModelAttribute ProductSearchCondition condition,
                                                    @PageableDefault Pageable pageable) {
        SearchProductCommand command = new SearchProductCommand(condition, pageable);

        Page<ProductRes> response = productService.searchProduct(command);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiRes.success(response));
    }
}
