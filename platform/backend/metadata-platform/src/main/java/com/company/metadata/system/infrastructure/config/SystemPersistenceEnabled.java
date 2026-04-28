package com.company.metadata.system.infrastructure.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ConditionalOnProperty(
        prefix = "metadata.system.persistence",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true
)
/**
 * 系统管理持久化配置组件，控制 MyBatis 仓储装配。
 *
 * 作者：Punjab
 */
public @interface SystemPersistenceEnabled {
}
