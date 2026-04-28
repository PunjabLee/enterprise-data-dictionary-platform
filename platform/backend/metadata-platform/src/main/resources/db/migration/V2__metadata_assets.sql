CREATE TABLE md_asset (
    id BIGSERIAL PRIMARY KEY,
    tenant_id VARCHAR(64) NOT NULL DEFAULT 'default',
    asset_type VARCHAR(64) NOT NULL,
    code VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    display_name VARCHAR(255),
    description TEXT,
    business_domain VARCHAR(128),
    system_code VARCHAR(128),
    owner_user_id BIGINT,
    steward_user_id BIGINT,
    status VARCHAR(32) NOT NULL DEFAULT 'draft',
    version INTEGER NOT NULL DEFAULT 0,
    source_type VARCHAR(32) NOT NULL DEFAULT 'manual',
    attributes_json TEXT,
    created_by VARCHAR(128),
    updated_by VARCHAR(128),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT uk_md_asset_code UNIQUE (tenant_id, code)
);

CREATE INDEX idx_md_asset_type_status ON md_asset (tenant_id, asset_type, status);
CREATE INDEX idx_md_asset_domain ON md_asset (tenant_id, business_domain);
CREATE INDEX idx_md_asset_system ON md_asset (tenant_id, system_code);

CREATE TABLE md_asset_field (
    id BIGSERIAL PRIMARY KEY,
    tenant_id VARCHAR(64) NOT NULL DEFAULT 'default',
    asset_id BIGINT NOT NULL REFERENCES md_asset(id) ON DELETE CASCADE,
    ordinal INTEGER NOT NULL DEFAULT 0,
    field_name VARCHAR(255) NOT NULL,
    display_name VARCHAR(255),
    data_type VARCHAR(128),
    length_value INTEGER,
    precision_value INTEGER,
    nullable BOOLEAN NOT NULL DEFAULT true,
    primary_key BOOLEAN NOT NULL DEFAULT false,
    key_field BOOLEAN NOT NULL DEFAULT false,
    sensitive BOOLEAN NOT NULL DEFAULT false,
    classification_level VARCHAR(64),
    business_definition TEXT,
    business_rule TEXT,
    standard_code VARCHAR(128),
    term_code VARCHAR(128),
    owner_user_id BIGINT,
    steward_user_id BIGINT,
    status VARCHAR(32) NOT NULL DEFAULT 'active',
    created_by VARCHAR(128),
    updated_by VARCHAR(128),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT uk_md_asset_field_name UNIQUE (tenant_id, asset_id, field_name)
);

CREATE INDEX idx_md_asset_field_asset ON md_asset_field (tenant_id, asset_id, ordinal);
CREATE INDEX idx_md_asset_field_sensitive ON md_asset_field (tenant_id, sensitive);
CREATE INDEX idx_md_asset_field_standard ON md_asset_field (tenant_id, standard_code);
CREATE INDEX idx_md_asset_field_term ON md_asset_field (tenant_id, term_code);

CREATE TABLE md_asset_version (
    id BIGSERIAL PRIMARY KEY,
    tenant_id VARCHAR(64) NOT NULL DEFAULT 'default',
    asset_id BIGINT NOT NULL REFERENCES md_asset(id) ON DELETE CASCADE,
    version_number INTEGER NOT NULL,
    status VARCHAR(32) NOT NULL DEFAULT 'published',
    snapshot_json TEXT NOT NULL,
    published_by VARCHAR(128),
    published_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    comment TEXT,
    CONSTRAINT uk_md_asset_version UNIQUE (tenant_id, asset_id, version_number)
);

CREATE INDEX idx_md_asset_version_asset ON md_asset_version (tenant_id, asset_id, version_number DESC);

INSERT INTO sys_permission (tenant_id, code, name, category, resource, action, description)
VALUES
    ('default', 'metadata:asset:read', '读取资产', 'operation', 'metadata_asset', 'read', '查看资产目录和资产详情。'),
    ('default', 'metadata:asset:create', '创建资产', 'operation', 'metadata_asset', 'create', '创建资产目录对象。'),
    ('default', 'metadata:asset:update', '更新资产', 'operation', 'metadata_asset', 'update', '维护资产基础信息。'),
    ('default', 'metadata:asset:publish', '发布资产', 'operation', 'metadata_asset', 'publish', '发布资产并生成版本。'),
    ('default', 'metadata:field:read', '读取字段字典', 'operation', 'metadata_field', 'read', '查看字段字典和字段详情。'),
    ('default', 'metadata:field:update', '更新字段字典', 'operation', 'metadata_field', 'update', '维护字段业务定义和分类分级。')
ON CONFLICT (tenant_id, code) DO NOTHING;

INSERT INTO sys_role_permission (tenant_id, role_id, permission_id)
SELECT 'default', r.id, p.id
FROM sys_role r
JOIN sys_permission p ON p.tenant_id = r.tenant_id
WHERE r.tenant_id = 'default'
  AND r.code = 'platform_admin'
  AND p.code LIKE 'metadata:%'
ON CONFLICT DO NOTHING;

COMMENT ON TABLE md_asset IS '元数据资产表，统一记录系统、表、接口、报表等可治理资产。';
COMMENT ON COLUMN md_asset.id IS '资产主键';
COMMENT ON COLUMN md_asset.tenant_id IS '租户标识，默认保留为 default';
COMMENT ON COLUMN md_asset.asset_type IS '资产类型，例如 system、database、table、api、report';
COMMENT ON COLUMN md_asset.code IS '资产编码，租户内唯一';
COMMENT ON COLUMN md_asset.name IS '资产技术名称';
COMMENT ON COLUMN md_asset.display_name IS '资产展示名称或中文名称';
COMMENT ON COLUMN md_asset.description IS '资产业务说明';
COMMENT ON COLUMN md_asset.business_domain IS '所属业务域';
COMMENT ON COLUMN md_asset.system_code IS '所属系统编码';
COMMENT ON COLUMN md_asset.owner_user_id IS '数据 Owner 用户主键';
COMMENT ON COLUMN md_asset.steward_user_id IS '数据 Steward 用户主键';
COMMENT ON COLUMN md_asset.status IS '资产状态，支持 draft、published、deprecated 等';
COMMENT ON COLUMN md_asset.version IS '当前发布版本号';
COMMENT ON COLUMN md_asset.source_type IS '来源类型，例如 manual、import、collector、api';
COMMENT ON COLUMN md_asset.attributes_json IS '扩展属性 JSON 文本';
COMMENT ON COLUMN md_asset.created_by IS '创建人';
COMMENT ON COLUMN md_asset.updated_by IS '最后更新人';
COMMENT ON COLUMN md_asset.created_at IS '创建时间';
COMMENT ON COLUMN md_asset.updated_at IS '更新时间';

COMMENT ON TABLE md_asset_field IS '元数据字段表，记录资产字段的技术属性和业务字典。';
COMMENT ON COLUMN md_asset_field.id IS '字段主键';
COMMENT ON COLUMN md_asset_field.tenant_id IS '租户标识，默认保留为 default';
COMMENT ON COLUMN md_asset_field.asset_id IS '所属资产主键';
COMMENT ON COLUMN md_asset_field.ordinal IS '字段排序号';
COMMENT ON COLUMN md_asset_field.field_name IS '字段技术名称';
COMMENT ON COLUMN md_asset_field.display_name IS '字段展示名称或中文名称';
COMMENT ON COLUMN md_asset_field.data_type IS '字段数据类型';
COMMENT ON COLUMN md_asset_field.length_value IS '字段长度';
COMMENT ON COLUMN md_asset_field.precision_value IS '字段精度';
COMMENT ON COLUMN md_asset_field.nullable IS '是否允许为空';
COMMENT ON COLUMN md_asset_field.primary_key IS '是否主键字段';
COMMENT ON COLUMN md_asset_field.key_field IS '是否关键字段';
COMMENT ON COLUMN md_asset_field.sensitive IS '是否敏感字段';
COMMENT ON COLUMN md_asset_field.classification_level IS '分类分级等级';
COMMENT ON COLUMN md_asset_field.business_definition IS '字段业务定义';
COMMENT ON COLUMN md_asset_field.business_rule IS '字段业务规则';
COMMENT ON COLUMN md_asset_field.standard_code IS '数据标准编码预留引用';
COMMENT ON COLUMN md_asset_field.term_code IS '业务术语编码预留引用';
COMMENT ON COLUMN md_asset_field.owner_user_id IS '字段 Owner 用户主键';
COMMENT ON COLUMN md_asset_field.steward_user_id IS '字段 Steward 用户主键';
COMMENT ON COLUMN md_asset_field.status IS '字段状态';
COMMENT ON COLUMN md_asset_field.created_by IS '创建人';
COMMENT ON COLUMN md_asset_field.updated_by IS '最后更新人';
COMMENT ON COLUMN md_asset_field.created_at IS '创建时间';
COMMENT ON COLUMN md_asset_field.updated_at IS '更新时间';

COMMENT ON TABLE md_asset_version IS '资产版本表，保存资产发布时的快照。';
COMMENT ON COLUMN md_asset_version.id IS '资产版本主键';
COMMENT ON COLUMN md_asset_version.tenant_id IS '租户标识，默认保留为 default';
COMMENT ON COLUMN md_asset_version.asset_id IS '资产主键';
COMMENT ON COLUMN md_asset_version.version_number IS '版本号';
COMMENT ON COLUMN md_asset_version.status IS '版本状态';
COMMENT ON COLUMN md_asset_version.snapshot_json IS '发布快照 JSON 文本';
COMMENT ON COLUMN md_asset_version.published_by IS '发布人';
COMMENT ON COLUMN md_asset_version.published_at IS '发布时间';
COMMENT ON COLUMN md_asset_version.comment IS '发布备注';
