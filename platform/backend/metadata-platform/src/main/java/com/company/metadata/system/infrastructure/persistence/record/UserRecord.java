package com.company.metadata.system.infrastructure.persistence.record;

import java.time.Instant;
/**
 * 系统管理持久化记录对象，映射数据库表字段。
 *
 * 作者：Punjab
 */

public class UserRecord {
    public Long id;
    public String tenantId;
    public Long organizationId;
    public String username;
    public String displayName;
    public String email;
    public String mobile;
    public String status;
    public String source;
    public String externalId;
    public Instant createdAt;
    public Instant updatedAt;
}
