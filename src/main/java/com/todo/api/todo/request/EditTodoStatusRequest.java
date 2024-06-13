package com.todo.api.todo.request;

import com.todo.api.todo.entity.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "todo 상태 변경 요청 데이터")
@NotNull
public record EditTodoStatusRequest(

    @Schema(description = "상태", example = "DONE")
    @NotNull
    Status status
) {

}
