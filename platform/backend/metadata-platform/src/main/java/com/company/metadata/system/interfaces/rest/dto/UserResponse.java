package com.company.metadata.system.interfaces.rest.dto;

import com.company.metadata.system.domain.model.User;
import java.time.Instant;
/**
 * 系统管理接口数据对象，定义请求或响应数据结构。
 *
 * 作者：Punjab
 */

public record UserResponse(
        Long id,
        String tenantId,
        Long organizationId,
        String username,
        String displayName,
        String email,
        String mobile,
        String status,
        String source,
        Instant createdAt,
        Instant updatedAt
) {

    public static UserResponse from(User user) {
        return new UserResponse(
                user.id(),
                user.tenantId(),
                user.organizationId(),
                user.username(),
                user.displayName(),
                user.email(),
                user.mobile(),
                user.status(),
                user.source(),
                user.createdAt(),
                user.updatedAt()
        );
    }
}
