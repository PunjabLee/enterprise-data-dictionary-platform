package com.company.metadata.system.infrastructure.persistence.record;

import java.time.Instant;
/**
 * 系统管理持久化记录对象，映射数据库表字段。
 *
 * 作者：Punjab
 */

public class DataScopeRecord {
    public Long id;
    public String tenantId;
    public String subjectType;
    public Long subjectId;
    public String scopeType;
    public String scopeValue;
    public Instant createdAt;
}
