package com.company.metadata.system.domain.repository;

import com.company.metadata.system.domain.model.Role;
import java.util.List;
import java.util.Optional;
import java.util.Set;
/**
 * 系统管理仓储接口，隔离应用层和持久化实现。
 *
 * 作者：Punjab
 */

public interface RoleRepository {

    Role save(Role role);

    Optional<Role> findById(String tenantId, Long id);

    List<Role> findAll(String tenantId, boolean includeDisabled);

    Set<String> findRoleCodesByUserId(String tenantId, Long userId);
}
