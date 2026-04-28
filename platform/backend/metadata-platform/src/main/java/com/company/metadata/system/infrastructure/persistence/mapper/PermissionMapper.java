package com.company.metadata.system.infrastructure.persistence.mapper;

import com.company.metadata.system.infrastructure.persistence.record.PermissionRecord;
import java.util.List;
import java.util.Set;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 系统管理 MyBatis Mapper，定义系统表访问语句。
 *
 * 作者：Punjab
 */
@Mapper
public interface PermissionMapper {

    @Select("""
            <script>
            SELECT id, tenant_id, code, name, category, resource, action, description,
                   enabled, created_at, updated_at
            FROM sys_permission
            WHERE tenant_id = #{tenantId}
            <if test="includeDisabled == false">
              AND enabled = true
            </if>
            ORDER BY resource, action, code
            </script>
            """)
    List<PermissionRecord> selectAll(
            @Param("tenantId") String tenantId,
            @Param("includeDisabled") boolean includeDisabled
    );

    @Select("""
            SELECT DISTINCT p.code
            FROM sys_user_role ur
            JOIN sys_role r
              ON r.tenant_id = ur.tenant_id
             AND r.id = ur.role_id
             AND r.enabled = true
            JOIN sys_role_permission rp
              ON rp.tenant_id = ur.tenant_id
             AND rp.role_id = ur.role_id
            JOIN sys_permission p
              ON p.tenant_id = rp.tenant_id
             AND p.id = rp.permission_id
             AND p.enabled = true
            WHERE ur.tenant_id = #{tenantId}
              AND ur.user_id = #{userId}
            """)
    Set<String> selectCodesByUserId(@Param("tenantId") String tenantId, @Param("userId") Long userId);

    @Delete("""
            DELETE FROM sys_role_permission
            WHERE tenant_id = #{tenantId}
              AND role_id = #{roleId}
            """)
    void deleteRolePermissions(@Param("tenantId") String tenantId, @Param("roleId") Long roleId);

    @Insert("""
            INSERT INTO sys_role_permission (tenant_id, role_id, permission_id)
            SELECT #{tenantId}, #{roleId}, id
            FROM sys_permission
            WHERE tenant_id = #{tenantId}
              AND code = #{permissionCode}
              AND enabled = true
            ON CONFLICT DO NOTHING
            """)
    void insertRolePermissionByCode(
            @Param("tenantId") String tenantId,
            @Param("roleId") Long roleId,
            @Param("permissionCode") String permissionCode
    );
}
