package com.company.metadata.metadata.interfaces.rest.dto;

import com.company.metadata.metadata.domain.model.MetadataAssetVersion;
import java.time.Instant;

/**
 * 资产版本响应对象，提供发布历史追溯能力。
 *
 * 作者：Punjab
 */
public record AssetVersionResponse(
        Long id,
        String tenantId,
        Long assetId,
        Integer versionNumber,
        String status,
        String snapshotJson,
        String publishedBy,
        Instant publishedAt,
        String comment
) {

    public static AssetVersionResponse from(MetadataAssetVersion version) {
        return new AssetVersionResponse(
                version.id(),
                version.tenantId(),
                version.assetId(),
                version.versionNumber(),
                version.status(),
                version.snapshotJson(),
                version.publishedBy(),
                version.publishedAt(),
                version.comment()
        );
    }
}
