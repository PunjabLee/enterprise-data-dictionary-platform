package com.company.metadata.common.security;

import java.util.Optional;
/**
 * 安全基础组件，负责当前用户、权限和数据范围处理。
 *
 * 作者：Punjab
 */

public interface CurrentUserProvider {

    Optional<CurrentUser> currentUser();

    default CurrentUser requireCurrentUser() {
        return currentUser().orElseThrow(() -> new UnauthenticatedException("Authentication is required"));
    }
}
