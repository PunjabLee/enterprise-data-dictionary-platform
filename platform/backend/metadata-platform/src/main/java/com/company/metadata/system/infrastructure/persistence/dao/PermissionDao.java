package com.company.metadata.system.infrastructure.persistence.dao;

import com.company.metadata.system.infrastructure.config.SystemPersistenceEnabled;
import com.company.metadata.system.infrastructure.persistence.mapper.PermissionMapper;
import com.company.metadata.system.infrastructure.persistence.record.PermissionRecord;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Repository;

/**
 * 系统管理 DAO 组件，封装 MyBatis Mapper 调用。
 *
 * 作者：Punjab
 */
@Repository
@SystemPersistenceEnabled
public class PermissionDao {

    private final PermissionMapper permissionMapper;

    public PermissionDao(PermissionMapper permissionMapper) {
        this.permissionMapper = permissionMapper;
    }

    public List<PermissionRecord> selectAll(String tenantId, boolean includeDisabled) {
        return permissionMapper.selectAll(tenantId, includeDisabled);
    }

    public Set<String> selectCodesByUserId(String tenantId, Long userId) {
        return permissionMapper.selectCodesByUserId(tenantId, userId);
    }

    public void replaceRolePermissions(String tenantId, Long roleId, Set<String> permissionCodes) {
        permissionMapper.deleteRolePermissions(tenantId, roleId);
        permissionCodes.forEach(permissionCode ->
                permissionMapper.insertRolePermissionByCode(tenantId, roleId, permissionCode));
    }
}
