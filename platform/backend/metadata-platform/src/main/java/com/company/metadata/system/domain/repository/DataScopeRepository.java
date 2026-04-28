package com.company.metadata.system.domain.repository;

import com.company.metadata.system.domain.model.DataScope;
import java.util.List;
/**
 * 系统管理仓储接口，隔离应用层和持久化实现。
 *
 * 作者：Punjab
 */

public interface DataScopeRepository {

    DataScope save(DataScope dataScope);

    List<DataScope> findBySubject(String tenantId, DataScope.SubjectType subjectType, Long subjectId);

    List<DataScope> findEffectiveScopes(String tenantId, Long userId);
}
