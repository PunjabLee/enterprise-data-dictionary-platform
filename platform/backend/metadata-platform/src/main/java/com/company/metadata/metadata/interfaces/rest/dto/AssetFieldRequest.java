package com.company.metadata.metadata.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 资产字段维护请求，覆盖字段字典首期需要的核心属性。
 *
 * 作者：Punjab
 */
public record AssetFieldRequest(
        @NotBlank String fieldName,
        String displayName,
        String dataType,
        Integer lengthValue,
        Integer precisionValue,
        Boolean nullable,
        Boolean primaryKey,
        Boolean keyField,
        Boolean sensitive,
        String classificationLevel,
        String businessDefinition,
        String businessRule,
        String standardCode,
        String termCode,
        Long ownerUserId,
        Long stewardUserId
) {
}
