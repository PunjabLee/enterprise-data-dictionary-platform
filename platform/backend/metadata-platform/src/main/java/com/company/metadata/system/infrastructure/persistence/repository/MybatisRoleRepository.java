package com.company.metadata.system.infrastructure.persistence.repository;

import com.company.metadata.system.domain.model.Role;
import com.company.metadata.system.domain.repository.RoleRepository;
import com.company.metadata.system.infrastructure.config.SystemPersistenceEnabled;
import com.company.metadata.system.infrastructure.persistence.dao.RoleDao;
import com.company.metadata.system.infrastructure.persistence.record.RoleRecord;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.stereotype.Repository;

/**
 * 系统管理仓储实现，完成领域模型与持久化记录转换。
 *
 * 作者：Punjab
 */
@Repository
@SystemPersistenceEnabled
public class MybatisRoleRepository implements RoleRepository {

    private final RoleDao roleDao;

    public MybatisRoleRepository(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    public Role save(Role role) {
        return toDomain(roleDao.insert(toRecord(role)));
    }

    @Override
    public Optional<Role> findById(String tenantId, Long id) {
        return Optional.ofNullable(roleDao.selectById(tenantId, id)).map(MybatisRoleRepository::toDomain);
    }

    @Override
    public List<Role> findAll(String tenantId, boolean includeDisabled) {
        return roleDao.selectAll(tenantId, includeDisabled).stream()
                .map(MybatisRoleRepository::toDomain)
                .toList();
    }

    @Override
    public Set<String> findRoleCodesByUserId(String tenantId, Long userId) {
        return roleDao.selectRoleCodesByUserId(tenantId, userId);
    }

    private static RoleRecord toRecord(Role role) {
        RoleRecord record = new RoleRecord();
        record.id = role.id();
        record.tenantId = role.tenantId();
        record.code = role.code();
        record.name = role.name();
        record.description = role.description();
        record.builtIn = role.builtIn();
        record.enabled = role.enabled();
        record.createdAt = role.createdAt();
        record.updatedAt = role.updatedAt();
        return record;
    }

    private static Role toDomain(RoleRecord record) {
        return new Role(
                record.id,
                record.tenantId,
                record.code,
                record.name,
                record.description,
                record.builtIn,
                record.enabled,
                record.createdAt,
                record.updatedAt
        );
    }
}
