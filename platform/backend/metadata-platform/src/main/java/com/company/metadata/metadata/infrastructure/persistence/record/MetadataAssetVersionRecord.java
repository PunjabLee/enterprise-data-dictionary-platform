package com.company.metadata.metadata.infrastructure.persistence.record;

import java.time.Instant;

/**
 * 资产版本持久化记录，映射 md_asset_version 表字段。
 *
 * 作者：Punjab
 */
public class MetadataAssetVersionRecord {
    public Long id;
    public String tenantId;
    public Long assetId;
    public Integer versionNumber;
    public String status;
    public String snapshotJson;
    public String publishedBy;
    public Instant publishedAt;
    public String comment;
}
