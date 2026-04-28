package com.company.metadata.system.application.command;

import com.company.metadata.system.domain.model.DataScope;
/**
 * 系统管理应用命令对象，承载接口层传入的业务指令。
 *
 * 作者：Punjab
 */

public record GrantDataScopeCommand(
        DataScope.SubjectType subjectType,
        Long subjectId,
        DataScope.ScopeType scopeType,
        String scopeValue
) {
}
