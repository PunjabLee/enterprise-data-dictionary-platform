package com.company.metadata.system.interfaces.rest.dto;

import com.company.metadata.system.domain.model.DataScope;
import java.time.Instant;
/**
 * 系统管理接口数据对象，定义请求或响应数据结构。
 *
 * 作者：Punjab
 */

public record DataScopeResponse(
        Long id,
        String tenantId,
        String subjectType,
        Long subjectId,
        String scopeType,
        String scopeValue,
        Instant createdAt
) {

    public static DataScopeResponse from(DataScope dataScope) {
        return new DataScopeResponse(
                dataScope.id(),
                dataScope.tenantId(),
                dataScope.subjectType().name(),
                dataScope.subjectId(),
                dataScope.scopeType().name(),
                dataScope.scopeValue(),
                dataScope.createdAt()
        );
    }
}
