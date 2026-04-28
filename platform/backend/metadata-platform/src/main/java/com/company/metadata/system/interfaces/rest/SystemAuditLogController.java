package com.company.metadata.system.interfaces.rest;

import com.company.metadata.system.application.query.AuditLogQuery;
import com.company.metadata.system.application.service.AuditApplicationService;
import com.company.metadata.system.interfaces.rest.dto.AuditLogResponse;
import java.time.Instant;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统管理 REST 接口，暴露用户、角色、权限、审计等基础能力。
 *
 * 作者：Punjab
 */
@RestController
@RequestMapping("/system/audit-logs")
public class SystemAuditLogController {

    private final AuditApplicationService auditApplicationService;

    public SystemAuditLogController(AuditApplicationService auditApplicationService) {
        this.auditApplicationService = auditApplicationService;
    }

    @GetMapping
    public List<AuditLogResponse> search(
            @RequestParam(required = false) Long actorId,
            @RequestParam(required = false) String action,
            @RequestParam(required = false) String objectType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to,
            @RequestParam(defaultValue = "50") int limit,
            @RequestParam(defaultValue = "0") int offset
    ) {
        return auditApplicationService.search(new AuditLogQuery(actorId, action, objectType, from, to, limit, offset))
                .stream()
                .map(AuditLogResponse::from)
                .toList();
    }
}
