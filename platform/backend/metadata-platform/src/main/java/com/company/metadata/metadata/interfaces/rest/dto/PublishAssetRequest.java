package com.company.metadata.metadata.interfaces.rest.dto;

/**
 * 资产发布请求，记录发布备注以便版本追溯。
 *
 * 作者：Punjab
 */
public record PublishAssetRequest(
        String comment
) {
}
