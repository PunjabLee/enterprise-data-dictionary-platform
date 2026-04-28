package com.company.metadata.system.domain.repository;

import com.company.metadata.system.domain.model.User;
import java.util.List;
import java.util.Optional;
import java.util.Set;
/**
 * 系统管理仓储接口，隔离应用层和持久化实现。
 *
 * 作者：Punjab
 */

public interface UserRepository {

    User save(User user);

    void update(User user);

    Optional<User> findById(String tenantId, Long id);

    List<User> search(String tenantId, String keyword, String status, int limit, int offset);

    void replaceUserRoles(String tenantId, Long userId, Set<String> roleCodes);
}
