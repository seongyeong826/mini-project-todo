package com.todo.api.auth.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그인 응답 데이터")
public record TokenResponse(
    @Schema(description = "토큰", example = "string")
    String token

) {

    public static TokenResponse from(String token) {
        return new TokenResponse(token);
    }
}
