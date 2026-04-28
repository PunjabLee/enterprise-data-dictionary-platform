package com.company.metadata.metadata.application.query;

/**
 * 资产目录查询条件，保持列表筛选参数稳定。
 *
 * 作者：Punjab
 */
public record AssetSearchQuery(
        String keyword,
        String assetType,
        String status,
        String businessDomain,
        String systemCode,
        int limit,
        int offset
) {
}
