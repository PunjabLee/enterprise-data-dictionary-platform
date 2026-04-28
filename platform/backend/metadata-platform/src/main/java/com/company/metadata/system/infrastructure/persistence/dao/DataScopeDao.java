package com.company.metadata.system.infrastructure.persistence.dao;

import com.company.metadata.system.infrastructure.config.SystemPersistenceEnabled;
import com.company.metadata.system.infrastructure.persistence.mapper.DataScopeMapper;
import com.company.metadata.system.infrastructure.persistence.record.DataScopeRecord;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * 系统管理 DAO 组件，封装 MyBatis Mapper 调用。
 *
 * 作者：Punjab
 */
@Repository
@SystemPersistenceEnabled
public class DataScopeDao {

    private final DataScopeMapper dataScopeMapper;

    public DataScopeDao(DataScopeMapper dataScopeMapper) {
        this.dataScopeMapper = dataScopeMapper;
    }

    public DataScopeRecord insert(DataScopeRecord record) {
        dataScopeMapper.insert(record);
        return record;
    }

    public List<DataScopeRecord> findBySubject(String tenantId, String subjectType, Long subjectId) {
        return dataScopeMapper.findBySubject(tenantId, subjectType, subjectId);
    }

    public List<DataScopeRecord> findEffectiveScopes(String tenantId, Long userId) {
        return dataScopeMapper.findEffectiveScopes(tenantId, userId);
    }
}
