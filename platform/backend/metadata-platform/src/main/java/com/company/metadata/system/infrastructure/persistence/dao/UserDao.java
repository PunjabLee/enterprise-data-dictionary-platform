package com.company.metadata.system.infrastructure.persistence.dao;

import com.company.metadata.system.infrastructure.config.SystemPersistenceEnabled;
import com.company.metadata.system.infrastructure.persistence.mapper.UserMapper;
import com.company.metadata.system.infrastructure.persistence.record.UserRecord;
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
public class UserDao {

    private final UserMapper userMapper;

    public UserDao(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public UserRecord insert(UserRecord record) {
        userMapper.insert(record);
        return record;
    }

    public void update(UserRecord record) {
        userMapper.update(record);
    }

    public UserRecord selectById(String tenantId, Long id) {
        return userMapper.selectById(tenantId, id);
    }

    public List<UserRecord> search(String tenantId, String keyword, String status, int limit, int offset) {
        return userMapper.search(tenantId, keyword, status, limit, offset);
    }

    public void replaceUserRoles(String tenantId, Long userId, Set<String> roleCodes) {
        userMapper.deleteUserRoles(tenantId, userId);
        roleCodes.forEach(roleCode -> userMapper.insertUserRoleByCode(tenantId, userId, roleCode));
    }
}
