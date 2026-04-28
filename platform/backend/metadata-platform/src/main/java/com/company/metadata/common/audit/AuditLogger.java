package com.company.metadata.common.audit;
/**
 * 审计基础组件，负责统一记录系统操作痕迹。
 *
 * 作者：Punjab
 */

public interface AuditLogger {

    void record(AuditEntry entry);
}
