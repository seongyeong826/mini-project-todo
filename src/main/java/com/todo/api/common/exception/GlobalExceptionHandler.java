package com.todo.api.common.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.todo.api.common.response.Response;
import io.jsonwebtoken.MalformedJwtException;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Response<Void>> handleCustomException(CustomException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(Response.fail(e.getCode()));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Response<Void>> handleNoSuchElementException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(Response.fail(ExceptionCode.NOT_FOUND));
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<Response<Void>> handleInvalidFormatException(InvalidFormatException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(Response.fail(ExceptionCode.INVALID_VALUE));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Response<Void>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(Response.fail(ExceptionCode.REQUEST_BODY_MISSING));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Response<Void>> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(Response.fail(ExceptionCode.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<Response<Void>> handleMalformedJwtExceptionException(MalformedJwtException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(Response.fail(ExceptionCode.INVALID_JWT_SIGNATURE));
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<Response<Void>> handleSecurityExceptionException(SecurityException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(Response.fail(ExceptionCode.INVALID_JWT_SIGNATURE));
    }

}
