package com.todo.api.todo.controller;

import com.todo.api.auth.util.SecurityUtil;
import com.todo.api.common.response.Response;
import com.todo.api.todo.enums.Sort;
import com.todo.api.todo.request.AddTodoRequest;
import com.todo.api.todo.request.EditTodoStatusRequest;
import com.todo.api.todo.response.GetTodoResponse;
import com.todo.api.todo.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/todos")
public class TodoController {

    private final TodoService todoService;

    @GetMapping("/search")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "todo 조건 조회", description = "todo 조건 조회")
    public Response<List<GetTodoResponse>> getTodoSearch(
        @RequestParam(value = "sort", defaultValue = "LATEST") @Parameter(description = "정렬 방식", example = "LATEST") Sort sort,
        @RequestParam(value = "start", defaultValue = "1") @Parameter(description = "시작 지점(1부터 시작)", example = "1") Integer start,
        @RequestParam(value = "limit", defaultValue = "30") @Parameter(description = "반환 갯수", example = "30") Integer limit
    ) {
        Long userId = SecurityUtil.getCurrentUser();
        return Response.success(todoService.getTodoSearch(userId, sort, start, limit));
    }

    @GetMapping("/recent-one")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "최근 todo 1건 조회", description = "최근 todo 1건 조회")
    public Response<GetTodoResponse> getRecentOneTodo() {
        Long userId = SecurityUtil.getCurrentUser();
        return Response.success(todoService.getRecentOneTodo(userId));
    }

    @PostMapping()
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "todo 추가", description = "todo 추가")
    public Response<Void> addTodo(@RequestBody AddTodoRequest request) {
        Long userId = SecurityUtil.getCurrentUser();
        todoService.addTodo(userId, request);
        return Response.success(null);
    }

    @PatchMapping("/{todoId}/status")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "todo 상태 변경", description = "todo 상태 변경")
    public Response<Void> editTodoStatus(
        @PathVariable @Parameter(description = "todo 아이디", required = true) Long todoId,
        @RequestBody EditTodoStatusRequest request) {
        Long userId = SecurityUtil.getCurrentUser();
        todoService.editTodoStatus(userId, todoId, request);
        return Response.success(null);
    }

}
