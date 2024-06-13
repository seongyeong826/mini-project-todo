package com.todo.api.user;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.todo.api.common.exception.CustomException;
import com.todo.api.common.exception.ExceptionCode;
import com.todo.api.todo.entity.Todo;
import com.todo.api.user.entity.User;
import com.todo.api.user.repository.UserRepository;
import com.todo.api.user.service.impl.UserServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    private static final Long USER_ID_1 = 1L;
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("removeUser - 존재하지 않는 사용자")
    void removeUser_UserNotFound() {
        when(userRepository.findById(USER_ID_1)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> {
            userService.removeUser(USER_ID_1);
        });

        verify(userRepository).findById(USER_ID_1);
        Assertions.assertEquals(ExceptionCode.NOT_FOUND_USER, exception.getCode());
    }

    private static class TestUser extends User {

        public TestUser(Long id) {
            super(id, null, null, null, null, null, null);
        }

    }

    private static class TestTodo extends Todo {

        public TestTodo(User user) {
            super(null, user, null, null, null);
        }

    }
}

