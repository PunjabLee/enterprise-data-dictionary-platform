package com.company.metadata.system.application.service;

import com.company.metadata.common.security.CurrentUser;
import com.company.metadata.common.security.CurrentUserProvider;
import com.company.metadata.common.security.PermissionChecker;
import com.company.metadata.common.security.SecurityConstants;
import com.company.metadata.system.domain.repository.PermissionRepository;
import java.util.Set;
import org.springframework.stereotype.Service;

/**
 * 系统管理应用服务，编排权限校验、领域仓储和审计记录。
 *
 * 作者：Punjab
 */
@Service
public class RepositoryPermissionChecker implements PermissionChecker {

    private final CurrentUserProvider currentUserProvider;
    private final PermissionRepository permissionRepository;

    public RepositoryPermissionChecker(
            CurrentUserProvider currentUserProvider,
            PermissionRepository permissionRepository
    ) {
        this.currentUserProvider = currentUserProvider;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public boolean hasPermission(String permissionCode) {
        CurrentUser currentUser = currentUserProvider.currentUser().orElse(null);
        if (currentUser == null) {
            return false;
        }
        if (currentUser.hasPermission(permissionCode)) {
            return true;
        }
        if (currentUser.userId() == null) {
            return false;
        }
        Set<String> persistedPermissions = permissionRepository.findPermissionCodesByUserId(
                currentUser.tenantId(),
                currentUser.userId()
        );
        return persistedPermissions.contains(permissionCode)
                || persistedPermissions.contains(SecurityConstants.ADMIN_PERMISSION);
    }
}
