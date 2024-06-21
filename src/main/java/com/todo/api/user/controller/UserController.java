package com.todo.api.user.controller;

import com.todo.api.auth.request.SignUpRequest;
import com.todo.api.auth.util.SecurityUtil;
import com.todo.api.common.response.Response;
import com.todo.api.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/sign-up")
    @Operation(summary = "회원가입", description = "회원가입")
    public Response<Void> signUp(@RequestBody SignUpRequest request) {
        userService.signUp(request);
        return Response.success(null);
    }

    @DeleteMapping("")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "회원 탈퇴", description = "탈퇴를 진행합니다.")
    public Response<Void> removeUser() {
        Long userId = SecurityUtil.getCurrentUser();
        userService.removeUser(userId);
        return Response.success(null);
    }

}
