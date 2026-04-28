package com.company.metadata.metadata.interfaces.rest;

import com.company.metadata.common.security.PermissionDeniedException;
import com.company.metadata.common.security.UnauthenticatedException;
import com.company.metadata.metadata.interfaces.rest.dto.ErrorResponse;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 元数据 REST 异常处理器，统一资产和字段字典接口错误响应。
 *
 * 作者：Punjab
 */
@RestControllerAdvice(basePackages = "com.company.metadata.metadata.interfaces.rest")
public class MetadataApiExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    ResponseEntity<ErrorResponse> handleNotFound(NoSuchElementException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.of("not_found", exception.getMessage()));
    }

    @ExceptionHandler(UnauthenticatedException.class)
    ResponseEntity<ErrorResponse> handleUnauthorized(UnauthenticatedException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.of("unauthorized", exception.getMessage()));
    }

    @ExceptionHandler(PermissionDeniedException.class)
    ResponseEntity<ErrorResponse> handleForbidden(PermissionDeniedException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ErrorResponse.of("forbidden", exception.getMessage()));
    }

    @ExceptionHandler({IllegalArgumentException.class, MethodArgumentNotValidException.class})
    ResponseEntity<ErrorResponse> handleBadRequest(Exception exception) {
        return ResponseEntity.badRequest()
                .body(ErrorResponse.of("bad_request", exception.getMessage()));
    }
}
