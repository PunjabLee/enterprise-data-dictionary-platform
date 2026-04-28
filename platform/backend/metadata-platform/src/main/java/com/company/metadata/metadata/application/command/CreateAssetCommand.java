package com.company.metadata.metadata.application.command;

/**
 * 元数据资产创建命令，承载资产目录的最小必填信息。
 *
 * 作者：Punjab
 */
public record CreateAssetCommand(
        String assetType,
        String code,
        String name,
        String displayName,
        String description,
        String businessDomain,
        String systemCode,
        Long ownerUserId,
        Long stewardUserId,
        String attributesJson
) {
}
