package com.company.metadata.system.application.service;

import com.company.metadata.common.audit.AuditEntry;
import com.company.metadata.common.audit.AuditLogger;
import com.company.metadata.common.security.CurrentUser;
import com.company.metadata.common.security.CurrentUserProvider;
import com.company.metadata.common.security.SecurityConstants;
import com.company.metadata.system.domain.model.AuditLog;
import com.company.metadata.system.domain.repository.AuditLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 系统管理应用服务，编排权限校验、领域仓储和审计记录。
 *
 * 作者：Punjab
 */
@Service
public class RepositoryAuditLogger implements AuditLogger {

    private final CurrentUserProvider currentUserProvider;
    private final AuditLogRepository auditLogRepository;

    public RepositoryAuditLogger(CurrentUserProvider currentUserProvider, AuditLogRepository auditLogRepository) {
        this.currentUserProvider = currentUserProvider;
        this.auditLogRepository = auditLogRepository;
    }

    @Override
    public void record(AuditEntry entry) {
        CurrentUser currentUser = currentUserProvider.currentUser().orElse(null);
        HttpServletRequest request = currentRequest();
        auditLogRepository.save(new AuditLog(
                null,
                currentUser == null ? SecurityConstants.DEFAULT_TENANT_ID : currentUser.tenantId(),
                currentUser == null ? null : currentUser.userId(),
                currentUser == null ? null : currentUser.username(),
                entry.action(),
                entry.objectType(),
                entry.objectId(),
                entry.beforeValue(),
                entry.afterValue(),
                entry.result(),
                request == null ? null : request.getRemoteAddr(),
                request == null ? null : request.getHeader("User-Agent"),
                request == null ? null : request.getHeader("X-Trace-Id"),
                Instant.now()
        ));
    }

    private static HttpServletRequest currentRequest() {
        if (RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attributes) {
            return attributes.getRequest();
        }
        return null;
    }
}
