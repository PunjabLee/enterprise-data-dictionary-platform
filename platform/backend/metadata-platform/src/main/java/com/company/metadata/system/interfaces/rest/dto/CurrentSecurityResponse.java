package com.company.metadata.system.interfaces.rest.dto;

import com.company.metadata.common.security.EffectiveDataScope;
import com.company.metadata.system.application.query.CurrentSecurityView;
import java.util.Set;
/**
 * 系统管理接口数据对象，定义请求或响应数据结构。
 *
 * 作者：Punjab
 */

public record CurrentSecurityResponse(
        String tenantId,
        Long userId,
        String username,
        String displayName,
        Set<String> roles,
        Set<String> permissions,
        EffectiveDataScope dataScope
) {

    public static CurrentSecurityResponse from(CurrentSecurityView view) {
        return new CurrentSecurityResponse(
                view.user().tenantId(),
                view.user().userId(),
                view.user().username(),
                view.user().displayName(),
                view.roles(),
                view.permissions(),
                view.dataScope()
        );
    }
}
