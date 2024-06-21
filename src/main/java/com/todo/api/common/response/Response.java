package com.todo.api.common.response;

import com.todo.api.common.exception.ExceptionCode;
import lombok.Getter;

@Getter
public class Response<T> {

    private final T data;
    private final ErrorResponse error;

    private Response(T data) {
        this.data = data;
        this.error = null;
    }

    private Response(ErrorResponse error) {
        this.data = null;
        this.error = error;
    }

    public static <T> Response<T> success(T data) {
        return new Response<>(data);
    }

    public static <T> Response<T> fail(ExceptionCode code) {
        return new Response<>(new ErrorResponse(code.getCode(), code.getMessage()));
    }
}
