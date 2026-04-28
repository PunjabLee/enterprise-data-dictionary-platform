package com.company.metadata.metadata.infrastructure.persistence.mapper;

import com.company.metadata.metadata.infrastructure.persistence.record.MetadataAssetRecord;
import com.company.metadata.metadata.infrastructure.persistence.record.MetadataAssetVersionRecord;
import java.util.List;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 元数据资产 MyBatis Mapper，集中定义资产和版本 SQL。
 *
 * 作者：Punjab
 */
@Mapper
public interface MetadataAssetMapper {

    @Insert("""
            INSERT INTO md_asset (
                tenant_id, asset_type, code, name, display_name, description,
                business_domain, system_code, owner_user_id, steward_user_id,
                status, version, source_type, attributes_json, created_by, updated_by
            ) VALUES (
                #{tenantId}, #{assetType}, #{code}, #{name}, #{displayName}, #{description},
                #{businessDomain}, #{systemCode}, #{ownerUserId}, #{stewardUserId},
                #{status}, #{version}, #{sourceType}, #{attributesJson}, #{createdBy}, #{updatedBy}
            )
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insert(MetadataAssetRecord record);

    @Update("""
            UPDATE md_asset
            SET display_name = #{displayName},
                description = #{description},
                business_domain = #{businessDomain},
                system_code = #{systemCode},
                owner_user_id = #{ownerUserId},
                steward_user_id = #{stewardUserId},
                status = #{status},
                version = #{version},
                source_type = #{sourceType},
                attributes_json = #{attributesJson},
                updated_by = #{updatedBy},
                updated_at = now()
            WHERE tenant_id = #{tenantId}
              AND id = #{id}
            """)
    void update(MetadataAssetRecord record);

    @Select("""
            SELECT id, tenant_id, asset_type, code, name, display_name, description,
                   business_domain, system_code, owner_user_id, steward_user_id,
                   status, version, source_type, attributes_json, created_by, updated_by,
                   created_at, updated_at
            FROM md_asset
            WHERE tenant_id = #{tenantId}
              AND id = #{id}
            """)
    MetadataAssetRecord selectById(@Param("tenantId") String tenantId, @Param("id") Long id);

    @Select("""
            <script>
            SELECT id, tenant_id, asset_type, code, name, display_name, description,
                   business_domain, system_code, owner_user_id, steward_user_id,
                   status, version, source_type, attributes_json, created_by, updated_by,
                   created_at, updated_at
            FROM md_asset
            WHERE tenant_id = #{tenantId}
            <if test="keyword != null and keyword != ''">
              AND (
                code ILIKE CONCAT('%', #{keyword}, '%')
                OR name ILIKE CONCAT('%', #{keyword}, '%')
                OR display_name ILIKE CONCAT('%', #{keyword}, '%')
                OR description ILIKE CONCAT('%', #{keyword}, '%')
              )
            </if>
            <if test="assetType != null and assetType != ''">
              AND asset_type = #{assetType}
            </if>
            <if test="status != null and status != ''">
              AND status = #{status}
            </if>
            <if test="businessDomain != null and businessDomain != ''">
              AND business_domain = #{businessDomain}
            </if>
            <if test="systemCode != null and systemCode != ''">
              AND system_code = #{systemCode}
            </if>
            ORDER BY updated_at DESC, id DESC
            LIMIT #{limit} OFFSET #{offset}
            </script>
            """)
    List<MetadataAssetRecord> search(
            @Param("tenantId") String tenantId,
            @Param("keyword") String keyword,
            @Param("assetType") String assetType,
            @Param("status") String status,
            @Param("businessDomain") String businessDomain,
            @Param("systemCode") String systemCode,
            @Param("limit") int limit,
            @Param("offset") int offset
    );

    @Insert("""
            INSERT INTO md_asset_version (
                tenant_id, asset_id, version_number, status, snapshot_json, published_by, comment
            ) VALUES (
                #{tenantId}, #{assetId}, #{versionNumber}, #{status}, #{snapshotJson}, #{publishedBy}, #{comment}
            )
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insertVersion(MetadataAssetVersionRecord record);

    @Select("""
            SELECT id, tenant_id, asset_id, version_number, status, snapshot_json,
                   published_by, published_at, comment
            FROM md_asset_version
            WHERE tenant_id = #{tenantId}
              AND asset_id = #{assetId}
            ORDER BY version_number DESC
            """)
    List<MetadataAssetVersionRecord> selectVersions(
            @Param("tenantId") String tenantId,
            @Param("assetId") Long assetId
    );
}
