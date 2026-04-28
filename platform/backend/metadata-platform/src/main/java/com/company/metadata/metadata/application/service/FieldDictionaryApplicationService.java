package com.company.metadata.metadata.application.service;

import com.company.metadata.common.audit.AuditEntry;
import com.company.metadata.common.audit.AuditLogger;
import com.company.metadata.common.security.CurrentUser;
import com.company.metadata.common.security.CurrentUserProvider;
import com.company.metadata.common.security.PermissionChecker;
import com.company.metadata.metadata.application.command.UpdateFieldDefinitionCommand;
import com.company.metadata.metadata.application.query.FieldDictionaryQuery;
import com.company.metadata.metadata.domain.model.MetadataField;
import com.company.metadata.metadata.domain.repository.MetadataFieldRepository;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 字段字典应用服务，处理字段业务定义和标准预留引用。
 *
 * 作者：Punjab
 */
@Service
public class FieldDictionaryApplicationService {

    private final CurrentUserProvider currentUserProvider;
    private final PermissionChecker permissionChecker;
    private final AuditLogger auditLogger;
    private final MetadataFieldRepository fieldRepository;

    public FieldDictionaryApplicationService(
            CurrentUserProvider currentUserProvider,
            PermissionChecker permissionChecker,
            AuditLogger auditLogger,
            MetadataFieldRepository fieldRepository
    ) {
        this.currentUserProvider = currentUserProvider;
        this.permissionChecker = permissionChecker;
        this.auditLogger = auditLogger;
        this.fieldRepository = fieldRepository;
    }

    public List<MetadataField> search(FieldDictionaryQuery query) {
        permissionChecker.requirePermission(MetadataPermissionCodes.FIELD_READ);
        CurrentUser currentUser = currentUserProvider.requireCurrentUser();
        FieldDictionaryQuery boundedQuery = new FieldDictionaryQuery(
                query.keyword(),
                query.assetId(),
                query.sensitive(),
                query.status(),
                boundedLimit(query.limit()),
                Math.max(query.offset(), 0)
        );
        return fieldRepository.searchDictionary(currentUser.tenantId(), boundedQuery);
    }

    @Transactional
    public MetadataField updateDefinition(UpdateFieldDefinitionCommand command) {
        permissionChecker.requirePermission(MetadataPermissionCodes.FIELD_UPDATE);
        CurrentUser currentUser = currentUserProvider.requireCurrentUser();
        MetadataField field = fieldRepository.findById(currentUser.tenantId(), command.fieldId())
                .orElseThrow(() -> new NoSuchElementException("Field not found: " + command.fieldId()));
        MetadataField updated = field.updateDefinition(
                command.displayName(),
                command.businessDefinition(),
                command.businessRule(),
                command.keyField(),
                command.sensitive(),
                command.classificationLevel(),
                command.standardCode(),
                command.termCode(),
                command.ownerUserId(),
                command.stewardUserId(),
                currentUser.username()
        );
        fieldRepository.update(updated);
        auditLogger.record(AuditEntry.success(
                "metadata.field.definition.update",
                "md_asset_field",
                String.valueOf(command.fieldId()),
                MetadataJson.idField("fieldId", command.fieldId())
        ));
        return updated;
    }

    private static int boundedLimit(int limit) {
        if (limit <= 0) {
            return 50;
        }
        return Math.min(limit, 200);
    }
}
