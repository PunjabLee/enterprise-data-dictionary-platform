package com.company.metadata.system.domain.model;

import java.time.Instant;
/**
 * 系统管理领域模型，表达系统基础资料和权限关系。
 *
 * 作者：Punjab
 */

public record Permission(
        Long id,
        String tenantId,
        String code,
        String name,
        String category,
        String resource,
        String action,
        String description,
        boolean enabled,
        Instant createdAt,
        Instant updatedAt
) {
}
