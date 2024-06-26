package com.todo.api.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "엑세스 토큰 재발급 요청 데이터")
@NotNull
public record RefreshRequest(
    @Schema(description = "리프레시 토큰", example = "token")
    @NotNull
    String refreshToken
) {

}
