package com.company.metadata.system.domain.model;

import java.time.Instant;
/**
 * 系统管理领域模型，表达系统基础资料和权限关系。
 *
 * 作者：Punjab
 */

public record Organization(
        Long id,
        String tenantId,
        Long parentId,
        String code,
        String name,
        String status,
        Instant createdAt,
        Instant updatedAt
) {
}
