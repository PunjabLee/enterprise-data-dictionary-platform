package com.company.metadata.system.infrastructure.persistence.repository;

import com.company.metadata.system.domain.model.Organization;
import com.company.metadata.system.domain.repository.OrganizationRepository;
import com.company.metadata.system.infrastructure.config.SystemPersistenceEnabled;
import com.company.metadata.system.infrastructure.persistence.dao.OrganizationDao;
import com.company.metadata.system.infrastructure.persistence.record.OrganizationRecord;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * 系统管理仓储实现，完成领域模型与持久化记录转换。
 *
 * 作者：Punjab
 */
@Repository
@SystemPersistenceEnabled
public class MybatisOrganizationRepository implements OrganizationRepository {

    private final OrganizationDao organizationDao;

    public MybatisOrganizationRepository(OrganizationDao organizationDao) {
        this.organizationDao = organizationDao;
    }

    @Override
    public Organization save(Organization organization) {
        return toDomain(organizationDao.insert(toRecord(organization)));
    }

    @Override
    public Optional<Organization> findById(String tenantId, Long id) {
        return Optional.ofNullable(organizationDao.selectById(tenantId, id))
                .map(MybatisOrganizationRepository::toDomain);
    }

    @Override
    public List<Organization> findAll(String tenantId) {
        return organizationDao.selectAll(tenantId).stream()
                .map(MybatisOrganizationRepository::toDomain)
                .toList();
    }

    private static OrganizationRecord toRecord(Organization organization) {
        OrganizationRecord record = new OrganizationRecord();
        record.id = organization.id();
        record.tenantId = organization.tenantId();
        record.parentId = organization.parentId();
        record.code = organization.code();
        record.name = organization.name();
        record.status = organization.status();
        record.createdAt = organization.createdAt();
        record.updatedAt = organization.updatedAt();
        return record;
    }

    private static Organization toDomain(OrganizationRecord record) {
        return new Organization(
                record.id,
                record.tenantId,
                record.parentId,
                record.code,
                record.name,
                record.status,
                record.createdAt,
                record.updatedAt
        );
    }
}
