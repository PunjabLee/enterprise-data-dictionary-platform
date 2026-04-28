package com.company.metadata.system.infrastructure.persistence.record;

import java.time.Instant;
/**
 * 系统管理持久化记录对象，映射数据库表字段。
 *
 * 作者：Punjab
 */

public class AuditLogRecord {
    public Long id;
    public String tenantId;
    public Long actorId;
    public String actorName;
    public String action;
    public String objectType;
    public String objectId;
    public String beforeValue;
    public String afterValue;
    public String result;
    public String ip;
    public String userAgent;
    public String traceId;
    public Instant createdAt;
}
