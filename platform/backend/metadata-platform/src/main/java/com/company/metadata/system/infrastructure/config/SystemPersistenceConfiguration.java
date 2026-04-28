package com.company.metadata.system.infrastructure.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * 系统管理持久化配置组件，控制 MyBatis 仓储装配。
 *
 * 作者：Punjab
 */
@Configuration
@SystemPersistenceEnabled
@MapperScan("com.company.metadata.system.infrastructure.persistence.mapper")
public class SystemPersistenceConfiguration {
}
