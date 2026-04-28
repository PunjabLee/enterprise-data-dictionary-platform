package com.company.metadata.system.infrastructure.persistence.record;

import java.time.Instant;
/**
 * 系统管理持久化记录对象，映射数据库表字段。
 *
 * 作者：Punjab
 */

public class OrganizationRecord {
    public Long id;
    public String tenantId;
    public Long parentId;
    public String code;
    public String name;
    public String status;
    public Instant createdAt;
    public Instant updatedAt;
}
