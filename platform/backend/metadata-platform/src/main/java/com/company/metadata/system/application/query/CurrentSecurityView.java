package com.company.metadata.system.application.query;

import com.company.metadata.common.security.CurrentUser;
import com.company.metadata.common.security.EffectiveDataScope;
import java.util.Set;
/**
 * 系统管理查询视图，承载接口层返回的查询结果。
 *
 * 作者：Punjab
 */

public record CurrentSecurityView(
        CurrentUser user,
        Set<String> roles,
        Set<String> permissions,
        EffectiveDataScope dataScope
) {

    public CurrentSecurityView {
        roles = roles == null ? Set.of() : Set.copyOf(roles);
        permissions = permissions == null ? Set.of() : Set.copyOf(permissions);
    }
}
