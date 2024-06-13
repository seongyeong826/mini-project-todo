package com.todo.api.todo.todo;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.todo.api.common.exception.CustomException;
import com.todo.api.common.exception.ExceptionCode;
import com.todo.api.todo.entity.Status;
import com.todo.api.todo.entity.Todo;
import com.todo.api.todo.enums.Sort;
import com.todo.api.todo.repository.TodoRepository;
import com.todo.api.todo.request.EditTodoStatusRequest;
import com.todo.api.todo.response.GetTodoResponse;
import com.todo.api.todo.service.impl.TodoServiceImpl;
import com.todo.api.user.entity.User;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class TodoServiceImplTest {

    private static final Long USER_ID_1 = 1L;
    private static final Long USER_ID_2 = 2L;
    private static final Long TODO_ID_1 = 1L;
    private static final String CONTENT_1 = "할일1";

    @InjectMocks
    private TodoServiceImpl todoService;

    @Mock
    private TodoRepository todoRepository;

    @Test
    @DisplayName("getTodos - 조회된 할 일 없음")
    void getTodoSearch_NoTodos() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Todo> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(todoRepository.findByUserIdOrderByIdDesc(USER_ID_1, pageable)).thenReturn(emptyPage);

        List<GetTodoResponse> result = todoService.getTodoSearch(USER_ID_1, Sort.LATEST, 1, 10);

        Assertions.assertEquals(0, result.size());
        verify(todoRepository).findByUserIdOrderByIdDesc(USER_ID_1, pageable);
    }

    @Test
    @DisplayName("getRecentTodo - 최근 할 일 없음")
    void getRecentTodo_NoTodo() {
        when(todoRepository.findTopByUserIdOrderByIdDesc(USER_ID_1)).thenReturn(null);

        GetTodoResponse result = todoService.getRecentOneTodo(USER_ID_1);

        Assertions.assertEquals(result, null);
    }

    @Test
    @DisplayName("editTodoStatus - 할일 -> 대기 상태 변경 실패")
    void editTodoStatus_FailChangeToPending1() {
        User user = new TestUser(USER_ID_1);
        Todo todo = new TestTodo(TODO_ID_1, user, Status.TODO, CONTENT_1);
        EditTodoStatusRequest request = new EditTodoStatusRequest(Status.PENDING);

        when(todoRepository.findById(TODO_ID_1)).thenReturn(Optional.of(todo));

        CustomException exception = assertThrows(CustomException.class, () -> {
            todoService.editTodoStatus(USER_ID_1, TODO_ID_1, request);
        });

        Assertions.assertEquals(ExceptionCode.CANNOT_CHANGE_TODO_STATUS, exception.getCode());
        Assertions.assertEquals(Status.TODO, todo.getStatus());
        verify(todoRepository).findById(TODO_ID_1);
    }

    @Test
    @DisplayName("editTodoStatus - 완료 -> 대기 상태 변경 실패")
    void editTodoStatus_FailChangeToPending2() {
        User user = new TestUser(USER_ID_1);
        Todo todo = new TestTodo(TODO_ID_1, user, Status.DONE, CONTENT_1);
        EditTodoStatusRequest request = new EditTodoStatusRequest(Status.PENDING);

        when(todoRepository.findById(TODO_ID_1)).thenReturn(Optional.of(todo));

        CustomException exception = assertThrows(CustomException.class, () -> {
            todoService.editTodoStatus(USER_ID_1, TODO_ID_1, request);
        });

        Assertions.assertEquals(ExceptionCode.CANNOT_CHANGE_TODO_STATUS, exception.getCode());
        Assertions.assertEquals(Status.DONE, todo.getStatus());
        verify(todoRepository).findById(TODO_ID_1);
    }

    @Test
    @DisplayName("editTodoStatus - 진행 중 -> 대기 상태 변경 실패")
    void editTodoStatus_FailChangeToPending3() {
        User user = new TestUser(USER_ID_1);
        Todo todo = new TestTodo(TODO_ID_1, user, Status.IN_PROGRESS, CONTENT_1);
        EditTodoStatusRequest request = new EditTodoStatusRequest(Status.PENDING);

        when(todoRepository.findById(TODO_ID_1)).thenReturn(Optional.of(todo));

        todoService.editTodoStatus(USER_ID_1, TODO_ID_1, request);

        Assertions.assertEquals(Status.PENDING, todo.getStatus());
        verify(todoRepository).findById(TODO_ID_1);
    }

    @Test
    @DisplayName("editTodoStatus - 대기 -> 할일 상태 변경 성공")
    void editTodoStatus_SuccessChange1() {
        User user = new TestUser(USER_ID_1);
        Todo todo = new TestTodo(TODO_ID_1, user, Status.PENDING, CONTENT_1);
        EditTodoStatusRequest request = new EditTodoStatusRequest(Status.TODO);

        when(todoRepository.findById(TODO_ID_1)).thenReturn(Optional.of(todo));

        todoService.editTodoStatus(USER_ID_1, TODO_ID_1, request);
        Assertions.assertEquals(Status.TODO, todo.getStatus());

        verify(todoRepository).findById(TODO_ID_1);
    }

    @Test
    @DisplayName("editTodoStatus - 대기 -> 진행 중 변경 성공")
    void editTodoStatus_SuccessChange2() {
        User user = new TestUser(USER_ID_1);
        Todo todo = new TestTodo(TODO_ID_1, user, Status.PENDING, CONTENT_1);
        EditTodoStatusRequest request = new EditTodoStatusRequest(Status.IN_PROGRESS);

        when(todoRepository.findById(TODO_ID_1)).thenReturn(Optional.of(todo));

        todoService.editTodoStatus(USER_ID_1, TODO_ID_1, request);
        Assertions.assertEquals(Status.IN_PROGRESS, todo.getStatus());

        verify(todoRepository).findById(TODO_ID_1);
    }

    @Test
    @DisplayName("editTodoStatus - 대기 -> 완료 변경 성공")
    void editTodoStatus_SuccessChange3() {
        User user = new TestUser(USER_ID_1);
        Todo todo = new TestTodo(TODO_ID_1, user, Status.PENDING, CONTENT_1);
        EditTodoStatusRequest request = new EditTodoStatusRequest(Status.DONE);

        when(todoRepository.findById(TODO_ID_1)).thenReturn(Optional.of(todo));

        todoService.editTodoStatus(USER_ID_1, TODO_ID_1, request);
        Assertions.assertEquals(Status.DONE, todo.getStatus());

        verify(todoRepository).findById(TODO_ID_1);
    }

    @Test
    @DisplayName("editTodoStatus - 상태 변경 권한 없음")
    void editTodoStatus_Forbidden() {
        User user1 = new TestUser(USER_ID_1);
        Todo todo = new TestTodo(TODO_ID_1, user1, Status.TODO, CONTENT_1);
        EditTodoStatusRequest request = new EditTodoStatusRequest(Status.DONE);

        when(todoRepository.findById(TODO_ID_1)).thenReturn(Optional.of(todo));

        CustomException exception = assertThrows(CustomException.class, () -> {
            todoService.editTodoStatus(USER_ID_2, TODO_ID_1, request);
        });

        Assertions.assertEquals(ExceptionCode.FORBIDDEN, exception.getCode());
        Assertions.assertEquals(Status.TODO, todo.getStatus());
        verify(todoRepository).findById(TODO_ID_1);
    }

    private static class TestUser extends User {

        public TestUser(Long id) {
            super(id, null, null, null, null, null, null);
        }
    }

    private static class TestTodo extends Todo {

        public TestTodo(Long id, User user, Status status, String content) {
            super(id, user, content, status, LocalDateTime.now());
        }
    }
}

