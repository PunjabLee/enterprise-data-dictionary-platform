package com.company.metadata.system.interfaces.rest.dto;

import com.company.metadata.system.domain.model.Role;
import java.time.Instant;
/**
 * 系统管理接口数据对象，定义请求或响应数据结构。
 *
 * 作者：Punjab
 */

public record RoleResponse(
        Long id,
        String tenantId,
        String code,
        String name,
        String description,
        boolean builtIn,
        boolean enabled,
        Instant createdAt,
        Instant updatedAt
) {

    public static RoleResponse from(Role role) {
        return new RoleResponse(
                role.id(),
                role.tenantId(),
                role.code(),
                role.name(),
                role.description(),
                role.builtIn(),
                role.enabled(),
                role.createdAt(),
                role.updatedAt()
        );
    }
}
