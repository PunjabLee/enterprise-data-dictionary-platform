package com.company.metadata.metadata.interfaces.rest.dto;

import com.company.metadata.metadata.domain.model.MetadataAsset;
import java.time.Instant;

/**
 * 资产目录响应对象，返回资产治理状态和责任信息。
 *
 * 作者：Punjab
 */
public record AssetResponse(
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
        Instant createdAt,
        Instant updatedAt
) {

    public static AssetResponse from(MetadataAsset asset) {
        return new AssetResponse(
                asset.id(),
                asset.tenantId(),
                asset.assetType(),
                asset.code(),
                asset.name(),
                asset.displayName(),
                asset.description(),
                asset.businessDomain(),
                asset.systemCode(),
                asset.ownerUserId(),
                asset.stewardUserId(),
                asset.status(),
                asset.version(),
                asset.sourceType(),
                asset.attributesJson(),
                asset.createdAt(),
                asset.updatedAt()
        );
    }
}
