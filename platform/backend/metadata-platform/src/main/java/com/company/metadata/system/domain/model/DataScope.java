package com.company.metadata.system.domain.model;

import java.time.Instant;
/**
 * 系统管理领域模型，表达系统基础资料和权限关系。
 *
 * 作者：Punjab
 */

public record DataScope(
        Long id,
        String tenantId,
        SubjectType subjectType,
        Long subjectId,
        ScopeType scopeType,
        String scopeValue,
        Instant createdAt
) {

    public enum SubjectType {
        USER,
        ROLE
    }

    public enum ScopeType {
        ALL,
        BUSINESS_DOMAIN,
        SYSTEM,
        ORGANIZATION,
        ASSET_TYPE
    }
}
