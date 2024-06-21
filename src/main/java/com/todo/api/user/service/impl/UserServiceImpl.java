package com.todo.api.user.service.impl;

import com.todo.api.auth.request.SignUpRequest;
import com.todo.api.common.exception.CustomException;
import com.todo.api.common.exception.ExceptionCode;
import com.todo.api.user.entity.Role;
import com.todo.api.user.entity.User;
import com.todo.api.user.repository.UserRepository;
import com.todo.api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void signUp(SignUpRequest request) {
        checkDuplicateUser(request.account());

        User newUser = User.create(Role.USER, request, passwordEncoder);

        userRepository.save(newUser);
    }

    @Override
    @Transactional
    public void removeUser(Long userId) {
        User user = findUserById(userId);

        userRepository.delete(user);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER));
    }

    private void checkDuplicateUser(String account) {
        userRepository.findUserByAccount(account)
            .ifPresent(user -> {
                throw new CustomException(ExceptionCode.DUPLICATE_ACCOUNT);
            });
    }

}
