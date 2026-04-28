package com.company.metadata.system.application.service;

import com.company.metadata.common.audit.AuditEntry;
import com.company.metadata.common.audit.AuditLogger;
import com.company.metadata.common.security.CurrentUserProvider;
import com.company.metadata.common.security.PermissionChecker;
import com.company.metadata.system.application.command.AssignRolePermissionsCommand;
import com.company.metadata.system.application.command.CreateRoleCommand;
import com.company.metadata.system.application.command.GrantDataScopeCommand;
import com.company.metadata.system.domain.model.DataScope;
import com.company.metadata.system.domain.model.Role;
import com.company.metadata.system.domain.repository.DataScopeRepository;
import com.company.metadata.system.domain.repository.PermissionRepository;
import com.company.metadata.system.domain.repository.RoleRepository;
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
public class RoleApplicationService {

    private final CurrentUserProvider currentUserProvider;
    private final PermissionChecker permissionChecker;
    private final AuditLogger auditLogger;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final DataScopeRepository dataScopeRepository;

    public RoleApplicationService(
            CurrentUserProvider currentUserProvider,
            PermissionChecker permissionChecker,
            AuditLogger auditLogger,
            RoleRepository roleRepository,
            PermissionRepository permissionRepository,
            DataScopeRepository dataScopeRepository
    ) {
        this.currentUserProvider = currentUserProvider;
        this.permissionChecker = permissionChecker;
        this.auditLogger = auditLogger;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.dataScopeRepository = dataScopeRepository;
    }

    public List<Role> findAll(boolean includeDisabled) {
        permissionChecker.requirePermission(SystemPermissionCodes.ROLE_READ);
        return roleRepository.findAll(currentUserProvider.requireCurrentUser().tenantId(), includeDisabled);
    }

    public Role findById(Long id) {
        permissionChecker.requirePermission(SystemPermissionCodes.ROLE_READ);
        return roleRepository.findById(currentUserProvider.requireCurrentUser().tenantId(), id)
                .orElseThrow(() -> new NoSuchElementException("Role not found: " + id));
    }

    public List<DataScope> findDataScopes(Long roleId) {
        permissionChecker.requirePermission(SystemPermissionCodes.DATA_SCOPE_READ);
        String tenantId = currentUserProvider.requireCurrentUser().tenantId();
        return dataScopeRepository.findBySubject(tenantId, DataScope.SubjectType.ROLE, roleId);
    }

    @Transactional
    public Role create(CreateRoleCommand command) {
        permissionChecker.requirePermission(SystemPermissionCodes.ROLE_CREATE);
        String tenantId = currentUserProvider.requireCurrentUser().tenantId();
        Role role = roleRepository.save(new Role(
                null,
                tenantId,
                command.code(),
                command.name(),
                command.description(),
                false,
                true,
                null,
                null
        ));
        auditLogger.record(AuditEntry.success(
                "system.role.create",
                "sys_role",
                String.valueOf(role.id()),
                AuditJson.stringField("code", role.code())
        ));
        return role;
    }

    @Transactional
    public void assignPermissions(AssignRolePermissionsCommand command) {
        permissionChecker.requirePermission(SystemPermissionCodes.PERMISSION_UPDATE);
        String tenantId = currentUserProvider.requireCurrentUser().tenantId();
        permissionRepository.replaceRolePermissions(tenantId, command.roleId(), command.permissionCodes());
        auditLogger.record(AuditEntry.success(
                "system.role.permissions.replace",
                "sys_role",
                String.valueOf(command.roleId()),
                AuditJson.stringArrayField("permissionCodes", command.permissionCodes())
        ));
    }

    @Transactional
    public DataScope grantDataScope(GrantDataScopeCommand command) {
        permissionChecker.requirePermission(SystemPermissionCodes.DATA_SCOPE_UPDATE);
        String tenantId = currentUserProvider.requireCurrentUser().tenantId();
        DataScope dataScope = dataScopeRepository.save(new DataScope(
                null,
                tenantId,
                command.subjectType(),
                command.subjectId(),
                command.scopeType(),
                command.scopeValue() == null || command.scopeValue().isBlank() ? "*" : command.scopeValue(),
                null
        ));
        auditLogger.record(AuditEntry.success(
                "system.data_scope.grant",
                "sys_data_scope",
                String.valueOf(dataScope.id()),
                AuditJson.idField("subjectId", dataScope.subjectId())
        ));
        return dataScope;
    }
}
