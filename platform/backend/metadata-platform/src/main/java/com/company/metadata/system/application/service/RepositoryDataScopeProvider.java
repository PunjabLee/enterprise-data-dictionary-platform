package com.company.metadata.system.application.service;

import com.company.metadata.common.security.CurrentUser;
import com.company.metadata.common.security.CurrentUserProvider;
import com.company.metadata.common.security.EffectiveDataScope;
import com.company.metadata.common.security.DataScopeProvider;
import com.company.metadata.system.domain.model.DataScope;
import com.company.metadata.system.domain.repository.DataScopeRepository;
import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Service;

/**
 * 系统管理应用服务，编排权限校验、领域仓储和审计记录。
 *
 * 作者：Punjab
 */
@Service
public class RepositoryDataScopeProvider implements DataScopeProvider {

    private final CurrentUserProvider currentUserProvider;
    private final DataScopeRepository dataScopeRepository;

    public RepositoryDataScopeProvider(CurrentUserProvider currentUserProvider, DataScopeRepository dataScopeRepository) {
        this.currentUserProvider = currentUserProvider;
        this.dataScopeRepository = dataScopeRepository;
    }

    @Override
    public EffectiveDataScope currentDataScope() {
        CurrentUser currentUser = currentUserProvider.currentUser().orElse(null);
        if (currentUser == null || currentUser.userId() == null) {
            return EffectiveDataScope.empty();
        }
        Set<String> businessDomains = new HashSet<>();
        Set<String> systemCodes = new HashSet<>();
        Set<Long> organizationIds = new HashSet<>();
        Set<String> assetTypes = new HashSet<>();
        for (DataScope scope : dataScopeRepository.findEffectiveScopes(currentUser.tenantId(), currentUser.userId())) {
            if (scope.scopeType() == DataScope.ScopeType.ALL) {
                return EffectiveDataScope.unrestricted();
            }
            if (scope.scopeType() == DataScope.ScopeType.BUSINESS_DOMAIN) {
                businessDomains.add(scope.scopeValue());
            } else if (scope.scopeType() == DataScope.ScopeType.SYSTEM) {
                systemCodes.add(scope.scopeValue());
            } else if (scope.scopeType() == DataScope.ScopeType.ORGANIZATION) {
                parseLong(scope.scopeValue(), organizationIds);
            } else if (scope.scopeType() == DataScope.ScopeType.ASSET_TYPE) {
                assetTypes.add(scope.scopeValue());
            }
        }
        return new EffectiveDataScope(false, businessDomains, systemCodes, organizationIds, assetTypes);
    }

    private static void parseLong(String value, Set<Long> target) {
        try {
            target.add(Long.valueOf(value));
        } catch (NumberFormatException ignored) {
            // Invalid persisted scope values are ignored instead of widening access.
        }
    }
}
