package com.company.metadata.metadata.infrastructure.persistence.record;

import java.time.Instant;

/**
 * 元数据字段持久化记录，映射 md_asset_field 表字段。
 *
 * 作者：Punjab
 */
public class MetadataFieldRecord {
    public Long id;
    public String tenantId;
    public Long assetId;
    public Integer ordinal;
    public String fieldName;
    public String displayName;
    public String dataType;
    public Integer lengthValue;
    public Integer precisionValue;
    public Boolean nullable;
    public Boolean primaryKey;
    public Boolean keyField;
    public Boolean sensitive;
    public String classificationLevel;
    public String businessDefinition;
    public String businessRule;
    public String standardCode;
    public String termCode;
    public Long ownerUserId;
    public Long stewardUserId;
    public String status;
    public String createdBy;
    public String updatedBy;
    public Instant createdAt;
    public Instant updatedAt;
}
