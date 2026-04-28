package com.company.metadata.system.infrastructure.persistence.repository;

import com.company.metadata.system.domain.model.DataScope;
import com.company.metadata.system.domain.repository.DataScopeRepository;
import com.company.metadata.system.infrastructure.config.SystemPersistenceEnabled;
import com.company.metadata.system.infrastructure.persistence.dao.DataScopeDao;
import com.company.metadata.system.infrastructure.persistence.record.DataScopeRecord;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * 系统管理仓储实现，完成领域模型与持久化记录转换。
 *
 * 作者：Punjab
 */
@Repository
@SystemPersistenceEnabled
public class MybatisDataScopeRepository implements DataScopeRepository {

    private final DataScopeDao dataScopeDao;

    public MybatisDataScopeRepository(DataScopeDao dataScopeDao) {
        this.dataScopeDao = dataScopeDao;
    }

    @Override
    public DataScope save(DataScope dataScope) {
        return toDomain(dataScopeDao.insert(toRecord(dataScope)));
    }

    @Override
    public List<DataScope> findBySubject(String tenantId, DataScope.SubjectType subjectType, Long subjectId) {
        return dataScopeDao.findBySubject(tenantId, subjectType.name(), subjectId)
                .stream()
                .map(MybatisDataScopeRepository::toDomain)
                .toList();
    }

    @Override
    public List<DataScope> findEffectiveScopes(String tenantId, Long userId) {
        return dataScopeDao.findEffectiveScopes(tenantId, userId)
                .stream()
                .map(MybatisDataScopeRepository::toDomain)
                .toList();
    }

    private static DataScopeRecord toRecord(DataScope dataScope) {
        DataScopeRecord record = new DataScopeRecord();
        record.id = dataScope.id();
        record.tenantId = dataScope.tenantId();
        record.subjectType = dataScope.subjectType().name();
        record.subjectId = dataScope.subjectId();
        record.scopeType = dataScope.scopeType().name();
        record.scopeValue = dataScope.scopeValue();
        record.createdAt = dataScope.createdAt();
        return record;
    }

    private static DataScope toDomain(DataScopeRecord record) {
        return new DataScope(
                record.id,
                record.tenantId,
                DataScope.SubjectType.valueOf(record.subjectType),
                record.subjectId,
                DataScope.ScopeType.valueOf(record.scopeType),
                record.scopeValue,
                record.createdAt
        );
    }
}
