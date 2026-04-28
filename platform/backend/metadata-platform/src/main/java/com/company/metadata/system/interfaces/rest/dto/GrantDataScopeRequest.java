package com.company.metadata.system.interfaces.rest.dto;

import com.company.metadata.system.domain.model.DataScope;
import jakarta.validation.constraints.NotNull;
/**
 * 系统管理接口数据对象，定义请求或响应数据结构。
 *
 * 作者：Punjab
 */

public record GrantDataScopeRequest(
        @NotNull DataScope.ScopeType scopeType,
        String scopeValue
) {
}
