package com.jumunhasyeo.stock.presentation.dto.request;

import java.util.List;

public record StoreStockReqList (
        List<StoreStockReq> productList
){
}
