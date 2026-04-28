package com.company.metadata.metadata.infrastructure.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * 元数据模块持久化配置，隔离 Mapper 扫描范围。
 *
 * 作者：Punjab
 */
@Configuration
@MetadataPersistenceEnabled
@MapperScan("com.company.metadata.metadata.infrastructure.persistence.mapper")
public class MetadataPersistenceConfiguration {
}
