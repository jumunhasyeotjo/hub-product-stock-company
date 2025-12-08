package com.jumunhasyeo.hub.hub.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record DeleteHubReq(
        @Schema(description = "삭제할 허브 id", example = "77777777-7777-7777-7777-777777777777", required = true)
        @NotNull(message = "허브 id는 필수입니다")
        UUID hubId,
        @Schema(description = "삭제 요청자 ID", example = "1L", required = true)
        @NotNull(message = "삭제 요청자 id는 필수입니다")
        Long userId
) {
}
