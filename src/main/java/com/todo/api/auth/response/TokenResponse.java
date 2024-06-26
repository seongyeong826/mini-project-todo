package com.todo.api.auth.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그인 응답 데이터")
public record TokenResponse(
    @Schema(description = "엑세스 토큰", example = "string")
    String accessToken,

    @Schema(description = "리프레시 토큰", example = "string")
    String refreshToken

) {

    public static TokenResponse from(String accessToken, String refreshToken) {
        return new TokenResponse(accessToken, refreshToken);
    }
}
