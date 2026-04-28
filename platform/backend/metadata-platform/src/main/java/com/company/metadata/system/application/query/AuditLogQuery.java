package com.company.metadata.system.application.query;

import java.time.Instant;
/**
 * 系统管理查询视图，承载接口层返回的查询结果。
 *
 * 作者：Punjab
 */

public record AuditLogQuery(
        Long actorId,
        String action,
        String objectType,
        Instant from,
        Instant to,
        int limit,
        int offset
) {
}
