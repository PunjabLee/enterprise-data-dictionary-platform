package com.company.metadata.metadata.application.query;

/**
 * 字段字典查询条件，支持按资产、关键字和敏感标识筛选。
 *
 * 作者：Punjab
 */
public record FieldDictionaryQuery(
        String keyword,
        Long assetId,
        Boolean sensitive,
        String status,
        int limit,
        int offset
) {
}
