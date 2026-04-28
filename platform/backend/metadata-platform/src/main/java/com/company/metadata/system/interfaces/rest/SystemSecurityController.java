package com.company.metadata.system.interfaces.rest;

import com.company.metadata.system.application.service.SecurityApplicationService;
import com.company.metadata.system.interfaces.rest.dto.CurrentSecurityResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统管理 REST 接口，暴露用户、角色、权限、审计等基础能力。
 *
 * 作者：Punjab
 */
@RestController
@RequestMapping("/system/security")
public class SystemSecurityController {

    private final SecurityApplicationService securityApplicationService;

    public SystemSecurityController(SecurityApplicationService securityApplicationService) {
        this.securityApplicationService = securityApplicationService;
    }

    @GetMapping("/me")
    public CurrentSecurityResponse current() {
        return CurrentSecurityResponse.from(securityApplicationService.current());
    }
}
