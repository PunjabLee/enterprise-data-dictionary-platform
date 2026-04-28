package com.company.metadata.system.application.service;

import com.company.metadata.common.security.CurrentUser;
import com.company.metadata.common.security.CurrentUserProvider;
import com.company.metadata.common.security.DataScopeProvider;
import com.company.metadata.system.application.query.CurrentSecurityView;
import com.company.metadata.system.domain.repository.PermissionRepository;
import com.company.metadata.system.domain.repository.RoleRepository;
import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Service;

/**
 * 系统管理应用服务，编排权限校验、领域仓储和审计记录。
 *
 * 作者：Punjab
 */
@Service
public class SecurityApplicationService {

    private final CurrentUserProvider currentUserProvider;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final DataScopeProvider dataScopeProvider;

    public SecurityApplicationService(
            CurrentUserProvider currentUserProvider,
            RoleRepository roleRepository,
            PermissionRepository permissionRepository,
            DataScopeProvider dataScopeProvider
    ) {
        this.currentUserProvider = currentUserProvider;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.dataScopeProvider = dataScopeProvider;
    }

    public CurrentSecurityView current() {
        CurrentUser currentUser = currentUserProvider.requireCurrentUser();
        Set<String> roles = new HashSet<>(currentUser.roleCodes());
        Set<String> permissions = new HashSet<>(currentUser.permissionCodes());
        if (currentUser.userId() != null) {
            roles.addAll(roleRepository.findRoleCodesByUserId(currentUser.tenantId(), currentUser.userId()));
            permissions.addAll(permissionRepository.findPermissionCodesByUserId(
                    currentUser.tenantId(),
                    currentUser.userId()
            ));
        }
        return new CurrentSecurityView(currentUser, roles, permissions, dataScopeProvider.currentDataScope());
    }
}
