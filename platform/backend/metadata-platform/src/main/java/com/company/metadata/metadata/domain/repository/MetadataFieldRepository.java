package com.company.metadata.metadata.domain.repository;

import com.company.metadata.metadata.application.query.FieldDictionaryQuery;
import com.company.metadata.metadata.domain.model.MetadataField;
import java.util.List;
import java.util.Optional;

/**
 * 元数据字段仓储接口，支撑字段清单和字段字典维护。
 *
 * 作者：Punjab
 */
public interface MetadataFieldRepository {

    MetadataField saveOrUpdate(MetadataField field);

    void update(MetadataField field);

    Optional<MetadataField> findById(String tenantId, Long id);

    List<MetadataField> findByAssetId(String tenantId, Long assetId);

    List<MetadataField> searchDictionary(String tenantId, FieldDictionaryQuery query);
}
