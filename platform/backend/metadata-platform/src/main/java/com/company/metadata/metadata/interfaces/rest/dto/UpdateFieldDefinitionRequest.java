package com.company.metadata.metadata.interfaces.rest.dto;

/**
 * 字段业务定义更新请求，首期预留标准和术语编码引用。
 *
 * 作者：Punjab
 */
public record UpdateFieldDefinitionRequest(
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
