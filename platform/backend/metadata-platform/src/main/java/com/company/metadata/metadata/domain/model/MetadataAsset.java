package com.company.metadata.metadata.domain.model;

import java.time.Instant;

/**
 * 元数据资产领域模型，表示系统、表、接口、报表等可治理对象。
 *
 * 作者：Punjab
 */
public record MetadataAsset(
        Long id,
        String tenantId,
        String assetType,
        String code,
        String name,
        String displayName,
        String description,
        String businessDomain,
        String systemCode,
        Long ownerUserId,
        Long stewardUserId,
        String status,
        Integer version,
        String sourceType,
        String attributesJson,
        String createdBy,
        String updatedBy,
        Instant createdAt,
        Instant updatedAt
) {

    public MetadataAsset publish(String operator) {
        int nextVersion = version == null ? 1 : version + 1;
        return new MetadataAsset(
                id,
                tenantId,
                assetType,
                code,
                name,
                displayName,
                description,
                businessDomain,
                systemCode,
                ownerUserId,
                stewardUserId,
                "published",
                nextVersion,
                sourceType,
                attributesJson,
                createdBy,
                operator,
                createdAt,
                updatedAt
        );
    }
}
