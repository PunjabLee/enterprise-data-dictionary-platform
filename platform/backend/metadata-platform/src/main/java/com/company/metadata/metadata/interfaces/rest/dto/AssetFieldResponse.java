package com.company.metadata.metadata.interfaces.rest.dto;

import com.company.metadata.metadata.domain.model.MetadataField;
import java.time.Instant;

/**
 * 资产字段响应对象，用于字段清单和字段字典列表。
 *
 * 作者：Punjab
 */
public record AssetFieldResponse(
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
        Instant createdAt,
        Instant updatedAt
) {

    public static AssetFieldResponse from(MetadataField field) {
        return new AssetFieldResponse(
                field.id(),
                field.tenantId(),
                field.assetId(),
                field.ordinal(),
                field.fieldName(),
                field.displayName(),
                field.dataType(),
                field.lengthValue(),
                field.precisionValue(),
                field.nullable(),
                field.primaryKey(),
                field.keyField(),
                field.sensitive(),
                field.classificationLevel(),
                field.businessDefinition(),
                field.businessRule(),
                field.standardCode(),
                field.termCode(),
                field.ownerUserId(),
                field.stewardUserId(),
                field.status(),
                field.createdAt(),
                field.updatedAt()
        );
    }
}
