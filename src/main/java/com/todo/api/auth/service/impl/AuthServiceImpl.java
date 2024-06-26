package com.todo.api.auth.service.impl;

import com.todo.api.auth.dto.CustomUserInfoDto;
import com.todo.api.auth.request.RefreshRequest;
import com.todo.api.auth.request.SignInRequest;
import com.todo.api.auth.response.TokenResponse;
import com.todo.api.auth.service.AuthService;
import com.todo.api.auth.util.JwtUtil;
import com.todo.api.common.exception.CustomException;
import com.todo.api.common.exception.ExceptionCode;
import com.todo.api.user.entity.User;
import com.todo.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public TokenResponse signIn(SignInRequest request) {
        User user = findUserByAccount(request.account());

        validatePassword(request.password(), user.getPassword());

        CustomUserInfoDto userInfo = CustomUserInfoDto.from(user);
        String accessToken = jwtUtil.createAccessToken(userInfo);
        String refreshToken = jwtUtil.createRefreshToken(userInfo);

        return TokenResponse.from(accessToken, refreshToken);
    }

    @Override
    public TokenResponse refresh(RefreshRequest request) {
        if (jwtUtil.validateToken(request.refreshToken())) {
            Long userId = jwtUtil.getUserId(request.refreshToken());
            User user = findUserById(userId);

            CustomUserInfoDto userInfo = CustomUserInfoDto.from(user);
            String accessToken = jwtUtil.createAccessToken(userInfo);
            String refreshToken = jwtUtil.createRefreshToken(userInfo);

            return TokenResponse.from(accessToken, refreshToken);
        }

        return null;
    }

    private User findUserById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER));
    }

    private User findUserByAccount(String account) {
        return userRepository.findUserByAccount(account)
            .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_ACCOUNT));
    }


    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new CustomException(ExceptionCode.PASSWORD_MISMATCH);
        }
    }

    private String createAccessToken(User user) {
        CustomUserInfoDto userInfo = CustomUserInfoDto.from(user);
        return jwtUtil.createAccessToken(userInfo);
    }
}
