package com.company.metadata.system.domain.repository;

import com.company.metadata.system.domain.model.Organization;
import java.util.List;
import java.util.Optional;
/**
 * 系统管理仓储接口，隔离应用层和持久化实现。
 *
 * 作者：Punjab
 */

public interface OrganizationRepository {

    Organization save(Organization organization);

    Optional<Organization> findById(String tenantId, Long id);

    List<Organization> findAll(String tenantId);
}
