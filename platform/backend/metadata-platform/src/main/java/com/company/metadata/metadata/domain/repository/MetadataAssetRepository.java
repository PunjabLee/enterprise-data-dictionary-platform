package com.company.metadata.metadata.domain.repository;

import com.company.metadata.metadata.application.query.AssetSearchQuery;
import com.company.metadata.metadata.domain.model.MetadataAsset;
import com.company.metadata.metadata.domain.model.MetadataAssetVersion;
import java.util.List;
import java.util.Optional;

/**
 * 元数据资产仓储接口，隔离应用层与 MyBatis 持久化细节。
 *
 * 作者：Punjab
 */
public interface MetadataAssetRepository {

    MetadataAsset save(MetadataAsset asset);

    void update(MetadataAsset asset);

    Optional<MetadataAsset> findById(String tenantId, Long id);

    List<MetadataAsset> search(String tenantId, AssetSearchQuery query);

    MetadataAssetVersion saveVersion(MetadataAssetVersion version);

    List<MetadataAssetVersion> findVersions(String tenantId, Long assetId);
}
