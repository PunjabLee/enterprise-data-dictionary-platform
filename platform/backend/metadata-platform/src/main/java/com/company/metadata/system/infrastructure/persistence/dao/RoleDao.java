package com.company.metadata.system.infrastructure.persistence.dao;

import com.company.metadata.system.infrastructure.config.SystemPersistenceEnabled;
import com.company.metadata.system.infrastructure.persistence.mapper.RoleMapper;
import com.company.metadata.system.infrastructure.persistence.record.RoleRecord;
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
public class RoleDao {

    private final RoleMapper roleMapper;

    public RoleDao(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    public RoleRecord insert(RoleRecord record) {
        roleMapper.insert(record);
        return record;
    }

    public RoleRecord selectById(String tenantId, Long id) {
        return roleMapper.selectById(tenantId, id);
    }

    public List<RoleRecord> selectAll(String tenantId, boolean includeDisabled) {
        return roleMapper.selectAll(tenantId, includeDisabled);
    }

    public Set<String> selectRoleCodesByUserId(String tenantId, Long userId) {
        return roleMapper.selectRoleCodesByUserId(tenantId, userId);
    }
}
