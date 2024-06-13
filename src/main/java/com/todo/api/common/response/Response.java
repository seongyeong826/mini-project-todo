package com.todo.api.common.response;

import com.todo.api.common.exception.ExceptionCode;
import lombok.Getter;

@Getter
public class Response<T> {

    private final boolean success;
    private final T data;
    private final ErrorResponse error;

    private Response(boolean success, T data) {
        this.success = success;
        this.data = data;
        this.error = null;
    }

    private Response(boolean success, ErrorResponse error) {
        this.success = success;
        this.data = null;
        this.error = error;
    }

    public static <T> Response<T> success(T data) {
        return new Response<>(true, data);
    }

    public static <T> Response<T> fail(ExceptionCode code) {
        return new Response<>(false, new ErrorResponse(code.getCode(), code.getMessage()));
    }
}
