package com.company.metadata.common.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 安全基础组件，负责当前用户、权限和数据范围处理。
 *
 * 作者：Punjab
 */
@Component
public class HeaderAuthenticationFilter extends OncePerRequestFilter {

    private static final String USER_ID_HEADER = "X-User-Id";
    private static final String USERNAME_HEADER = "X-Username";
    private static final String DISPLAY_NAME_HEADER = "X-Display-Name";
    private static final String TENANT_ID_HEADER = "X-Tenant-Id";
    private static final String ROLES_HEADER = "X-Roles";
    private static final String PERMISSIONS_HEADER = "X-Permissions";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            CurrentUser currentUser = readCurrentUser(request);
            if (currentUser != null) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        currentUser,
                        null,
                        currentUser.permissionCodes().stream()
                                .map(SimpleGrantedAuthority::new)
                                .toList()
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }

    private CurrentUser readCurrentUser(HttpServletRequest request) {
        String username = trimToNull(request.getHeader(USERNAME_HEADER));
        Long userId = parseLong(trimToNull(request.getHeader(USER_ID_HEADER)));
        if (username == null && userId == null) {
            return null;
        }
        return new CurrentUser(
                valueOrDefault(trimToNull(request.getHeader(TENANT_ID_HEADER)), SecurityConstants.DEFAULT_TENANT_ID),
                userId,
                valueOrDefault(username, "user-" + userId),
                trimToNull(request.getHeader(DISPLAY_NAME_HEADER)),
                csv(request.getHeader(ROLES_HEADER)),
                csv(request.getHeader(PERMISSIONS_HEADER))
        );
    }

    private static Set<String> csv(String value) {
        if (value == null || value.isBlank()) {
            return Set.of();
        }
        return Arrays.stream(value.split(","))
                .map(String::trim)
                .filter(item -> !item.isBlank())
                .collect(Collectors.toUnmodifiableSet());
    }

    private static Long parseLong(String value) {
        if (value == null) {
            return null;
        }
        try {
            return Long.valueOf(value);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    private static String trimToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

    private static String valueOrDefault(String value, String defaultValue) {
        return value == null ? defaultValue : value;
    }
}
