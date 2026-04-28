package com.company.metadata.system.domain.repository;

import com.company.metadata.system.domain.model.Permission;
import java.util.List;
import java.util.Set;
/**
 * 系统管理仓储接口，隔离应用层和持久化实现。
 *
 * 作者：Punjab
 */

public interface PermissionRepository {

    List<Permission> findAll(String tenantId, boolean includeDisabled);

    Set<String> findPermissionCodesByUserId(String tenantId, Long userId);

    void replaceRolePermissions(String tenantId, Long roleId, Set<String> permissionCodes);
}
