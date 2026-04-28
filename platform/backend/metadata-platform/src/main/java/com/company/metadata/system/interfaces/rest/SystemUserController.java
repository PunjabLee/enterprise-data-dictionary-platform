package com.company.metadata.system.interfaces.rest;

import com.company.metadata.system.application.command.AssignUserRolesCommand;
import com.company.metadata.system.application.command.CreateUserCommand;
import com.company.metadata.system.application.service.UserApplicationService;
import com.company.metadata.system.interfaces.rest.dto.AssignUserRolesRequest;
import com.company.metadata.system.interfaces.rest.dto.CreateUserRequest;
import com.company.metadata.system.interfaces.rest.dto.UserResponse;
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
@RequestMapping("/system/users")
public class SystemUserController {

    private final UserApplicationService userApplicationService;

    public SystemUserController(UserApplicationService userApplicationService) {
        this.userApplicationService = userApplicationService;
    }

    @GetMapping
    public List<UserResponse> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "50") int limit,
            @RequestParam(defaultValue = "0") int offset
    ) {
        return userApplicationService.search(keyword, status, limit, offset).stream()
                .map(UserResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    public UserResponse findById(@PathVariable Long id) {
        return UserResponse.from(userApplicationService.findById(id));
    }

    @PostMapping
    public UserResponse create(@Valid @RequestBody CreateUserRequest request) {
        return UserResponse.from(userApplicationService.create(new CreateUserCommand(
                request.organizationId(),
                request.username(),
                request.displayName(),
                request.email(),
                request.mobile()
        )));
    }

    @PutMapping("/{id}/disabled")
    public UserResponse disable(@PathVariable Long id) {
        return UserResponse.from(userApplicationService.disable(id));
    }

    @PutMapping("/{id}/roles")
    public void assignRoles(@PathVariable Long id, @Valid @RequestBody AssignUserRolesRequest request) {
        userApplicationService.assignRoles(new AssignUserRolesCommand(id, request.roleCodes()));
    }
}
