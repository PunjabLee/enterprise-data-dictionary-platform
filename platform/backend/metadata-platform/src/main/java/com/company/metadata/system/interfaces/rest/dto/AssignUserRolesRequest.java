package com.company.metadata.system.interfaces.rest.dto;

import jakarta.validation.constraints.NotNull;
import java.util.Set;
/**
 * 系统管理接口数据对象，定义请求或响应数据结构。
 *
 * 作者：Punjab
 */

public record AssignUserRolesRequest(@NotNull Set<String> roleCodes) {
}
