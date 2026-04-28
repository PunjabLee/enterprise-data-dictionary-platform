package com.company.metadata.metadata.infrastructure.persistence.dao;

import com.company.metadata.metadata.infrastructure.config.MetadataPersistenceEnabled;
import com.company.metadata.metadata.infrastructure.persistence.mapper.MetadataFieldMapper;
import com.company.metadata.metadata.infrastructure.persistence.record.MetadataFieldRecord;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * 元数据字段 DAO，封装字段字典 Mapper 调用。
 *
 * 作者：Punjab
 */
@Repository
@MetadataPersistenceEnabled
public class MetadataFieldDao {

    private final MetadataFieldMapper fieldMapper;

    public MetadataFieldDao(MetadataFieldMapper fieldMapper) {
        this.fieldMapper = fieldMapper;
    }

    public MetadataFieldRecord upsert(MetadataFieldRecord record) {
        fieldMapper.upsert(record);
        return record;
    }

    public void update(MetadataFieldRecord record) {
        fieldMapper.update(record);
    }

    public MetadataFieldRecord selectById(String tenantId, Long id) {
        return fieldMapper.selectById(tenantId, id);
    }

    public List<MetadataFieldRecord> selectByAssetId(String tenantId, Long assetId) {
        return fieldMapper.selectByAssetId(tenantId, assetId);
    }

    public List<MetadataFieldRecord> searchDictionary(
            String tenantId,
            String keyword,
            Long assetId,
            Boolean sensitive,
            String status,
            int limit,
            int offset
    ) {
        return fieldMapper.searchDictionary(tenantId, keyword, assetId, sensitive, status, limit, offset);
    }
}
