package com.company.metadata.system.application.command;

import java.util.Set;
/**
 * 系统管理应用命令对象，承载接口层传入的业务指令。
 *
 * 作者：Punjab
 */

public record AssignRolePermissionsCommand(Long roleId, Set<String> permissionCodes) {

    public AssignRolePermissionsCommand {
        permissionCodes = permissionCodes == null ? Set.of() : Set.copyOf(permissionCodes);
    }
}
