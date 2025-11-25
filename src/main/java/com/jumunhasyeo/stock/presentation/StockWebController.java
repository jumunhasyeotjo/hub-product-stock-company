package com.jumunhasyeo.stock.presentation;

import com.jumunhasyeo.common.ApiRes;
import com.jumunhasyeo.stock.application.StockService;
import com.jumunhasyeo.stock.application.command.DecreaseStockCommand;
import com.jumunhasyeo.stock.application.command.IncreaseStockCommand;
import com.jumunhasyeo.stock.application.dto.response.StockRes;
import com.jumunhasyeo.stock.presentation.docs.ApiDocDecrementStock;
import com.jumunhasyeo.stock.presentation.docs.ApiDocIncrementStock;
import com.jumunhasyeo.stock.presentation.dto.request.DecreaseStockReq;
import com.jumunhasyeo.stock.presentation.dto.request.IncrementStockReq;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Stock", description = "재고 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stocks")
public class StockWebController {

    private final StockService stockService;

    //재고 증가 (TODO: HUB_MANAGER/MASTER, SYSTEM)
    @ApiDocIncrementStock
    @PostMapping("/increment")
    public ResponseEntity<ApiRes<StockRes>> increment(
            @Parameter(description = "재고 증가 요청 정보", required = true)
            @RequestBody @Valid IncrementStockReq req
    ) {
        IncreaseStockCommand command = new IncreaseStockCommand(req.productId(), req.amount());
        StockRes stockRes = stockService.increment(command);
        return ResponseEntity.ok(ApiRes.success(stockRes));
    }

    //재고 증가 (TODO: HUB_MANAGER/MASTER, SYSTEM)
    @ApiDocDecrementStock
    @PostMapping("/decrement")
    public ResponseEntity<ApiRes<StockRes>> decrement(
            @Parameter(description = "재고 감소 요청 정보", required = true)
            @RequestBody @Valid DecreaseStockReq req
    ) {
        DecreaseStockCommand command = new DecreaseStockCommand(req.productId(), req.amount());
        StockRes stockRes = stockService.decrement(command);
        return ResponseEntity.ok(ApiRes.success(stockRes));
    }
}