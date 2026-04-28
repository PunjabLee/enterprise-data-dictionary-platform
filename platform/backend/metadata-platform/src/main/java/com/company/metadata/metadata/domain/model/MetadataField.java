package com.company.metadata.metadata.domain.model;

import java.time.Instant;

/**
 * 资产字段领域模型，承载字段字典和分类分级信息。
 *
 * 作者：Punjab
 */
public record MetadataField(
        Long id,
        String tenantId,
        Long assetId,
        Integer ordinal,
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
        Long stewardUserId,
        String status,
        String createdBy,
        String updatedBy,
        Instant createdAt,
        Instant updatedAt
) {

    public MetadataField updateDefinition(
            String displayName,
            String businessDefinition,
            String businessRule,
            Boolean keyField,
            Boolean sensitive,
            String classificationLevel,
            String standardCode,
            String termCode,
            Long ownerUserId,
            Long stewardUserId,
            String operator
    ) {
        return new MetadataField(
                id,
                tenantId,
                assetId,
                ordinal,
                fieldName,
                valueOrCurrent(displayName, this.displayName),
                dataType,
                lengthValue,
                precisionValue,
                nullable,
                primaryKey,
                valueOrCurrent(keyField, this.keyField),
                valueOrCurrent(sensitive, this.sensitive),
                valueOrCurrent(classificationLevel, this.classificationLevel),
                valueOrCurrent(businessDefinition, this.businessDefinition),
                valueOrCurrent(businessRule, this.businessRule),
                valueOrCurrent(standardCode, this.standardCode),
                valueOrCurrent(termCode, this.termCode),
                valueOrCurrent(ownerUserId, this.ownerUserId),
                valueOrCurrent(stewardUserId, this.stewardUserId),
                status,
                createdBy,
                operator,
                createdAt,
                updatedAt
        );
    }

    private static <T> T valueOrCurrent(T value, T current) {
        return value == null ? current : value;
    }
}
