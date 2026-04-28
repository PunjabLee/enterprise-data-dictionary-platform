package com.company.metadata.system.infrastructure.persistence.mapper;

import com.company.metadata.system.infrastructure.persistence.record.DataScopeRecord;
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
public interface DataScopeMapper {

    @Insert("""
            INSERT INTO sys_data_scope (tenant_id, subject_type, subject_id, scope_type, scope_value)
            VALUES (#{tenantId}, #{subjectType}, #{subjectId}, #{scopeType}, #{scopeValue})
            ON CONFLICT (tenant_id, subject_type, subject_id, scope_type, scope_value) DO UPDATE
                SET scope_value = EXCLUDED.scope_value
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insert(DataScopeRecord record);

    @Select("""
            SELECT id, tenant_id, subject_type, subject_id, scope_type, scope_value, created_at
            FROM sys_data_scope
            WHERE tenant_id = #{tenantId}
              AND subject_type = #{subjectType}
              AND subject_id = #{subjectId}
            ORDER BY scope_type, scope_value
            """)
    List<DataScopeRecord> findBySubject(
            @Param("tenantId") String tenantId,
            @Param("subjectType") String subjectType,
            @Param("subjectId") Long subjectId
    );

    @Select("""
            SELECT id, tenant_id, subject_type, subject_id, scope_type, scope_value, created_at
            FROM sys_data_scope
            WHERE tenant_id = #{tenantId}
              AND subject_type = 'USER'
              AND subject_id = #{userId}
            UNION
            SELECT ds.id, ds.tenant_id, ds.subject_type, ds.subject_id, ds.scope_type, ds.scope_value, ds.created_at
            FROM sys_data_scope ds
            JOIN sys_user_role ur
              ON ur.tenant_id = ds.tenant_id
             AND ur.role_id = ds.subject_id
            WHERE ds.tenant_id = #{tenantId}
              AND ds.subject_type = 'ROLE'
              AND ur.user_id = #{userId}
            """)
    List<DataScopeRecord> findEffectiveScopes(
            @Param("tenantId") String tenantId,
            @Param("userId") Long userId
    );
}
