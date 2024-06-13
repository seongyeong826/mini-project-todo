package com.todo.api.todo.service.impl;

import com.todo.api.common.exception.CustomException;
import com.todo.api.common.exception.ExceptionCode;
import com.todo.api.todo.entity.Status;
import com.todo.api.todo.entity.Todo;
import com.todo.api.todo.enums.Sort;
import com.todo.api.todo.repository.TodoRepository;
import com.todo.api.todo.request.AddTodoRequest;
import com.todo.api.todo.request.EditTodoStatusRequest;
import com.todo.api.todo.response.GetTodoResponse;
import com.todo.api.todo.service.TodoService;
import com.todo.api.user.entity.User;
import com.todo.api.user.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    @Override
    public List<GetTodoResponse> getTodoSearch(Long userId, Sort sort, Integer start,
        Integer limit) {
//        int page = (start > 0) ? start - 1 : 0;
//        int size = (limit > 0) ? limit : 30;
//
//        Pageable pageable = PageRequest.of(page, size);
//
//        Page<Todo> todoPage;
//        if (sort == Sort.OLDEST) {
//            todoPage = todoRepository.findByUserIdOrderByIdAsc(userId, pageable);
//        } else {
//            todoPage = todoRepository.findByUserIdOrderByIdDesc(userId, pageable);
//        }
//
//        return todoPage.stream()
//            .map(todo -> new GetTodoResponse(todo.getId(), todo.getContent(), todo.getStatus(),
//                todo.getCreatedAt()))
//            .collect(Collectors.toList());
        Pageable pageable = createPageable(start, limit);
        Page<Todo> todoPage = findTodosBySort(userId, sort, pageable);

        return todoPage.stream()
            .map(GetTodoResponse::from)
            .collect(Collectors.toList());
    }

    @Override
    public GetTodoResponse getRecentOneTodo(Long userId) {
        Todo todo = todoRepository.findTopByUserIdOrderByIdDesc(userId);

        return Optional.ofNullable(todo)
            .map(GetTodoResponse::from)
            .orElse(null);
    }

    @Override
    @Transactional
    public void addTodo(Long userId, AddTodoRequest request) {
        User user = findUserById(userId);
        Todo todo = Todo.create(user, request);

        user.insertTodo(todo);
    }

    @Override
    @Transactional
    public void editTodoStatus(Long userId, Long todoId, EditTodoStatusRequest request) {
        Todo todo = findTodoById(todoId);

        validateUserAccess(userId, todo);
        validateStatusChange(todo, request.status());

        todo.updateStatus(request.status());
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER));
    }

    private Todo findTodoById(Long todoId) {
        return todoRepository.findById(todoId)
            .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_TODO));
    }

    private void validateStatusChange(Todo todo, Status status) {
        if (Status.PENDING.equals(status) && !Status.IN_PROGRESS.equals(todo.getStatus())) {
            throw new CustomException(ExceptionCode.CANNOT_CHANGE_TODO_STATUS);
        }
    }

    private void validateUserAccess(Long userId, Todo todo) {
        if (!userId.equals(todo.getUser().getId())) {
            throw new CustomException(ExceptionCode.FORBIDDEN);
        }
    }

    private Pageable createPageable(Integer start, Integer limit) {
        int page = (start > 0) ? start - 1 : 0;
        int size = (limit > 0) ? limit : 30;
        return PageRequest.of(page, size);
    }

    private Page<Todo> findTodosBySort(Long userId, Sort sort, Pageable pageable) {
        if (sort == Sort.OLDEST) {
            return todoRepository.findByUserIdOrderByIdAsc(userId, pageable);
        } else {
            return todoRepository.findByUserIdOrderByIdDesc(userId, pageable);
        }
    }
}
