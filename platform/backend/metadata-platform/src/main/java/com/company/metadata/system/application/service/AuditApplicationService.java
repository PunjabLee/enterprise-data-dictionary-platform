package com.company.metadata.system.application.service;

import com.company.metadata.common.security.CurrentUserProvider;
import com.company.metadata.common.security.PermissionChecker;
import com.company.metadata.system.application.query.AuditLogQuery;
import com.company.metadata.system.domain.model.AuditLog;
import com.company.metadata.system.domain.repository.AuditLogRepository;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * 系统管理应用服务，编排权限校验、领域仓储和审计记录。
 *
 * 作者：Punjab
 */
@Service
public class AuditApplicationService {

    private final CurrentUserProvider currentUserProvider;
    private final PermissionChecker permissionChecker;
    private final AuditLogRepository auditLogRepository;

    public AuditApplicationService(
            CurrentUserProvider currentUserProvider,
            PermissionChecker permissionChecker,
            AuditLogRepository auditLogRepository
    ) {
        this.currentUserProvider = currentUserProvider;
        this.permissionChecker = permissionChecker;
        this.auditLogRepository = auditLogRepository;
    }

    public List<AuditLog> search(AuditLogQuery query) {
        permissionChecker.requirePermission(SystemPermissionCodes.AUDIT_READ);
        String tenantId = currentUserProvider.requireCurrentUser().tenantId();
        int limit = boundedLimit(query.limit());
        int offset = Math.max(query.offset(), 0);
        return auditLogRepository.search(
                tenantId,
                query.actorId(),
                query.action(),
                query.objectType(),
                query.from(),
                query.to(),
                limit,
                offset
        );
    }

    private static int boundedLimit(int limit) {
        if (limit <= 0) {
            return 50;
        }
        return Math.min(limit, 200);
    }
}
