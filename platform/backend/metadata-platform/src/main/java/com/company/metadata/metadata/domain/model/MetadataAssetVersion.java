package com.company.metadata.metadata.domain.model;

import java.time.Instant;

/**
 * 资产发布版本模型，保存发布时的资产快照。
 *
 * 作者：Punjab
 */
public record MetadataAssetVersion(
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
}
