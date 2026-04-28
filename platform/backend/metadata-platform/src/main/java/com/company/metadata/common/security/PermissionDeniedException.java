package com.company.metadata.common.security;

import org.springframework.security.access.AccessDeniedException;
/**
 * 安全基础组件，负责当前用户、权限和数据范围处理。
 *
 * 作者：Punjab
 */

public class PermissionDeniedException extends AccessDeniedException {

    public PermissionDeniedException(String message) {
        super(message);
    }
}
