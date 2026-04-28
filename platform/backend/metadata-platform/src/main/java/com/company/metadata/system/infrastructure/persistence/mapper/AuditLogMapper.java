package com.company.metadata.system.infrastructure.persistence.mapper;

import com.company.metadata.system.infrastructure.persistence.record.AuditLogRecord;
import java.time.Instant;
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
public interface AuditLogMapper {

    @Insert("""
            INSERT INTO sys_audit_log (
                tenant_id, actor_id, actor_name, action, object_type, object_id,
                before_value, after_value, result, ip, user_agent, trace_id, created_at
            ) VALUES (
                #{tenantId}, #{actorId}, #{actorName}, #{action}, #{objectType}, #{objectId},
                CAST(#{beforeValue,jdbcType=VARCHAR} AS jsonb),
                CAST(#{afterValue,jdbcType=VARCHAR} AS jsonb),
                #{result}, #{ip}, #{userAgent}, #{traceId}, COALESCE(#{createdAt}, now())
            )
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insert(AuditLogRecord record);

    @Select("""
            <script>
            SELECT id, tenant_id, actor_id, actor_name, action, object_type, object_id,
                   before_value::text AS before_value, after_value::text AS after_value,
                   result, ip, user_agent, trace_id, created_at
            FROM sys_audit_log
            WHERE tenant_id = #{tenantId}
            <if test="actorId != null">
              AND actor_id = #{actorId}
            </if>
            <if test="action != null and action != ''">
              AND action = #{action}
            </if>
            <if test="objectType != null and objectType != ''">
              AND object_type = #{objectType}
            </if>
            <if test="from != null">
              AND created_at &gt;= #{from}
            </if>
            <if test="to != null">
              AND created_at &lt;= #{to}
            </if>
            ORDER BY created_at DESC, id DESC
            LIMIT #{limit} OFFSET #{offset}
            </script>
            """)
    List<AuditLogRecord> search(
            @Param("tenantId") String tenantId,
            @Param("actorId") Long actorId,
            @Param("action") String action,
            @Param("objectType") String objectType,
            @Param("from") Instant from,
            @Param("to") Instant to,
            @Param("limit") int limit,
            @Param("offset") int offset
    );
}
