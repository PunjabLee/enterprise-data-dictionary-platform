package com.company.metadata.metadata.infrastructure.persistence.dao;

import com.company.metadata.metadata.infrastructure.config.MetadataPersistenceEnabled;
import com.company.metadata.metadata.infrastructure.persistence.mapper.MetadataAssetMapper;
import com.company.metadata.metadata.infrastructure.persistence.record.MetadataAssetRecord;
import com.company.metadata.metadata.infrastructure.persistence.record.MetadataAssetVersionRecord;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * 元数据资产 DAO，封装资产 Mapper 调用。
 *
 * 作者：Punjab
 */
@Repository
@MetadataPersistenceEnabled
public class MetadataAssetDao {

    private final MetadataAssetMapper assetMapper;

    public MetadataAssetDao(MetadataAssetMapper assetMapper) {
        this.assetMapper = assetMapper;
    }

    public MetadataAssetRecord insert(MetadataAssetRecord record) {
        assetMapper.insert(record);
        return record;
    }

    public void update(MetadataAssetRecord record) {
        assetMapper.update(record);
    }

    public MetadataAssetRecord selectById(String tenantId, Long id) {
        return assetMapper.selectById(tenantId, id);
    }

    public List<MetadataAssetRecord> search(
            String tenantId,
            String keyword,
            String assetType,
            String status,
            String businessDomain,
            String systemCode,
            int limit,
            int offset
    ) {
        return assetMapper.search(tenantId, keyword, assetType, status, businessDomain, systemCode, limit, offset);
    }

    public MetadataAssetVersionRecord insertVersion(MetadataAssetVersionRecord record) {
        assetMapper.insertVersion(record);
        return record;
    }

    public List<MetadataAssetVersionRecord> selectVersions(String tenantId, Long assetId) {
        return assetMapper.selectVersions(tenantId, assetId);
    }
}
