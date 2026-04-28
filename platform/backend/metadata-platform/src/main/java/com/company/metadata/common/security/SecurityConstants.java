package com.company.metadata.common.security;
/**
 * 安全基础组件，负责当前用户、权限和数据范围处理。
 *
 * 作者：Punjab
 */

public final class SecurityConstants {

    public static final String DEFAULT_TENANT_ID = "default";
    public static final String ADMIN_PERMISSION = "system:admin";

    private SecurityConstants() {
    }
}
