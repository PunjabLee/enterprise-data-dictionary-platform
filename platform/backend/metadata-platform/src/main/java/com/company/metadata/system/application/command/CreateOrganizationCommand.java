package com.company.metadata.system.application.command;
/**
 * 系统管理应用命令对象，承载接口层传入的业务指令。
 *
 * 作者：Punjab
 */

public record CreateOrganizationCommand(
        Long parentId,
        String code,
        String name
) {
}
