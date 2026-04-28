package com.company.metadata.metadata.application.service;

import com.company.metadata.common.audit.AuditEntry;
import com.company.metadata.common.audit.AuditLogger;
import com.company.metadata.common.security.CurrentUser;
import com.company.metadata.common.security.CurrentUserProvider;
import com.company.metadata.common.security.PermissionChecker;
import com.company.metadata.metadata.application.command.CreateAssetCommand;
import com.company.metadata.metadata.application.command.UpsertAssetFieldCommand;
import com.company.metadata.metadata.application.query.AssetSearchQuery;
import com.company.metadata.metadata.domain.model.MetadataAsset;
import com.company.metadata.metadata.domain.model.MetadataAssetVersion;
import com.company.metadata.metadata.domain.model.MetadataField;
import com.company.metadata.metadata.domain.repository.MetadataAssetRepository;
import com.company.metadata.metadata.domain.repository.MetadataFieldRepository;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 资产目录应用服务，负责编排权限、资产状态和版本快照。
 *
 * 作者：Punjab
 */
@Service
public class AssetApplicationService {

    private final CurrentUserProvider currentUserProvider;
    private final PermissionChecker permissionChecker;
    private final AuditLogger auditLogger;
    private final MetadataAssetRepository assetRepository;
    private final MetadataFieldRepository fieldRepository;

    public AssetApplicationService(
            CurrentUserProvider currentUserProvider,
            PermissionChecker permissionChecker,
            AuditLogger auditLogger,
            MetadataAssetRepository assetRepository,
            MetadataFieldRepository fieldRepository
    ) {
        this.currentUserProvider = currentUserProvider;
        this.permissionChecker = permissionChecker;
        this.auditLogger = auditLogger;
        this.assetRepository = assetRepository;
        this.fieldRepository = fieldRepository;
    }

    public List<MetadataAsset> search(AssetSearchQuery query) {
        permissionChecker.requirePermission(MetadataPermissionCodes.ASSET_READ);
        CurrentUser currentUser = currentUserProvider.requireCurrentUser();
        AssetSearchQuery boundedQuery = new AssetSearchQuery(
                query.keyword(),
                query.assetType(),
                query.status(),
                query.businessDomain(),
                query.systemCode(),
                boundedLimit(query.limit()),
                Math.max(query.offset(), 0)
        );
        return assetRepository.search(currentUser.tenantId(), boundedQuery);
    }

    public MetadataAsset findById(Long id) {
        permissionChecker.requirePermission(MetadataPermissionCodes.ASSET_READ);
        String tenantId = currentUserProvider.requireCurrentUser().tenantId();
        return assetRepository.findById(tenantId, id)
                .orElseThrow(() -> new NoSuchElementException("Asset not found: " + id));
    }

    @Transactional
    public MetadataAsset create(CreateAssetCommand command) {
        permissionChecker.requirePermission(MetadataPermissionCodes.ASSET_CREATE);
        CurrentUser currentUser = currentUserProvider.requireCurrentUser();
        MetadataAsset asset = assetRepository.save(new MetadataAsset(
                null,
                currentUser.tenantId(),
                command.assetType(),
                command.code(),
                command.name(),
                command.displayName(),
                command.description(),
                command.businessDomain(),
                command.systemCode(),
                command.ownerUserId(),
                command.stewardUserId(),
                "draft",
                0,
                "manual",
                command.attributesJson(),
                currentUser.username(),
                currentUser.username(),
                null,
                null
        ));
        auditLogger.record(AuditEntry.success(
                "metadata.asset.create",
                "md_asset",
                String.valueOf(asset.id()),
                MetadataJson.idField("assetId", asset.id())
        ));
        return asset;
    }

    public List<MetadataField> listFields(Long assetId) {
        permissionChecker.requirePermission(MetadataPermissionCodes.FIELD_READ);
        String tenantId = currentUserProvider.requireCurrentUser().tenantId();
        ensureAssetExists(tenantId, assetId);
        return fieldRepository.findByAssetId(tenantId, assetId);
    }

    @Transactional
    public MetadataField saveField(UpsertAssetFieldCommand command) {
        permissionChecker.requirePermission(MetadataPermissionCodes.FIELD_UPDATE);
        CurrentUser currentUser = currentUserProvider.requireCurrentUser();
        ensureAssetExists(currentUser.tenantId(), command.assetId());
        MetadataField field = fieldRepository.saveOrUpdate(new MetadataField(
                null,
                currentUser.tenantId(),
                command.assetId(),
                null,
                command.fieldName(),
                command.displayName(),
                command.dataType(),
                command.lengthValue(),
                command.precisionValue(),
                valueOrDefault(command.nullable(), true),
                valueOrDefault(command.primaryKey(), false),
                valueOrDefault(command.keyField(), false),
                valueOrDefault(command.sensitive(), false),
                command.classificationLevel(),
                command.businessDefinition(),
                command.businessRule(),
                command.standardCode(),
                command.termCode(),
                command.ownerUserId(),
                command.stewardUserId(),
                "active",
                currentUser.username(),
                currentUser.username(),
                null,
                null
        ));
        auditLogger.record(AuditEntry.success(
                "metadata.field.upsert",
                "md_asset_field",
                String.valueOf(field.id()),
                MetadataJson.idField("fieldId", field.id())
        ));
        return field;
    }

    @Transactional
    public MetadataAssetVersion publish(Long assetId, String comment) {
        permissionChecker.requirePermission(MetadataPermissionCodes.ASSET_PUBLISH);
        CurrentUser currentUser = currentUserProvider.requireCurrentUser();
        MetadataAsset asset = assetRepository.findById(currentUser.tenantId(), assetId)
                .orElseThrow(() -> new NoSuchElementException("Asset not found: " + assetId));
        MetadataAsset published = asset.publish(currentUser.username());
        assetRepository.update(published);
        List<MetadataField> fields = fieldRepository.findByAssetId(currentUser.tenantId(), assetId);
        MetadataAssetVersion version = assetRepository.saveVersion(new MetadataAssetVersion(
                null,
                currentUser.tenantId(),
                assetId,
                published.version(),
                "published",
                MetadataJson.assetSnapshot(published, fields),
                currentUser.username(),
                null,
                comment
        ));
        auditLogger.record(AuditEntry.success(
                "metadata.asset.publish",
                "md_asset",
                String.valueOf(assetId),
                MetadataJson.idField("versionId", version.id())
        ));
        return version;
    }

    public List<MetadataAssetVersion> listVersions(Long assetId) {
        permissionChecker.requirePermission(MetadataPermissionCodes.ASSET_READ);
        String tenantId = currentUserProvider.requireCurrentUser().tenantId();
        ensureAssetExists(tenantId, assetId);
        return assetRepository.findVersions(tenantId, assetId);
    }

    private void ensureAssetExists(String tenantId, Long assetId) {
        assetRepository.findById(tenantId, assetId)
                .orElseThrow(() -> new NoSuchElementException("Asset not found: " + assetId));
    }

    private static int boundedLimit(int limit) {
        if (limit <= 0) {
            return 50;
        }
        return Math.min(limit, 200);
    }

    private static Boolean valueOrDefault(Boolean value, Boolean defaultValue) {
        return value == null ? defaultValue : value;
    }
}
