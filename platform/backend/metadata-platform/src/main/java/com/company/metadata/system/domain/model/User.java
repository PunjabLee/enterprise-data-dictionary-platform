package com.company.metadata.system.domain.model;

import java.time.Instant;
/**
 * 系统管理领域模型，表达系统基础资料和权限关系。
 *
 * 作者：Punjab
 */

public record User(
        Long id,
        String tenantId,
        Long organizationId,
        String username,
        String displayName,
        String email,
        String mobile,
        String status,
        String source,
        String externalId,
        Instant createdAt,
        Instant updatedAt
) {

    public User disable() {
        return new User(
                id,
                tenantId,
                organizationId,
                username,
                displayName,
                email,
                mobile,
                "disabled",
                source,
                externalId,
                createdAt,
                updatedAt
        );
    }
}
