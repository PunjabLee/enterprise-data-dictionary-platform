package com.company.metadata.system.interfaces.rest.dto;

import com.company.metadata.system.domain.model.Permission;
/**
 * 系统管理接口数据对象，定义请求或响应数据结构。
 *
 * 作者：Punjab
 */

public record PermissionResponse(
        Long id,
        String code,
        String name,
        String category,
        String resource,
        String action,
        String description,
        boolean enabled
) {

    public static PermissionResponse from(Permission permission) {
        return new PermissionResponse(
                permission.id(),
                permission.code(),
                permission.name(),
                permission.category(),
                permission.resource(),
                permission.action(),
                permission.description(),
                permission.enabled()
        );
    }
}
