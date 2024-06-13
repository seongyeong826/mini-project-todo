package com.todo.api.todo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "todo 추가 요청 데이터")
@NotNull
public record AddTodoRequest(
    @Schema(description = "내용", example = "할일1")
    @NotNull
    String content
) {

}
