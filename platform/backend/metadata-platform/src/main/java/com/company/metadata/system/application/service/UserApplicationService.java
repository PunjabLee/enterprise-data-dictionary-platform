package com.company.metadata.system.application.service;

import com.company.metadata.common.audit.AuditEntry;
import com.company.metadata.common.audit.AuditLogger;
import com.company.metadata.common.security.CurrentUserProvider;
import com.company.metadata.common.security.PermissionChecker;
import com.company.metadata.system.application.command.AssignUserRolesCommand;
import com.company.metadata.system.application.command.CreateUserCommand;
import com.company.metadata.system.domain.model.User;
import com.company.metadata.system.domain.repository.UserRepository;
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
public class UserApplicationService {

    private final CurrentUserProvider currentUserProvider;
    private final PermissionChecker permissionChecker;
    private final AuditLogger auditLogger;
    private final UserRepository userRepository;

    public UserApplicationService(
            CurrentUserProvider currentUserProvider,
            PermissionChecker permissionChecker,
            AuditLogger auditLogger,
            UserRepository userRepository
    ) {
        this.currentUserProvider = currentUserProvider;
        this.permissionChecker = permissionChecker;
        this.auditLogger = auditLogger;
        this.userRepository = userRepository;
    }

    public List<User> search(String keyword, String status, int limit, int offset) {
        permissionChecker.requirePermission(SystemPermissionCodes.USER_READ);
        return userRepository.search(
                currentUserProvider.requireCurrentUser().tenantId(),
                keyword,
                status,
                boundedLimit(limit),
                Math.max(offset, 0)
        );
    }

    public User findById(Long id) {
        permissionChecker.requirePermission(SystemPermissionCodes.USER_READ);
        return userRepository.findById(currentUserProvider.requireCurrentUser().tenantId(), id)
                .orElseThrow(() -> new NoSuchElementException("User not found: " + id));
    }

    @Transactional
    public User create(CreateUserCommand command) {
        permissionChecker.requirePermission(SystemPermissionCodes.USER_CREATE);
        String tenantId = currentUserProvider.requireCurrentUser().tenantId();
        User user = userRepository.save(new User(
                null,
                tenantId,
                command.organizationId(),
                command.username(),
                command.displayName(),
                command.email(),
                command.mobile(),
                "active",
                "local",
                null,
                null,
                null
        ));
        auditLogger.record(AuditEntry.success(
                "system.user.create",
                "sys_user",
                String.valueOf(user.id()),
                AuditJson.stringField("username", user.username())
        ));
        return user;
    }

    @Transactional
    public User disable(Long id) {
        permissionChecker.requirePermission(SystemPermissionCodes.USER_UPDATE);
        String tenantId = currentUserProvider.requireCurrentUser().tenantId();
        User user = userRepository.findById(tenantId, id)
                .orElseThrow(() -> new NoSuchElementException("User not found: " + id));
        User disabled = user.disable();
        userRepository.update(disabled);
        auditLogger.record(AuditEntry.success(
                "system.user.disable",
                "sys_user",
                String.valueOf(id),
                AuditJson.stringField("status", "disabled")
        ));
        return disabled;
    }

    @Transactional
    public void assignRoles(AssignUserRolesCommand command) {
        permissionChecker.requirePermission(SystemPermissionCodes.USER_UPDATE);
        String tenantId = currentUserProvider.requireCurrentUser().tenantId();
        userRepository.replaceUserRoles(tenantId, command.userId(), command.roleCodes());
        auditLogger.record(AuditEntry.success(
                "system.user.roles.replace",
                "sys_user",
                String.valueOf(command.userId()),
                AuditJson.stringArrayField("roleCodes", command.roleCodes())
        ));
    }

    private static int boundedLimit(int limit) {
        if (limit <= 0) {
            return 50;
        }
        return Math.min(limit, 200);
    }
}
