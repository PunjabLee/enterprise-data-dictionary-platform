package com.company.metadata.system.domain.model;

import java.time.Instant;
/**
 * 系统管理领域模型，表达系统基础资料和权限关系。
 *
 * 作者：Punjab
 */

public record AuditLog(
        Long id,
        String tenantId,
        Long actorId,
        String actorName,
        String action,
        String objectType,
        String objectId,
        String beforeValue,
        String afterValue,
        String result,
        String ip,
        String userAgent,
        String traceId,
        Instant createdAt
) {
}
