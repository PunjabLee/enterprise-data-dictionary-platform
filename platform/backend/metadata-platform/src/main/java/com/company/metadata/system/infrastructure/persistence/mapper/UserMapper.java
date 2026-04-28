package com.company.metadata.system.infrastructure.persistence.mapper;

import com.company.metadata.system.infrastructure.persistence.record.UserRecord;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 系统管理 MyBatis Mapper，定义系统表访问语句。
 *
 * 作者：Punjab
 */
@Mapper
public interface UserMapper {

    @Insert("""
            INSERT INTO sys_user (
                tenant_id, organization_id, username, display_name, email, mobile,
                status, source, external_id
            ) VALUES (
                #{tenantId}, #{organizationId}, #{username}, #{displayName}, #{email}, #{mobile},
                #{status}, #{source}, #{externalId}
            )
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insert(UserRecord record);

    @Update("""
            UPDATE sys_user
            SET organization_id = #{organizationId},
                display_name = #{displayName},
                email = #{email},
                mobile = #{mobile},
                status = #{status},
                updated_at = now()
            WHERE tenant_id = #{tenantId}
              AND id = #{id}
            """)
    void update(UserRecord record);

    @Select("""
            SELECT id, tenant_id, organization_id, username, display_name, email, mobile,
                   status, source, external_id, created_at, updated_at
            FROM sys_user
            WHERE tenant_id = #{tenantId}
              AND id = #{id}
            """)
    UserRecord selectById(@Param("tenantId") String tenantId, @Param("id") Long id);

    @Select("""
            <script>
            SELECT id, tenant_id, organization_id, username, display_name, email, mobile,
                   status, source, external_id, created_at, updated_at
            FROM sys_user
            WHERE tenant_id = #{tenantId}
            <if test="keyword != null and keyword != ''">
              AND (
                username ILIKE CONCAT('%', #{keyword}, '%')
                OR display_name ILIKE CONCAT('%', #{keyword}, '%')
                OR email ILIKE CONCAT('%', #{keyword}, '%')
              )
            </if>
            <if test="status != null and status != ''">
              AND status = #{status}
            </if>
            ORDER BY id DESC
            LIMIT #{limit} OFFSET #{offset}
            </script>
            """)
    List<UserRecord> search(
            @Param("tenantId") String tenantId,
            @Param("keyword") String keyword,
            @Param("status") String status,
            @Param("limit") int limit,
            @Param("offset") int offset
    );

    @Delete("""
            DELETE FROM sys_user_role
            WHERE tenant_id = #{tenantId}
              AND user_id = #{userId}
            """)
    void deleteUserRoles(@Param("tenantId") String tenantId, @Param("userId") Long userId);

    @Insert("""
            INSERT INTO sys_user_role (tenant_id, user_id, role_id)
            SELECT #{tenantId}, #{userId}, id
            FROM sys_role
            WHERE tenant_id = #{tenantId}
              AND code = #{roleCode}
              AND enabled = true
            ON CONFLICT DO NOTHING
            """)
    void insertUserRoleByCode(
            @Param("tenantId") String tenantId,
            @Param("userId") Long userId,
            @Param("roleCode") String roleCode
    );
}
