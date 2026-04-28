package com.company.metadata.system.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;
/**
 * 系统管理接口数据对象，定义请求或响应数据结构。
 *
 * 作者：Punjab
 */

public record CreateRoleRequest(
        @NotBlank String code,
        @NotBlank String name,
        String description
) {
}
