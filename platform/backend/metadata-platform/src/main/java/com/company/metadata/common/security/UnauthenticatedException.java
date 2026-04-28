package com.company.metadata.common.security;

import org.springframework.security.core.AuthenticationException;
/**
 * 安全基础组件，负责当前用户、权限和数据范围处理。
 *
 * 作者：Punjab
 */

public class UnauthenticatedException extends AuthenticationException {

    public UnauthenticatedException(String message) {
        super(message);
    }
}
