package com.todo.api.todo.service;

import com.todo.api.todo.enums.Sort;
import com.todo.api.todo.request.AddTodoRequest;
import com.todo.api.todo.request.EditTodoStatusRequest;
import com.todo.api.todo.response.GetTodoResponse;
import java.util.List;

public interface TodoService {

    List<GetTodoResponse> getTodoSearch(Long userId, Sort sort, Integer start, Integer limit);

    GetTodoResponse getRecentOneTodo(Long userId);

    void addTodo(Long userId, AddTodoRequest request);

    void editTodoStatus(Long userId, Long todoId, EditTodoStatusRequest request);

}
