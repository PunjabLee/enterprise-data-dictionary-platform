package com.company.metadata.metadata.infrastructure.persistence.repository;

import com.company.metadata.metadata.application.query.FieldDictionaryQuery;
import com.company.metadata.metadata.domain.model.MetadataField;
import com.company.metadata.metadata.domain.repository.MetadataFieldRepository;
import com.company.metadata.metadata.infrastructure.config.MetadataPersistenceEnabled;
import com.company.metadata.metadata.infrastructure.persistence.dao.MetadataFieldDao;
import com.company.metadata.metadata.infrastructure.persistence.record.MetadataFieldRecord;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * 元数据字段仓储 MyBatis 实现，完成字段领域模型和记录对象转换。
 *
 * 作者：Punjab
 */
@Repository
@MetadataPersistenceEnabled
public class MybatisMetadataFieldRepository implements MetadataFieldRepository {

    private final MetadataFieldDao fieldDao;

    public MybatisMetadataFieldRepository(MetadataFieldDao fieldDao) {
        this.fieldDao = fieldDao;
    }

    @Override
    public MetadataField saveOrUpdate(MetadataField field) {
        MetadataFieldRecord saved = fieldDao.upsert(toRecord(field));
        if (saved.id == null) {
            return fieldDao.selectByAssetId(field.tenantId(), field.assetId()).stream()
                    .filter(record -> record.fieldName.equals(field.fieldName()))
                    .findFirst()
                    .map(MybatisMetadataFieldRepository::toDomain)
                    .orElse(field);
        }
        return toDomain(saved);
    }

    @Override
    public void update(MetadataField field) {
        fieldDao.update(toRecord(field));
    }

    @Override
    public Optional<MetadataField> findById(String tenantId, Long id) {
        return Optional.ofNullable(fieldDao.selectById(tenantId, id)).map(MybatisMetadataFieldRepository::toDomain);
    }

    @Override
    public List<MetadataField> findByAssetId(String tenantId, Long assetId) {
        return fieldDao.selectByAssetId(tenantId, assetId)
                .stream()
                .map(MybatisMetadataFieldRepository::toDomain)
                .toList();
    }

    @Override
    public List<MetadataField> searchDictionary(String tenantId, FieldDictionaryQuery query) {
        return fieldDao.searchDictionary(
                        tenantId,
                        query.keyword(),
                        query.assetId(),
                        query.sensitive(),
                        query.status(),
                        query.limit(),
                        query.offset()
                )
                .stream()
                .map(MybatisMetadataFieldRepository::toDomain)
                .toList();
    }

    private static MetadataFieldRecord toRecord(MetadataField field) {
        MetadataFieldRecord record = new MetadataFieldRecord();
        record.id = field.id();
        record.tenantId = field.tenantId();
        record.assetId = field.assetId();
        record.ordinal = field.ordinal();
        record.fieldName = field.fieldName();
        record.displayName = field.displayName();
        record.dataType = field.dataType();
        record.lengthValue = field.lengthValue();
        record.precisionValue = field.precisionValue();
        record.nullable = field.nullable();
        record.primaryKey = field.primaryKey();
        record.keyField = field.keyField();
        record.sensitive = field.sensitive();
        record.classificationLevel = field.classificationLevel();
        record.businessDefinition = field.businessDefinition();
        record.businessRule = field.businessRule();
        record.standardCode = field.standardCode();
        record.termCode = field.termCode();
        record.ownerUserId = field.ownerUserId();
        record.stewardUserId = field.stewardUserId();
        record.status = field.status();
        record.createdBy = field.createdBy();
        record.updatedBy = field.updatedBy();
        record.createdAt = field.createdAt();
        record.updatedAt = field.updatedAt();
        return record;
    }

    private static MetadataField toDomain(MetadataFieldRecord record) {
        return new MetadataField(
                record.id,
                record.tenantId,
                record.assetId,
                record.ordinal,
                record.fieldName,
                record.displayName,
                record.dataType,
                record.lengthValue,
                record.precisionValue,
                record.nullable,
                record.primaryKey,
                record.keyField,
                record.sensitive,
                record.classificationLevel,
                record.businessDefinition,
                record.businessRule,
                record.standardCode,
                record.termCode,
                record.ownerUserId,
                record.stewardUserId,
                record.status,
                record.createdBy,
                record.updatedBy,
                record.createdAt,
                record.updatedAt
        );
    }
}
