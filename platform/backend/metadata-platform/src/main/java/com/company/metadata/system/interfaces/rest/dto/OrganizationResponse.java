package com.company.metadata.system.interfaces.rest.dto;

import com.company.metadata.system.domain.model.Organization;
import java.time.Instant;
/**
 * 系统管理接口数据对象，定义请求或响应数据结构。
 *
 * 作者：Punjab
 */

public record OrganizationResponse(
        Long id,
        String tenantId,
        Long parentId,
        String code,
        String name,
        String status,
        Instant createdAt,
        Instant updatedAt
) {

    public static OrganizationResponse from(Organization organization) {
        return new OrganizationResponse(
                organization.id(),
                organization.tenantId(),
                organization.parentId(),
                organization.code(),
                organization.name(),
                organization.status(),
                organization.createdAt(),
                organization.updatedAt()
        );
    }
}
