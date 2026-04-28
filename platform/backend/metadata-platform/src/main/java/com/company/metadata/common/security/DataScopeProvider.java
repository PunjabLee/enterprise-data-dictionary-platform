package com.company.metadata.common.security;
/**
 * 安全基础组件，负责当前用户、权限和数据范围处理。
 *
 * 作者：Punjab
 */

public interface DataScopeProvider {

    EffectiveDataScope currentDataScope();
}
