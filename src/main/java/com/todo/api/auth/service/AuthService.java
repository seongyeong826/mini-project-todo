package com.todo.api.auth.service;

import com.todo.api.auth.request.RefreshRequest;
import com.todo.api.auth.request.SignInRequest;
import com.todo.api.auth.response.TokenResponse;

public interface AuthService {

    TokenResponse signIn(SignInRequest request);

    void logout(Long userId);

    TokenResponse refresh(RefreshRequest request);

}
