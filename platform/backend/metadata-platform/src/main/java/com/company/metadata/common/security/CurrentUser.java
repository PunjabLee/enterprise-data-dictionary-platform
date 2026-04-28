package com.company.metadata.common.security;

import java.util.Set;
/**
 * 安全基础组件，负责当前用户、权限和数据范围处理。
 *
 * 作者：Punjab
 */

public record CurrentUser(
        String tenantId,
        Long userId,
        String username,
        String displayName,
        Set<String> roleCodes,
        Set<String> permissionCodes
) {

    public CurrentUser {
        if (tenantId == null || tenantId.isBlank()) {
            tenantId = SecurityConstants.DEFAULT_TENANT_ID;
        }
        roleCodes = roleCodes == null ? Set.of() : Set.copyOf(roleCodes);
        permissionCodes = permissionCodes == null ? Set.of() : Set.copyOf(permissionCodes);
    }

    public boolean hasPermission(String permissionCode) {
        return permissionCodes.contains(permissionCode)
                || permissionCodes.contains(SecurityConstants.ADMIN_PERMISSION);
    }
}
