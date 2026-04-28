package com.company.metadata.system.interfaces.rest;

import com.company.metadata.system.interfaces.rest.dto.ErrorResponse;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 系统管理 REST 接口，暴露用户、角色、权限、审计等基础能力。
 *
 * 作者：Punjab
 */
@RestControllerAdvice(basePackages = "com.company.metadata.system.interfaces.rest")
public class SystemApiExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    ResponseEntity<ErrorResponse> handleNotFound(NoSuchElementException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.of("not_found", exception.getMessage()));
    }

    @ExceptionHandler({IllegalArgumentException.class, MethodArgumentNotValidException.class})
    ResponseEntity<ErrorResponse> handleBadRequest(Exception exception) {
        return ResponseEntity.badRequest()
                .body(ErrorResponse.of("bad_request", exception.getMessage()));
    }
}
