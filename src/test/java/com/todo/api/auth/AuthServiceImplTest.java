package com.todo.api.auth;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.todo.api.auth.request.SignInRequest;
import com.todo.api.auth.request.SignUpRequest;
import com.todo.api.auth.service.impl.AuthServiceImpl;
import com.todo.api.common.exception.CustomException;
import com.todo.api.common.exception.ExceptionCode;
import com.todo.api.user.entity.User;
import com.todo.api.user.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    private static final String ACCOUNT_1 = "account1";
    private static final String ACCOUNT_2 = "account2";

    private static final String PASSWORD_1 = "pass1";
    private static final String PASSWORD_2 = "pass2";

    private static final String NICKNAME_1 = "nickname1";


    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;


    @Test
    @DisplayName("signUp - 이미 존재하는 계정")
    void signUp_DuplicateAccount() {
        User user = new TestUser(ACCOUNT_1);
        SignUpRequest request = new SignUpRequest(ACCOUNT_1, PASSWORD_1, NICKNAME_1);
        when(userRepository.findUserByAccount(ACCOUNT_1)).thenReturn(Optional.of(user));

        CustomException exception = assertThrows(CustomException.class, () -> {
            authService.signUp(request);
        });

        Assertions.assertEquals(ExceptionCode.DUPLICATE_ACCOUNT, exception.getCode());
    }

    @Test
    @DisplayName("signIn - 계정 불일치")
    void signIn_NotFountAccount() {
        SignInRequest request = new SignInRequest(ACCOUNT_2, PASSWORD_1);

        when(userRepository.findUserByAccount(ACCOUNT_2)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> {
            authService.signIn(request);
        });

        Assertions.assertEquals(ExceptionCode.NOT_FOUND_ACCOUNT, exception.getCode());
    }

    @Test
    @DisplayName("signIn - 비밀번호 불일치")
    void signIn_PasswordMismatch() {
        User user = new TestUser(ACCOUNT_1, PASSWORD_1);
        SignInRequest request = new SignInRequest(ACCOUNT_1, PASSWORD_2);

        when(userRepository.findUserByAccount(ACCOUNT_1)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.password(), user.getPassword()))
            .thenReturn(false);

        CustomException exception = assertThrows(CustomException.class, () -> {
            authService.signIn(request);
        });

        Assertions.assertEquals(ExceptionCode.PASSWORD_MISMATCH, exception.getCode());
    }


    private static class TestUser extends User {
        public TestUser(String account) {
            super(null, account, null, null, null, null, null);
        }

        public TestUser(String account, String password) {
            super(null, account, password, null, null, null, null);
        }

    }
}
