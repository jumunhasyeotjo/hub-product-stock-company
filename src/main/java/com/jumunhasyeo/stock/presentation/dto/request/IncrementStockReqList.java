package com.jumunhasyeo.stock.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "재고 증가 요청 리스트")
public record IncrementStockReqList(
       List<IncrementStockReq> productList
) {
}
