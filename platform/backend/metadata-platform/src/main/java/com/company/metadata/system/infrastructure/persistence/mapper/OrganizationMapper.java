package com.company.metadata.system.infrastructure.persistence.mapper;

import com.company.metadata.system.infrastructure.persistence.record.OrganizationRecord;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 系统管理 MyBatis Mapper，定义系统表访问语句。
 *
 * 作者：Punjab
 */
@Mapper
public interface OrganizationMapper {

    @Insert("""
            INSERT INTO sys_organization (tenant_id, parent_id, code, name, status)
            VALUES (#{tenantId}, #{parentId}, #{code}, #{name}, #{status})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insert(OrganizationRecord record);

    @Select("""
            SELECT id, tenant_id, parent_id, code, name, status, created_at, updated_at
            FROM sys_organization
            WHERE tenant_id = #{tenantId}
              AND id = #{id}
            """)
    OrganizationRecord selectById(@Param("tenantId") String tenantId, @Param("id") Long id);

    @Select("""
            SELECT id, tenant_id, parent_id, code, name, status, created_at, updated_at
            FROM sys_organization
            WHERE tenant_id = #{tenantId}
            ORDER BY code
            """)
    List<OrganizationRecord> selectAll(@Param("tenantId") String tenantId);
}
