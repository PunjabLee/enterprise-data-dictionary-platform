package com.company.metadata.system.application.service;

import com.company.metadata.common.audit.AuditEntry;
import com.company.metadata.common.audit.AuditLogger;
import com.company.metadata.common.security.CurrentUserProvider;
import com.company.metadata.common.security.PermissionChecker;
import com.company.metadata.system.application.command.CreateOrganizationCommand;
import com.company.metadata.system.domain.model.Organization;
import com.company.metadata.system.domain.repository.OrganizationRepository;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 系统管理应用服务，编排权限校验、领域仓储和审计记录。
 *
 * 作者：Punjab
 */
@Service
public class OrganizationApplicationService {

    private final CurrentUserProvider currentUserProvider;
    private final PermissionChecker permissionChecker;
    private final AuditLogger auditLogger;
    private final OrganizationRepository organizationRepository;

    public OrganizationApplicationService(
            CurrentUserProvider currentUserProvider,
            PermissionChecker permissionChecker,
            AuditLogger auditLogger,
            OrganizationRepository organizationRepository
    ) {
        this.currentUserProvider = currentUserProvider;
        this.permissionChecker = permissionChecker;
        this.auditLogger = auditLogger;
        this.organizationRepository = organizationRepository;
    }

    public List<Organization> findAll() {
        permissionChecker.requirePermission(SystemPermissionCodes.ORGANIZATION_READ);
        return organizationRepository.findAll(currentUserProvider.requireCurrentUser().tenantId());
    }

    public Organization findById(Long id) {
        permissionChecker.requirePermission(SystemPermissionCodes.ORGANIZATION_READ);
        return organizationRepository.findById(currentUserProvider.requireCurrentUser().tenantId(), id)
                .orElseThrow(() -> new NoSuchElementException("Organization not found: " + id));
    }

    @Transactional
    public Organization create(CreateOrganizationCommand command) {
        permissionChecker.requirePermission(SystemPermissionCodes.ORGANIZATION_CREATE);
        String tenantId = currentUserProvider.requireCurrentUser().tenantId();
        Organization organization = organizationRepository.save(new Organization(
                null,
                tenantId,
                command.parentId(),
                command.code(),
                command.name(),
                "active",
                null,
                null
        ));
        auditLogger.record(AuditEntry.success(
                "system.organization.create",
                "sys_organization",
                String.valueOf(organization.id()),
                AuditJson.stringField("code", organization.code())
        ));
        return organization;
    }
}
