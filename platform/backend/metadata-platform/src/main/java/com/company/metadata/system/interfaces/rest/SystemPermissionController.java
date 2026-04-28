package com.company.metadata.system.interfaces.rest;

import com.company.metadata.system.application.service.PermissionApplicationService;
import com.company.metadata.system.interfaces.rest.dto.PermissionResponse;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统管理 REST 接口，暴露用户、角色、权限、审计等基础能力。
 *
 * 作者：Punjab
 */
@RestController
@RequestMapping("/system/permissions")
public class SystemPermissionController {

    private final PermissionApplicationService permissionApplicationService;

    public SystemPermissionController(PermissionApplicationService permissionApplicationService) {
        this.permissionApplicationService = permissionApplicationService;
    }

    @GetMapping
    public List<PermissionResponse> findAll(@RequestParam(defaultValue = "false") boolean includeDisabled) {
        return permissionApplicationService.findAll(includeDisabled).stream()
                .map(PermissionResponse::from)
                .toList();
    }
}
