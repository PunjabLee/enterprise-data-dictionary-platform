package com.company.metadata.metadata.infrastructure.persistence.repository;

import com.company.metadata.metadata.application.query.AssetSearchQuery;
import com.company.metadata.metadata.domain.model.MetadataAsset;
import com.company.metadata.metadata.domain.model.MetadataAssetVersion;
import com.company.metadata.metadata.domain.repository.MetadataAssetRepository;
import com.company.metadata.metadata.infrastructure.config.MetadataPersistenceEnabled;
import com.company.metadata.metadata.infrastructure.persistence.dao.MetadataAssetDao;
import com.company.metadata.metadata.infrastructure.persistence.record.MetadataAssetRecord;
import com.company.metadata.metadata.infrastructure.persistence.record.MetadataAssetVersionRecord;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * 元数据资产仓储 MyBatis 实现，完成领域模型和记录对象转换。
 *
 * 作者：Punjab
 */
@Repository
@MetadataPersistenceEnabled
public class MybatisMetadataAssetRepository implements MetadataAssetRepository {

    private final MetadataAssetDao assetDao;

    public MybatisMetadataAssetRepository(MetadataAssetDao assetDao) {
        this.assetDao = assetDao;
    }

    @Override
    public MetadataAsset save(MetadataAsset asset) {
        return toDomain(assetDao.insert(toRecord(asset)));
    }

    @Override
    public void update(MetadataAsset asset) {
        assetDao.update(toRecord(asset));
    }

    @Override
    public Optional<MetadataAsset> findById(String tenantId, Long id) {
        return Optional.ofNullable(assetDao.selectById(tenantId, id)).map(MybatisMetadataAssetRepository::toDomain);
    }

    @Override
    public List<MetadataAsset> search(String tenantId, AssetSearchQuery query) {
        return assetDao.search(
                        tenantId,
                        query.keyword(),
                        query.assetType(),
                        query.status(),
                        query.businessDomain(),
                        query.systemCode(),
                        query.limit(),
                        query.offset()
                )
                .stream()
                .map(MybatisMetadataAssetRepository::toDomain)
                .toList();
    }

    @Override
    public MetadataAssetVersion saveVersion(MetadataAssetVersion version) {
        return toDomain(assetDao.insertVersion(toRecord(version)));
    }

    @Override
    public List<MetadataAssetVersion> findVersions(String tenantId, Long assetId) {
        return assetDao.selectVersions(tenantId, assetId)
                .stream()
                .map(MybatisMetadataAssetRepository::toDomain)
                .toList();
    }

    private static MetadataAssetRecord toRecord(MetadataAsset asset) {
        MetadataAssetRecord record = new MetadataAssetRecord();
        record.id = asset.id();
        record.tenantId = asset.tenantId();
        record.assetType = asset.assetType();
        record.code = asset.code();
        record.name = asset.name();
        record.displayName = asset.displayName();
        record.description = asset.description();
        record.businessDomain = asset.businessDomain();
        record.systemCode = asset.systemCode();
        record.ownerUserId = asset.ownerUserId();
        record.stewardUserId = asset.stewardUserId();
        record.status = asset.status();
        record.version = asset.version();
        record.sourceType = asset.sourceType();
        record.attributesJson = asset.attributesJson();
        record.createdBy = asset.createdBy();
        record.updatedBy = asset.updatedBy();
        record.createdAt = asset.createdAt();
        record.updatedAt = asset.updatedAt();
        return record;
    }

    private static MetadataAsset toDomain(MetadataAssetRecord record) {
        return new MetadataAsset(
                record.id,
                record.tenantId,
                record.assetType,
                record.code,
                record.name,
                record.displayName,
                record.description,
                record.businessDomain,
                record.systemCode,
                record.ownerUserId,
                record.stewardUserId,
                record.status,
                record.version,
                record.sourceType,
                record.attributesJson,
                record.createdBy,
                record.updatedBy,
                record.createdAt,
                record.updatedAt
        );
    }

    private static MetadataAssetVersionRecord toRecord(MetadataAssetVersion version) {
        MetadataAssetVersionRecord record = new MetadataAssetVersionRecord();
        record.id = version.id();
        record.tenantId = version.tenantId();
        record.assetId = version.assetId();
        record.versionNumber = version.versionNumber();
        record.status = version.status();
        record.snapshotJson = version.snapshotJson();
        record.publishedBy = version.publishedBy();
        record.publishedAt = version.publishedAt();
        record.comment = version.comment();
        return record;
    }

    private static MetadataAssetVersion toDomain(MetadataAssetVersionRecord record) {
        return new MetadataAssetVersion(
                record.id,
                record.tenantId,
                record.assetId,
                record.versionNumber,
                record.status,
                record.snapshotJson,
                record.publishedBy,
                record.publishedAt,
                record.comment
        );
    }
}
