package com.todo.api.auth.service;

import com.todo.api.auth.dto.CustomUserInfoDto;
import com.todo.api.common.exception.CustomException;
import com.todo.api.common.exception.ExceptionCode;
import com.todo.api.user.entity.User;
import com.todo.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userRepository.findById(Long.valueOf(userId))
            .orElseThrow(() -> new CustomException(ExceptionCode.NOT_FOUND_USER));

        CustomUserInfoDto userInfo = CustomUserInfoDto.from(user);

        return new CustomUserDetails(userInfo);
    }
}
