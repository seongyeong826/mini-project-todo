package com.todo.api.auth.dto;

import com.todo.api.user.entity.Role;
import com.todo.api.user.entity.User;

public record CustomUserInfoDto(

    Long id,
    String account,
    Role role
) {

    private CustomUserInfoDto(User user) {
        this(
            user.getId(),
            user.getAccount(),
            user.getRole()
        );
    }

    public static CustomUserInfoDto from(User user) {
        return new CustomUserInfoDto(user);
    }
}
