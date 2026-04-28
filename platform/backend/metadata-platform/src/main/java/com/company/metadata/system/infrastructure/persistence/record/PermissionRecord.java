package com.company.metadata.system.infrastructure.persistence.record;

import java.time.Instant;
/**
 * 系统管理持久化记录对象，映射数据库表字段。
 *
 * 作者：Punjab
 */

public class PermissionRecord {
    public Long id;
    public String tenantId;
    public String code;
    public String name;
    public String category;
    public String resource;
    public String action;
    public String description;
    public boolean enabled;
    public Instant createdAt;
    public Instant updatedAt;
}
