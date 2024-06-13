package com.todo.api.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionCode {
    // auth
    UNAUTHORIZED("AUTH_001", "인증 정보가 유효하지 않습니다."),
    FORBIDDEN("AUTH_002", "접근 권한이 없습니다."),
    INVALID_JWT_SIGNATURE("AUTH_003", "잘못된 JWT 서명입니다."),
    EXPIRED_JWT("AUTH_004", "만료된 JWT 토큰입니다."),
    UNSUPPORTED_JWT("AUTH_005", "지원되지 않는 JWT 토큰입니다."),
    ILLEGAL_JWT("AUTH_006", "JWT 토큰이 잘못되었습니다."),

    // user
    NOT_FOUND_USER("USER_001", "회원을 찾을 수 없습니다"),
    NOT_FOUND_ACCOUNT("USER_002", "계정을 찾을 수 없습니다"),
    PASSWORD_MISMATCH("USER_003", "비밀번호가 일치하지 않습니다."),
    DUPLICATE_ACCOUNT("USER_004", "이미 존재하는 계정입니다."),

    // todo
    NOT_FOUND_TODO("TODO_001", "TODO를 찾을 수 없습니다"),
    CANNOT_CHANGE_TODO_STATUS("TODO_002", "TODO 상태를 변경할 수 없습니다."),

    // common
    NOT_FOUND("COMMON_001", "찾을 수 없습니다."),
    INVALID_VALUE("COMMON_002", "유효하지 않은 값입니다."),
    NOT_NULL("COMMON_003", "null 값을 허용하지 않습니다."),
    REQUEST_BODY_MISSING("COMMON_004", "요청값이 누락되었습니다."),
    INTERNAL_SERVER_ERROR("COMMON_005", "내부 서버 오류입니다.");

    private final String code;
    private final String message;

}
