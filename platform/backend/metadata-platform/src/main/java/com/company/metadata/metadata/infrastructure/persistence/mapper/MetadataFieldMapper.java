package com.company.metadata.metadata.infrastructure.persistence.mapper;

import com.company.metadata.metadata.infrastructure.persistence.record.MetadataFieldRecord;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 元数据字段 MyBatis Mapper，集中定义字段字典 SQL。
 *
 * 作者：Punjab
 */
@Mapper
public interface MetadataFieldMapper {

    @Insert("""
            INSERT INTO md_asset_field (
                tenant_id, asset_id, ordinal, field_name, display_name, data_type,
                length_value, precision_value, nullable, primary_key, key_field, sensitive,
                classification_level, business_definition, business_rule, standard_code, term_code,
                owner_user_id, steward_user_id, status, created_by, updated_by
            ) VALUES (
                #{tenantId}, #{assetId},
                COALESCE(#{ordinal}, (
                    SELECT COALESCE(MAX(ordinal), 0) + 1
                    FROM md_asset_field
                    WHERE tenant_id = #{tenantId}
                      AND asset_id = #{assetId}
                )),
                #{fieldName}, #{displayName}, #{dataType},
                #{lengthValue}, #{precisionValue}, #{nullable}, #{primaryKey}, #{keyField}, #{sensitive},
                #{classificationLevel}, #{businessDefinition}, #{businessRule}, #{standardCode}, #{termCode},
                #{ownerUserId}, #{stewardUserId}, #{status}, #{createdBy}, #{updatedBy}
            )
            ON CONFLICT (tenant_id, asset_id, field_name)
            DO UPDATE SET
                display_name = EXCLUDED.display_name,
                data_type = EXCLUDED.data_type,
                length_value = EXCLUDED.length_value,
                precision_value = EXCLUDED.precision_value,
                nullable = EXCLUDED.nullable,
                primary_key = EXCLUDED.primary_key,
                key_field = EXCLUDED.key_field,
                sensitive = EXCLUDED.sensitive,
                classification_level = EXCLUDED.classification_level,
                business_definition = EXCLUDED.business_definition,
                business_rule = EXCLUDED.business_rule,
                standard_code = EXCLUDED.standard_code,
                term_code = EXCLUDED.term_code,
                owner_user_id = EXCLUDED.owner_user_id,
                steward_user_id = EXCLUDED.steward_user_id,
                status = EXCLUDED.status,
                updated_by = EXCLUDED.updated_by,
                updated_at = now()
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void upsert(MetadataFieldRecord record);

    @Update("""
            UPDATE md_asset_field
            SET display_name = #{displayName},
                key_field = #{keyField},
                sensitive = #{sensitive},
                classification_level = #{classificationLevel},
                business_definition = #{businessDefinition},
                business_rule = #{businessRule},
                standard_code = #{standardCode},
                term_code = #{termCode},
                owner_user_id = #{ownerUserId},
                steward_user_id = #{stewardUserId},
                status = #{status},
                updated_by = #{updatedBy},
                updated_at = now()
            WHERE tenant_id = #{tenantId}
              AND id = #{id}
            """)
    void update(MetadataFieldRecord record);

    @Select("""
            SELECT id, tenant_id, asset_id, ordinal, field_name, display_name, data_type,
                   length_value, precision_value, nullable, primary_key, key_field, sensitive,
                   classification_level, business_definition, business_rule, standard_code, term_code,
                   owner_user_id, steward_user_id, status, created_by, updated_by, created_at, updated_at
            FROM md_asset_field
            WHERE tenant_id = #{tenantId}
              AND id = #{id}
            """)
    MetadataFieldRecord selectById(@Param("tenantId") String tenantId, @Param("id") Long id);

    @Select("""
            SELECT id, tenant_id, asset_id, ordinal, field_name, display_name, data_type,
                   length_value, precision_value, nullable, primary_key, key_field, sensitive,
                   classification_level, business_definition, business_rule, standard_code, term_code,
                   owner_user_id, steward_user_id, status, created_by, updated_by, created_at, updated_at
            FROM md_asset_field
            WHERE tenant_id = #{tenantId}
              AND asset_id = #{assetId}
            ORDER BY ordinal ASC, id ASC
            """)
    List<MetadataFieldRecord> selectByAssetId(
            @Param("tenantId") String tenantId,
            @Param("assetId") Long assetId
    );

    @Select("""
            <script>
            SELECT id, tenant_id, asset_id, ordinal, field_name, display_name, data_type,
                   length_value, precision_value, nullable, primary_key, key_field, sensitive,
                   classification_level, business_definition, business_rule, standard_code, term_code,
                   owner_user_id, steward_user_id, status, created_by, updated_by, created_at, updated_at
            FROM md_asset_field
            WHERE tenant_id = #{tenantId}
            <if test="keyword != null and keyword != ''">
              AND (
                field_name ILIKE CONCAT('%', #{keyword}, '%')
                OR display_name ILIKE CONCAT('%', #{keyword}, '%')
                OR business_definition ILIKE CONCAT('%', #{keyword}, '%')
              )
            </if>
            <if test="assetId != null">
              AND asset_id = #{assetId}
            </if>
            <if test="sensitive != null">
              AND sensitive = #{sensitive}
            </if>
            <if test="status != null and status != ''">
              AND status = #{status}
            </if>
            ORDER BY updated_at DESC, id DESC
            LIMIT #{limit} OFFSET #{offset}
            </script>
            """)
    List<MetadataFieldRecord> searchDictionary(
            @Param("tenantId") String tenantId,
            @Param("keyword") String keyword,
            @Param("assetId") Long assetId,
            @Param("sensitive") Boolean sensitive,
            @Param("status") String status,
            @Param("limit") int limit,
            @Param("offset") int offset
    );
}
