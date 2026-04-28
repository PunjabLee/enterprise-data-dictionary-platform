package com.company.metadata.common.audit;
/**
 * 审计基础组件，负责统一记录系统操作痕迹。
 *
 * 作者：Punjab
 */

public record AuditEntry(
        String action,
        String objectType,
        String objectId,
        String beforeValue,
        String afterValue,
        String result
) {

    public static AuditEntry success(String action, String objectType, String objectId, String afterValue) {
        return new AuditEntry(action, objectType, objectId, null, afterValue, "success");
    }
}
