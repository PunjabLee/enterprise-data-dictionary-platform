package com.company.metadata.system.domain.repository;

import com.company.metadata.system.domain.model.AuditLog;
import java.time.Instant;
import java.util.List;
/**
 * 系统管理仓储接口，隔离应用层和持久化实现。
 *
 * 作者：Punjab
 */

public interface AuditLogRepository {

    AuditLog save(AuditLog auditLog);

    List<AuditLog> search(
            String tenantId,
            Long actorId,
            String action,
            String objectType,
            Instant from,
            Instant to,
            int limit,
            int offset
    );
}
