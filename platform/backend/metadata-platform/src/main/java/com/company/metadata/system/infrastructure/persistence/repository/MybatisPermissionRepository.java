package com.company.metadata.system.infrastructure.persistence.repository;

import com.company.metadata.system.domain.model.Permission;
import com.company.metadata.system.domain.repository.PermissionRepository;
import com.company.metadata.system.infrastructure.config.SystemPersistenceEnabled;
import com.company.metadata.system.infrastructure.persistence.dao.PermissionDao;
import com.company.metadata.system.infrastructure.persistence.record.PermissionRecord;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Repository;

/**
 * 系统管理仓储实现，完成领域模型与持久化记录转换。
 *
 * 作者：Punjab
 */
@Repository
@SystemPersistenceEnabled
public class MybatisPermissionRepository implements PermissionRepository {

    private final PermissionDao permissionDao;

    public MybatisPermissionRepository(PermissionDao permissionDao) {
        this.permissionDao = permissionDao;
    }

    @Override
    public List<Permission> findAll(String tenantId, boolean includeDisabled) {
        return permissionDao.selectAll(tenantId, includeDisabled).stream()
                .map(MybatisPermissionRepository::toDomain)
                .toList();
    }

    @Override
    public Set<String> findPermissionCodesByUserId(String tenantId, Long userId) {
        return permissionDao.selectCodesByUserId(tenantId, userId);
    }

    @Override
    public void replaceRolePermissions(String tenantId, Long roleId, Set<String> permissionCodes) {
        permissionDao.replaceRolePermissions(tenantId, roleId, permissionCodes);
    }

    private static Permission toDomain(PermissionRecord record) {
        return new Permission(
                record.id,
                record.tenantId,
                record.code,
                record.name,
                record.category,
                record.resource,
                record.action,
                record.description,
                record.enabled,
                record.createdAt,
                record.updatedAt
        );
    }
}
