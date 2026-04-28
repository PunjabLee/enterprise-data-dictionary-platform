package com.company.metadata.system.application.service;

import com.company.metadata.common.security.CurrentUserProvider;
import com.company.metadata.common.security.PermissionChecker;
import com.company.metadata.system.domain.model.Permission;
import com.company.metadata.system.domain.repository.PermissionRepository;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 系统管理应用服务，编排权限校验、领域仓储和审计记录。
 *
 * 作者：Punjab
 */
@Service
public class PermissionApplicationService {

    private final CurrentUserProvider currentUserProvider;
    private final PermissionChecker permissionChecker;
    private final PermissionRepository permissionRepository;

    public PermissionApplicationService(
            CurrentUserProvider currentUserProvider,
            PermissionChecker permissionChecker,
            PermissionRepository permissionRepository
    ) {
        this.currentUserProvider = currentUserProvider;
        this.permissionChecker = permissionChecker;
        this.permissionRepository = permissionRepository;
    }

    public List<Permission> findAll(boolean includeDisabled) {
        permissionChecker.requirePermission(SystemPermissionCodes.PERMISSION_READ);
        return permissionRepository.findAll(currentUserProvider.requireCurrentUser().tenantId(), includeDisabled);
    }
}
