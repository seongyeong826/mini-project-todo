package com.todo.api.auth.controller;

import com.todo.api.auth.request.RefreshRequest;
import com.todo.api.auth.request.SignInRequest;
import com.todo.api.auth.response.TokenResponse;
import com.todo.api.auth.service.AuthService;
import com.todo.api.common.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-in")
    @Operation(summary = "로그인", description = "로그인")
    public Response<TokenResponse> signIn(@RequestBody SignInRequest request) {
        return Response.success(authService.signIn(request));
    }

    // 로그아웃 추가하기
    @PostMapping("/refresh")
    @Operation(summary = "토큰 재발급", description = "엑세스 토큰 재발급")
    public Response<TokenResponse> refresh(@RequestBody RefreshRequest request) {
        return Response.success(authService.refresh(request));
    }

}