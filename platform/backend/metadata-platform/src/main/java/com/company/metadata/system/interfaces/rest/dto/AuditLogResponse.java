package com.company.metadata.system.interfaces.rest.dto;

import com.company.metadata.system.domain.model.AuditLog;
import java.time.Instant;
/**
 * 系统管理接口数据对象，定义请求或响应数据结构。
 *
 * 作者：Punjab
 */

public record AuditLogResponse(
        Long id,
        String tenantId,
        Long actorId,
        String actorName,
        String action,
        String objectType,
        String objectId,
        String result,
        String ip,
        String traceId,
        Instant createdAt
) {

    public static AuditLogResponse from(AuditLog auditLog) {
        return new AuditLogResponse(
                auditLog.id(),
                auditLog.tenantId(),
                auditLog.actorId(),
                auditLog.actorName(),
                auditLog.action(),
                auditLog.objectType(),
                auditLog.objectId(),
                auditLog.result(),
                auditLog.ip(),
                auditLog.traceId(),
                auditLog.createdAt()
        );
    }
}
