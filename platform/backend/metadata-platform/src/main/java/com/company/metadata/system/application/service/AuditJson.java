package com.company.metadata.system.application.service;

import java.util.Collection;
import java.util.stream.Collectors;
/**
 * 系统管理应用服务，编排权限校验、领域仓储和审计记录。
 *
 * 作者：Punjab
 */

final class AuditJson {

    private AuditJson() {
    }

    static String stringField(String field, String value) {
        return "{\"" + escape(field) + "\":\"" + escape(value) + "\"}";
    }

    static String idField(String field, Long value) {
        return "{\"" + escape(field) + "\":" + value + "}";
    }

    static String stringArrayField(String field, Collection<String> values) {
        String array = values.stream()
                .map(value -> "\"" + escape(value) + "\"")
                .collect(Collectors.joining(",", "[", "]"));
        return "{\"" + escape(field) + "\":" + array + "}";
    }

    private static String escape(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
