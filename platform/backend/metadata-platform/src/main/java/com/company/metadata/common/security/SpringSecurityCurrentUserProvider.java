package com.company.metadata.common.security;

import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * 安全基础组件，负责当前用户、权限和数据范围处理。
 *
 * 作者：Punjab
 */
@Component
public class SpringSecurityCurrentUserProvider implements CurrentUserProvider {

    @Override
    public Optional<CurrentUser> currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof CurrentUser currentUser) {
            return Optional.of(currentUser);
        }
        return Optional.empty();
    }
}
