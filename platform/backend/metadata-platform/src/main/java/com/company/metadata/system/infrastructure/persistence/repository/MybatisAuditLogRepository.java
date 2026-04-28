package com.company.metadata.system.infrastructure.persistence.repository;

import com.company.metadata.system.domain.model.AuditLog;
import com.company.metadata.system.domain.repository.AuditLogRepository;
import com.company.metadata.system.infrastructure.config.SystemPersistenceEnabled;
import com.company.metadata.system.infrastructure.persistence.dao.AuditLogDao;
import com.company.metadata.system.infrastructure.persistence.record.AuditLogRecord;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * 系统管理仓储实现，完成领域模型与持久化记录转换。
 *
 * 作者：Punjab
 */
@Repository
@SystemPersistenceEnabled
public class MybatisAuditLogRepository implements AuditLogRepository {

    private final AuditLogDao auditLogDao;

    public MybatisAuditLogRepository(AuditLogDao auditLogDao) {
        this.auditLogDao = auditLogDao;
    }

    @Override
    public AuditLog save(AuditLog auditLog) {
        return toDomain(auditLogDao.insert(toRecord(auditLog)));
    }

    @Override
    public List<AuditLog> search(
            String tenantId,
            Long actorId,
            String action,
            String objectType,
            Instant from,
            Instant to,
            int limit,
            int offset
    ) {
        return auditLogDao.search(tenantId, actorId, action, objectType, from, to, limit, offset)
                .stream()
                .map(MybatisAuditLogRepository::toDomain)
                .toList();
    }

    private static AuditLogRecord toRecord(AuditLog auditLog) {
        AuditLogRecord record = new AuditLogRecord();
        record.id = auditLog.id();
        record.tenantId = auditLog.tenantId();
        record.actorId = auditLog.actorId();
        record.actorName = auditLog.actorName();
        record.action = auditLog.action();
        record.objectType = auditLog.objectType();
        record.objectId = auditLog.objectId();
        record.beforeValue = auditLog.beforeValue();
        record.afterValue = auditLog.afterValue();
        record.result = auditLog.result();
        record.ip = auditLog.ip();
        record.userAgent = auditLog.userAgent();
        record.traceId = auditLog.traceId();
        record.createdAt = auditLog.createdAt();
        return record;
    }

    private static AuditLog toDomain(AuditLogRecord record) {
        return new AuditLog(
                record.id,
                record.tenantId,
                record.actorId,
                record.actorName,
                record.action,
                record.objectType,
                record.objectId,
                record.beforeValue,
                record.afterValue,
                record.result,
                record.ip,
                record.userAgent,
                record.traceId,
                record.createdAt
        );
    }
}
