package com.todo.api.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "회원가입 요청 데이터")
@NotNull
public record SignUpRequest(

    @Schema(description = "계정", example = "id1")
    @NotNull
    String account,
    @Schema(description = "비밀번호", example = "qwer")
    @NotNull
    String password,
    @Schema(description = "닉네임", example = "nickname1")
    @NotNull
    String nickname

) {

}
