package com.todo.api.todo.response;

import com.todo.api.todo.entity.Status;
import com.todo.api.todo.entity.Todo;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "todo 조회 응답 데이터")
public record GetTodoResponse(
    @Schema(description = "todo 아이디", example = "1")
    Long id,
    @Schema(description = "내용", example = "할일1")
    String content,

    @Schema(description = "상태", example = "IN_PROGRESS")
    Status status,

    @Schema(description = "생성일", example = "2024-06-13T06:33:28.663686")
    LocalDateTime createdAt

) {
    public static GetTodoResponse from(Todo todo) {
        return new GetTodoResponse(
            todo.getId(),
            todo.getContent(),
            todo.getStatus(),
            todo.getCreatedAt()
        );
    }

}
