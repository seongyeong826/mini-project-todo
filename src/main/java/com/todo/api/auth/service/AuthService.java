package com.todo.api.auth.service;

import com.todo.api.auth.response.TokenResponse;
import com.todo.api.auth.request.SignInRequest;
import com.todo.api.auth.request.SignUpRequest;

public interface AuthService {

    void signUp(SignUpRequest request);
    TokenResponse signIn(SignInRequest request);

}
