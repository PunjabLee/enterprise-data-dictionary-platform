package com.company.metadata.metadata.application.command;

/**
 * 资产字段维护命令，用于人工补录或采集后的字段字典确认。
 *
 * 作者：Punjab
 */
public record UpsertAssetFieldCommand(
        Long assetId,
        String fieldName,
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
