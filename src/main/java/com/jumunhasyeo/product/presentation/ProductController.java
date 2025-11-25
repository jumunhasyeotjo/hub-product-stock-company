package com.jumunhasyeo.product.presentation;

import com.jumunhasyeo.common.ApiRes;
import com.jumunhasyeo.product.application.ProductService;
import com.jumunhasyeo.product.application.command.*;
import com.jumunhasyeo.product.application.dto.ProductRes;
import com.jumunhasyeo.product.presentation.dto.req.CreateProductReq;
import com.jumunhasyeo.product.presentation.dto.req.ProductSearchCondition;
import com.jumunhasyeo.product.presentation.dto.req.UpdateProductReq;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ApiRes<ProductRes>> createProduct(@RequestBody CreateProductReq req,
                                                            @RequestHeader("user-id") Long userId) {
        CreateProductCommand command = new CreateProductCommand(
                req.name(),
                req.price(),
                req.description(),
                userId
        );

        ProductRes response = productService.createProduct(command);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiRes.success(response));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ApiRes<ProductRes>> updateProduct(@RequestBody UpdateProductReq req,
                                                            @PathVariable UUID productId,
                                                            @RequestHeader("user-id") Long userId) {
        UpdateProductCommand command = new UpdateProductCommand(
                productId,
                userId,
                req.name(),
                req.price(),
                req.description()
        );

        ProductRes response = productService.updateProduct(command);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiRes.success(response));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiRes<?>> deleteProduct(@PathVariable UUID productId,
                                                   @RequestHeader("user-id") Long userId,
                                                   @RequestHeader("user-role") String userRole) {
        DeleteProductCommand command = new DeleteProductCommand(
                productId,
                userId,
                userRole);

        productService.deleteProduct(command);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiRes<ProductRes>> getProduct(@PathVariable UUID productId) {
        GetProductCommand command = new GetProductCommand(productId);

        ProductRes response = productService.getProduct(command);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiRes.success(response));
    }


    @GetMapping
    public ResponseEntity<ApiRes<?>> searchProducts(@ModelAttribute ProductSearchCondition condition,
                                                    @PageableDefault Pageable pageable) {
        SearchProductCommand command = new SearchProductCommand(condition, pageable);

        Page<ProductRes> response = productService.searchProduct(command);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiRes.success(response));
    }
}
