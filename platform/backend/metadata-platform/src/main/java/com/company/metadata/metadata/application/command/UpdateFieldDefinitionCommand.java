package com.company.metadata.metadata.application.command;

/**
 * 字段字典维护命令，聚合业务定义、分类分级和标准预留引用。
 *
 * 作者：Punjab
 */
public record UpdateFieldDefinitionCommand(
        Long fieldId,
        String displayName,
        String businessDefinition,
        String businessRule,
        Boolean keyField,
        Boolean sensitive,
        String classificationLevel,
        String standardCode,
        String termCode,
        Long ownerUserId,
        Long stewardUserId
) {
}
