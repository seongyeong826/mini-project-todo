package com.todo.api.user.service;

import com.todo.api.auth.request.SignUpRequest;

public interface UserService {

    void signUp(SignUpRequest request);

    void removeUser(Long userId);

}
