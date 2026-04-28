package com.company.metadata.metadata.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 资产创建请求，和开放 API 中的资产 DTO 保持字段语义一致。
 *
 * 作者：Punjab
 */
public record CreateAssetRequest(
        @NotBlank String assetType,
        @NotBlank String code,
        @NotBlank String name,
        String displayName,
        String description,
        String businessDomain,
        String systemCode,
        Long ownerUserId,
        Long stewardUserId,
        String attributesJson
) {
}
