package com.jumunhasyeo.product.presentation;

import com.jumunhasyeo.common.ApiRes;
import com.jumunhasyeo.product.application.ProductService;
import com.jumunhasyeo.product.presentation.docs.ApiDocInternalExistProduct;
import com.jumunhasyeo.product.presentation.docs.ApiDocInternalSearchOrderProduct;
import com.jumunhasyeo.product.presentation.dto.res.OrderProductRes;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Internal-Product", description = "내부 서비스 간 통신용 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products/internal")
public class ProductInternalController {

    private final ProductService productService;

    @ApiDocInternalExistProduct
    @GetMapping("/{productId}/exists")
    public ResponseEntity<ApiRes<Boolean>> existProduct(@PathVariable UUID productId) {
        Boolean response = productService.existsProduct(productId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiRes.success(response));
    }

    @ApiDocInternalSearchOrderProduct
    @PostMapping("/order")
    public ResponseEntity<ApiRes<List<OrderProductRes>>> searchOrderProduct(@RequestBody List<UUID> req) {
        List<OrderProductRes> response = productService.searchOrderProduct(req);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiRes.success(response));
    }
}
