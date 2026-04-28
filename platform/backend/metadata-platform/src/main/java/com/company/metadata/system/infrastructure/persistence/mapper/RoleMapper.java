package com.company.metadata.system.infrastructure.persistence.mapper;

import com.company.metadata.system.infrastructure.persistence.record.RoleRecord;
import java.util.List;
import java.util.Set;
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
public interface RoleMapper {

    @Insert("""
            INSERT INTO sys_role (tenant_id, code, name, description, built_in, enabled)
            VALUES (#{tenantId}, #{code}, #{name}, #{description}, #{builtIn}, #{enabled})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insert(RoleRecord record);

    @Select("""
            SELECT id, tenant_id, code, name, description, built_in, enabled, created_at, updated_at
            FROM sys_role
            WHERE tenant_id = #{tenantId}
              AND id = #{id}
            """)
    RoleRecord selectById(@Param("tenantId") String tenantId, @Param("id") Long id);

    @Select("""
            <script>
            SELECT id, tenant_id, code, name, description, built_in, enabled, created_at, updated_at
            FROM sys_role
            WHERE tenant_id = #{tenantId}
            <if test="includeDisabled == false">
              AND enabled = true
            </if>
            ORDER BY built_in DESC, code
            </script>
            """)
    List<RoleRecord> selectAll(@Param("tenantId") String tenantId, @Param("includeDisabled") boolean includeDisabled);

    @Select("""
            SELECT r.code
            FROM sys_user_role ur
            JOIN sys_role r
              ON r.tenant_id = ur.tenant_id
             AND r.id = ur.role_id
             AND r.enabled = true
            WHERE ur.tenant_id = #{tenantId}
              AND ur.user_id = #{userId}
            ORDER BY r.code
            """)
    Set<String> selectRoleCodesByUserId(@Param("tenantId") String tenantId, @Param("userId") Long userId);
}
