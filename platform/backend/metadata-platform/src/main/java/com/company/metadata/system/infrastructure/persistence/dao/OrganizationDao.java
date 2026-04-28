package com.company.metadata.system.infrastructure.persistence.dao;

import com.company.metadata.system.infrastructure.config.SystemPersistenceEnabled;
import com.company.metadata.system.infrastructure.persistence.mapper.OrganizationMapper;
import com.company.metadata.system.infrastructure.persistence.record.OrganizationRecord;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * 系统管理 DAO 组件，封装 MyBatis Mapper 调用。
 *
 * 作者：Punjab
 */
@Repository
@SystemPersistenceEnabled
public class OrganizationDao {

    private final OrganizationMapper organizationMapper;

    public OrganizationDao(OrganizationMapper organizationMapper) {
        this.organizationMapper = organizationMapper;
    }

    public OrganizationRecord insert(OrganizationRecord record) {
        organizationMapper.insert(record);
        return record;
    }

    public OrganizationRecord selectById(String tenantId, Long id) {
        return organizationMapper.selectById(tenantId, id);
    }

    public List<OrganizationRecord> selectAll(String tenantId) {
        return organizationMapper.selectAll(tenantId);
    }
}
