package com.company.metadata.common.security;

import java.util.Set;
/**
 * 安全基础组件，负责当前用户、权限和数据范围处理。
 *
 * 作者：Punjab
 */

public record EffectiveDataScope(
        boolean allData,
        Set<String> businessDomains,
        Set<String> systemCodes,
        Set<Long> organizationIds,
        Set<String> assetTypes
) {

    public EffectiveDataScope {
        businessDomains = businessDomains == null ? Set.of() : Set.copyOf(businessDomains);
        systemCodes = systemCodes == null ? Set.of() : Set.copyOf(systemCodes);
        organizationIds = organizationIds == null ? Set.of() : Set.copyOf(organizationIds);
        assetTypes = assetTypes == null ? Set.of() : Set.copyOf(assetTypes);
    }

    public static EffectiveDataScope unrestricted() {
        return new EffectiveDataScope(true, Set.of(), Set.of(), Set.of(), Set.of());
    }

    public static EffectiveDataScope empty() {
        return new EffectiveDataScope(false, Set.of(), Set.of(), Set.of(), Set.of());
    }
}
