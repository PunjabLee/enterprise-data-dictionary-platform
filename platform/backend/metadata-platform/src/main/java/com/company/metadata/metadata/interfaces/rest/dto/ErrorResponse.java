package com.company.metadata.metadata.interfaces.rest.dto;

import java.time.Instant;

/**
 * 元数据 REST 错误响应，保持接口错误结构稳定。
 *
 * 作者：Punjab
 */
public record ErrorResponse(
        String code,
        String message,
        Instant timestamp
) {

    public static ErrorResponse of(String code, String message) {
        return new ErrorResponse(code, message, Instant.now());
    }
}
