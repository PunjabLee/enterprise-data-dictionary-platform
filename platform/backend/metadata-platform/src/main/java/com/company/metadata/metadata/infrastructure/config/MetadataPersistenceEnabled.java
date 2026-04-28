package com.company.metadata.metadata.infrastructure.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * 元数据模块持久化开关，便于无数据库环境加载应用上下文。
 *
 * 作者：Punjab
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ConditionalOnProperty(
        prefix = "metadata.metadata.persistence",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true
)
public @interface MetadataPersistenceEnabled {
}
