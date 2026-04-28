package com.company.metadata.metadata.application.service;

/**
 * 元数据模块权限编码，和数据库初始化权限保持一致。
 *
 * 作者：Punjab
 */
public final class MetadataPermissionCodes {

    public static final String ASSET_READ = "metadata:asset:read";
    public static final String ASSET_CREATE = "metadata:asset:create";
    public static final String ASSET_UPDATE = "metadata:asset:update";
    public static final String ASSET_PUBLISH = "metadata:asset:publish";
    public static final String FIELD_READ = "metadata:field:read";
    public static final String FIELD_UPDATE = "metadata:field:update";

    private MetadataPermissionCodes() {
    }
}
