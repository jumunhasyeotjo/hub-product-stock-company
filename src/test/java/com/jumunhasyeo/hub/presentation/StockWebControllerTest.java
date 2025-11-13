package com.jumunhasyeo.hub.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jumunhasyeo.ControllerTestConfig;
import com.jumunhasyeo.hub.application.HubService;
import com.jumunhasyeo.hub.application.command.DecreaseStockCommand;
import com.jumunhasyeo.hub.application.dto.response.StockRes;
import com.jumunhasyeo.hub.exception.GlobalExceptionHandler;
import com.jumunhasyeo.hub.presentation.dto.request.IncrementStockReq;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StockWebController.class)
@Import({ControllerTestConfig.class, GlobalExceptionHandler.class})
class StockWebControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private HubService hubService;

    @Test
    @DisplayName("재고 증가 API로 재고 증가를 요청할 수 있다.")
    void increment_stock_success() throws Exception {
        // given
        UUID productId = UUID.randomUUID();
        UUID stockId = UUID.randomUUID();
        IncrementStockReq request = new IncrementStockReq(productId, 100);
        StockRes stockRes = StockRes.builder()
                .stockId(stockId)
                .hubId(UUID.randomUUID())
                .productId(productId)
                .quantity(200)
                .build();

        given(hubService.increaseStock(any())).willReturn(stockRes);

        // when & then
        mockMvc.perform(post("/api/v1/stocks/increment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.stockId").value(stockId.toString()))
                .andExpect(jsonPath("$.data.productId").value(productId.toString()))
                .andExpect(jsonPath("$.data.hubId").exists())
                .andExpect(jsonPath("$.data.quantity").value(200))
                .andReturn();
    }

    @Test
    @DisplayName("재고 감소 API로 재고 감소를 요청할 수 있다.")
    void decrement_stock_success() throws Exception {
        // given
        UUID productId = UUID.randomUUID();
        UUID stockId = UUID.randomUUID();
        DecreaseStockCommand request = new DecreaseStockCommand(productId, 100);
        StockRes stockRes = StockRes.builder()
                .stockId(stockId)
                .hubId(UUID.randomUUID())
                .productId(productId)
                .quantity(200)
                .build();

        given(hubService.decreaseStock(any())).willReturn(stockRes);

        // when & then
        mockMvc.perform(post("/api/v1/stocks/decrement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.stockId").value(stockId.toString()))
                .andExpect(jsonPath("$.data.productId").value(productId.toString()))
                .andExpect(jsonPath("$.data.hubId").exists())
                .andExpect(jsonPath("$.data.quantity").value(200))
                .andReturn();
    }
}