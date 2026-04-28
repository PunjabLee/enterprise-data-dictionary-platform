package com.company.metadata.system.application.service;
/**
 * 系统管理应用服务，编排权限校验、领域仓储和审计记录。
 *
 * 作者：Punjab
 */

public final class SystemPermissionCodes {

    public static final String ORGANIZATION_READ = "system:organization:read";
    public static final String ORGANIZATION_CREATE = "system:organization:create";
    public static final String USER_READ = "system:user:read";
    public static final String USER_CREATE = "system:user:create";
    public static final String USER_UPDATE = "system:user:update";
    public static final String ROLE_READ = "system:role:read";
    public static final String ROLE_CREATE = "system:role:create";
    public static final String ROLE_UPDATE = "system:role:update";
    public static final String PERMISSION_READ = "system:permission:read";
    public static final String PERMISSION_UPDATE = "system:permission:update";
    public static final String DATA_SCOPE_READ = "system:data-scope:read";
    public static final String DATA_SCOPE_UPDATE = "system:data-scope:update";
    public static final String AUDIT_READ = "system:audit:read";

    private SystemPermissionCodes() {
    }
}
