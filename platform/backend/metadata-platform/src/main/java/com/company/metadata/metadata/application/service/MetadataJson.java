package com.company.metadata.metadata.application.service;

import com.company.metadata.metadata.domain.model.MetadataAsset;
import com.company.metadata.metadata.domain.model.MetadataField;
import java.util.List;

/**
 * 元数据模块轻量 JSON 工具，当前仅用于审计和版本快照。
 *
 * 作者：Punjab
 */
final class MetadataJson {

    private MetadataJson() {
    }

    static String idField(String field, Long value) {
        return "{\"" + escape(field) + "\":" + value + "}";
    }

    static String assetSnapshot(MetadataAsset asset, List<MetadataField> fields) {
        return "{"
                + "\"assetId\":" + asset.id()
                + ",\"assetCode\":\"" + escape(asset.code()) + "\""
                + ",\"assetType\":\"" + escape(asset.assetType()) + "\""
                + ",\"status\":\"" + escape(asset.status()) + "\""
                + ",\"version\":" + asset.version()
                + ",\"fieldCount\":" + fields.size()
                + "}";
    }

    private static String escape(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
