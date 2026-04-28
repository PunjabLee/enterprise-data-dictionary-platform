package com.company.metadata.system.interfaces.rest;

import com.company.metadata.system.application.command.AssignRolePermissionsCommand;
import com.company.metadata.system.application.command.CreateRoleCommand;
import com.company.metadata.system.application.command.GrantDataScopeCommand;
import com.company.metadata.system.application.service.RoleApplicationService;
import com.company.metadata.system.domain.model.DataScope;
import com.company.metadata.system.interfaces.rest.dto.AssignRolePermissionsRequest;
import com.company.metadata.system.interfaces.rest.dto.CreateRoleRequest;
import com.company.metadata.system.interfaces.rest.dto.DataScopeResponse;
import com.company.metadata.system.interfaces.rest.dto.GrantDataScopeRequest;
import com.company.metadata.system.interfaces.rest.dto.RoleResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统管理 REST 接口，暴露用户、角色、权限、审计等基础能力。
 *
 * 作者：Punjab
 */
@RestController
@RequestMapping("/system/roles")
public class SystemRoleController {

    private final RoleApplicationService roleApplicationService;

    public SystemRoleController(RoleApplicationService roleApplicationService) {
        this.roleApplicationService = roleApplicationService;
    }

    @GetMapping
    public List<RoleResponse> findAll(@RequestParam(defaultValue = "false") boolean includeDisabled) {
        return roleApplicationService.findAll(includeDisabled).stream()
                .map(RoleResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    public RoleResponse findById(@PathVariable Long id) {
        return RoleResponse.from(roleApplicationService.findById(id));
    }

    @PostMapping
    public RoleResponse create(@Valid @RequestBody CreateRoleRequest request) {
        return RoleResponse.from(roleApplicationService.create(new CreateRoleCommand(
                request.code(),
                request.name(),
                request.description()
        )));
    }

    @PutMapping("/{id}/permissions")
    public void assignPermissions(
            @PathVariable Long id,
            @Valid @RequestBody AssignRolePermissionsRequest request
    ) {
        roleApplicationService.assignPermissions(new AssignRolePermissionsCommand(id, request.permissionCodes()));
    }

    @GetMapping("/{id}/data-scopes")
    public List<DataScopeResponse> findDataScopes(@PathVariable Long id) {
        return roleApplicationService.findDataScopes(id).stream()
                .map(DataScopeResponse::from)
                .toList();
    }

    @PostMapping("/{id}/data-scopes")
    public DataScopeResponse grantDataScope(
            @PathVariable Long id,
            @Valid @RequestBody GrantDataScopeRequest request
    ) {
        return DataScopeResponse.from(roleApplicationService.grantDataScope(new GrantDataScopeCommand(
                DataScope.SubjectType.ROLE,
                id,
                request.scopeType(),
                request.scopeValue()
        )));
    }
}
