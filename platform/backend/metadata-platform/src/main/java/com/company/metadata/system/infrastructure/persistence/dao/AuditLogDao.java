package com.company.metadata.system.infrastructure.persistence.dao;

import com.company.metadata.system.infrastructure.config.SystemPersistenceEnabled;
import com.company.metadata.system.infrastructure.persistence.mapper.AuditLogMapper;
import com.company.metadata.system.infrastructure.persistence.record.AuditLogRecord;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * 系统管理 DAO 组件，封装 MyBatis Mapper 调用。
 *
 * 作者：Punjab
 */
@Repository
@SystemPersistenceEnabled
public class AuditLogDao {

    private final AuditLogMapper auditLogMapper;

    public AuditLogDao(AuditLogMapper auditLogMapper) {
        this.auditLogMapper = auditLogMapper;
    }

    public AuditLogRecord insert(AuditLogRecord record) {
        auditLogMapper.insert(record);
        return record;
    }

    public List<AuditLogRecord> search(
            String tenantId,
            Long actorId,
            String action,
            String objectType,
            Instant from,
            Instant to,
            int limit,
            int offset
    ) {
        return auditLogMapper.search(tenantId, actorId, action, objectType, from, to, limit, offset);
    }
}
