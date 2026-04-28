package com.company.metadata.common.security;
/**
 * 安全基础组件，负责当前用户、权限和数据范围处理。
 *
 * 作者：Punjab
 */

public interface PermissionChecker {

    boolean hasPermission(String permissionCode);

    default void requirePermission(String permissionCode) {
        if (!hasPermission(permissionCode)) {
            throw new PermissionDeniedException("Missing permission: " + permissionCode);
        }
    }
}
