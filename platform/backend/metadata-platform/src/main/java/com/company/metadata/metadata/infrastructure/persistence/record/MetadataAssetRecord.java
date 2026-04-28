package com.company.metadata.metadata.infrastructure.persistence.record;

import java.time.Instant;

/**
 * 元数据资产持久化记录，映射 md_asset 表字段。
 *
 * 作者：Punjab
 */
public class MetadataAssetRecord {
    public Long id;
    public String tenantId;
    public String assetType;
    public String code;
    public String name;
    public String displayName;
    public String description;
    public String businessDomain;
    public String systemCode;
    public Long ownerUserId;
    public Long stewardUserId;
    public String status;
    public Integer version;
    public String sourceType;
    public String attributesJson;
    public String createdBy;
    public String updatedBy;
    public Instant createdAt;
    public Instant updatedAt;
}
