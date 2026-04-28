package com.company.metadata.system.infrastructure.persistence.repository;

import com.company.metadata.system.domain.model.User;
import com.company.metadata.system.domain.repository.UserRepository;
import com.company.metadata.system.infrastructure.config.SystemPersistenceEnabled;
import com.company.metadata.system.infrastructure.persistence.dao.UserDao;
import com.company.metadata.system.infrastructure.persistence.record.UserRecord;
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
public class MybatisUserRepository implements UserRepository {

    private final UserDao userDao;

    public MybatisUserRepository(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User save(User user) {
        return toDomain(userDao.insert(toRecord(user)));
    }

    @Override
    public void update(User user) {
        userDao.update(toRecord(user));
    }

    @Override
    public Optional<User> findById(String tenantId, Long id) {
        return Optional.ofNullable(userDao.selectById(tenantId, id)).map(MybatisUserRepository::toDomain);
    }

    @Override
    public List<User> search(String tenantId, String keyword, String status, int limit, int offset) {
        return userDao.search(tenantId, keyword, status, limit, offset)
                .stream()
                .map(MybatisUserRepository::toDomain)
                .toList();
    }

    @Override
    public void replaceUserRoles(String tenantId, Long userId, Set<String> roleCodes) {
        userDao.replaceUserRoles(tenantId, userId, roleCodes);
    }

    private static UserRecord toRecord(User user) {
        UserRecord record = new UserRecord();
        record.id = user.id();
        record.tenantId = user.tenantId();
        record.organizationId = user.organizationId();
        record.username = user.username();
        record.displayName = user.displayName();
        record.email = user.email();
        record.mobile = user.mobile();
        record.status = user.status();
        record.source = user.source();
        record.externalId = user.externalId();
        record.createdAt = user.createdAt();
        record.updatedAt = user.updatedAt();
        return record;
    }

    private static User toDomain(UserRecord record) {
        return new User(
                record.id,
                record.tenantId,
                record.organizationId,
                record.username,
                record.displayName,
                record.email,
                record.mobile,
                record.status,
                record.source,
                record.externalId,
                record.createdAt,
                record.updatedAt
        );
    }
}
