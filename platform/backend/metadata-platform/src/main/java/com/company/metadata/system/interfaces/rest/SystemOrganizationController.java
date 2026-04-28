package com.company.metadata.system.interfaces.rest;

import com.company.metadata.system.application.command.CreateOrganizationCommand;
import com.company.metadata.system.application.service.OrganizationApplicationService;
import com.company.metadata.system.interfaces.rest.dto.CreateOrganizationRequest;
import com.company.metadata.system.interfaces.rest.dto.OrganizationResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统管理 REST 接口，暴露用户、角色、权限、审计等基础能力。
 *
 * 作者：Punjab
 */
@RestController
@RequestMapping("/system/organizations")
public class SystemOrganizationController {

    private final OrganizationApplicationService organizationApplicationService;

    public SystemOrganizationController(OrganizationApplicationService organizationApplicationService) {
        this.organizationApplicationService = organizationApplicationService;
    }

    @GetMapping
    public List<OrganizationResponse> findAll() {
        return organizationApplicationService.findAll().stream()
                .map(OrganizationResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    public OrganizationResponse findById(@PathVariable Long id) {
        return OrganizationResponse.from(organizationApplicationService.findById(id));
    }

    @PostMapping
    public OrganizationResponse create(@Valid @RequestBody CreateOrganizationRequest request) {
        return OrganizationResponse.from(organizationApplicationService.create(new CreateOrganizationCommand(
                request.parentId(),
                request.code(),
                request.name()
        )));
    }
}
