-- 脚本说明：创建系统管理、安全权限、数据范围和审计日志基础表。
-- 作者：Punjab

CREATE TABLE sys_organization (
    id BIGSERIAL PRIMARY KEY,
    tenant_id VARCHAR(64) NOT NULL DEFAULT 'default',
    parent_id BIGINT REFERENCES sys_organization (id),
    code VARCHAR(128) NOT NULL,
    name VARCHAR(255) NOT NULL,
    status VARCHAR(32) NOT NULL DEFAULT 'active',
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT uk_sys_organization_code UNIQUE (tenant_id, code)
);

CREATE TABLE sys_user (
    id BIGSERIAL PRIMARY KEY,
    tenant_id VARCHAR(64) NOT NULL DEFAULT 'default',
    organization_id BIGINT REFERENCES sys_organization (id),
    username VARCHAR(128) NOT NULL,
    display_name VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    mobile VARCHAR(64),
    status VARCHAR(32) NOT NULL DEFAULT 'active',
    source VARCHAR(32) NOT NULL DEFAULT 'local',
    external_id VARCHAR(255),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT uk_sys_user_username UNIQUE (tenant_id, username)
);

CREATE INDEX idx_sys_user_org ON sys_user (tenant_id, organization_id);
CREATE INDEX idx_sys_user_status ON sys_user (tenant_id, status);

CREATE TABLE sys_role (
    id BIGSERIAL PRIMARY KEY,
    tenant_id VARCHAR(64) NOT NULL DEFAULT 'default',
    code VARCHAR(128) NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    built_in BOOLEAN NOT NULL DEFAULT false,
    enabled BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT uk_sys_role_code UNIQUE (tenant_id, code)
);

CREATE TABLE sys_permission (
    id BIGSERIAL PRIMARY KEY,
    tenant_id VARCHAR(64) NOT NULL DEFAULT 'default',
    code VARCHAR(128) NOT NULL,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(64) NOT NULL,
    resource VARCHAR(128) NOT NULL,
    action VARCHAR(64) NOT NULL,
    description TEXT,
    enabled BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT uk_sys_permission_code UNIQUE (tenant_id, code)
);

CREATE TABLE sys_user_role (
    tenant_id VARCHAR(64) NOT NULL DEFAULT 'default',
    user_id BIGINT NOT NULL REFERENCES sys_user (id),
    role_id BIGINT NOT NULL REFERENCES sys_role (id),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    PRIMARY KEY (tenant_id, user_id, role_id)
);

CREATE INDEX idx_sys_user_role_role ON sys_user_role (tenant_id, role_id);

CREATE TABLE sys_role_permission (
    tenant_id VARCHAR(64) NOT NULL DEFAULT 'default',
    role_id BIGINT NOT NULL REFERENCES sys_role (id),
    permission_id BIGINT NOT NULL REFERENCES sys_permission (id),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    PRIMARY KEY (tenant_id, role_id, permission_id)
);

CREATE INDEX idx_sys_role_permission_permission ON sys_role_permission (tenant_id, permission_id);

CREATE TABLE sys_data_scope (
    id BIGSERIAL PRIMARY KEY,
    tenant_id VARCHAR(64) NOT NULL DEFAULT 'default',
    subject_type VARCHAR(32) NOT NULL,
    subject_id BIGINT NOT NULL,
    scope_type VARCHAR(64) NOT NULL,
    scope_value VARCHAR(255) NOT NULL DEFAULT '*',
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT uk_sys_data_scope UNIQUE (tenant_id, subject_type, subject_id, scope_type, scope_value)
);

CREATE INDEX idx_sys_data_scope_subject ON sys_data_scope (tenant_id, subject_type, subject_id);

CREATE TABLE sys_audit_log (
    id BIGSERIAL PRIMARY KEY,
    tenant_id VARCHAR(64) NOT NULL DEFAULT 'default',
    actor_id BIGINT,
    actor_name VARCHAR(255),
    action VARCHAR(128) NOT NULL,
    object_type VARCHAR(128) NOT NULL,
    object_id VARCHAR(128),
    before_value JSONB,
    after_value JSONB,
    result VARCHAR(32) NOT NULL,
    ip VARCHAR(64),
    user_agent TEXT,
    trace_id VARCHAR(128),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_sys_audit_log_actor ON sys_audit_log (tenant_id, actor_id, created_at DESC);
CREATE INDEX idx_sys_audit_log_object ON sys_audit_log (tenant_id, object_type, object_id, created_at DESC);
CREATE INDEX idx_sys_audit_log_action ON sys_audit_log (tenant_id, action, created_at DESC);

INSERT INTO sys_organization (tenant_id, code, name, status)
VALUES ('default', 'root', '根组织', 'active');

INSERT INTO sys_role (tenant_id, code, name, description, built_in, enabled)
VALUES
    ('default', 'platform_admin', '平台管理员', '拥有平台系统管理全部权限。', true, true),
    ('default', 'security_auditor', '安全审计员', '只读审计与权限复核角色。', true, true);

INSERT INTO sys_permission (tenant_id, code, name, category, resource, action, description)
VALUES
    ('default', 'system:admin', '系统管理', 'operation', 'system', 'admin', '系统初始化和完整系统管理权限。'),
    ('default', 'system:organization:read', '读取组织', 'operation', 'organization', 'read', '查看组织信息。'),
    ('default', 'system:organization:create', '创建组织', 'operation', 'organization', 'create', '创建组织信息。'),
    ('default', 'system:organization:update', '更新组织', 'operation', 'organization', 'update', '更新组织信息。'),
    ('default', 'system:user:read', '读取用户', 'operation', 'user', 'read', '查看用户信息。'),
    ('default', 'system:user:create', '创建用户', 'operation', 'user', 'create', '创建用户信息。'),
    ('default', 'system:user:update', '更新用户', 'operation', 'user', 'update', '更新用户信息。'),
    ('default', 'system:role:read', '读取角色', 'operation', 'role', 'read', '查看角色信息。'),
    ('default', 'system:role:create', '创建角色', 'operation', 'role', 'create', '创建角色信息。'),
    ('default', 'system:role:update', '更新角色', 'operation', 'role', 'update', '更新角色信息。'),
    ('default', 'system:permission:read', '读取权限', 'operation', 'permission', 'read', '查看权限信息。'),
    ('default', 'system:permission:update', '更新权限', 'operation', 'permission', 'update', '更新角色权限关系。'),
    ('default', 'system:data-scope:read', '读取数据范围', 'operation', 'data_scope', 'read', '查看数据范围配置。'),
    ('default', 'system:data-scope:update', '更新数据范围', 'operation', 'data_scope', 'update', '授权数据范围。'),
    ('default', 'system:audit:read', '读取审计日志', 'operation', 'audit_log', 'read', '查看审计日志。');

INSERT INTO sys_role_permission (tenant_id, role_id, permission_id)
SELECT 'default', r.id, p.id
FROM sys_role r
CROSS JOIN sys_permission p
WHERE r.tenant_id = 'default'
  AND r.code = 'platform_admin'
  AND p.tenant_id = 'default';

INSERT INTO sys_role_permission (tenant_id, role_id, permission_id)
SELECT 'default', r.id, p.id
FROM sys_role r
JOIN sys_permission p ON p.tenant_id = r.tenant_id
WHERE r.tenant_id = 'default'
  AND r.code = 'security_auditor'
  AND p.code IN (
      'system:organization:read',
      'system:user:read',
      'system:role:read',
      'system:permission:read',
      'system:data-scope:read',
      'system:audit:read'
  );

INSERT INTO sys_data_scope (tenant_id, subject_type, subject_id, scope_type, scope_value)
SELECT 'default', 'ROLE', id, 'ALL', '*'
FROM sys_role
WHERE tenant_id = 'default'
  AND code = 'platform_admin';


COMMENT ON TABLE sys_organization IS '系统组织表，记录平台内组织结构。';
COMMENT ON COLUMN sys_organization.id IS '组织主键';
COMMENT ON COLUMN sys_organization.tenant_id IS '租户标识，默认保留为 default';
COMMENT ON COLUMN sys_organization.parent_id IS '上级组织主键';
COMMENT ON COLUMN sys_organization.code IS '组织编码';
COMMENT ON COLUMN sys_organization.name IS '组织名称';
COMMENT ON COLUMN sys_organization.status IS '组织状态';
COMMENT ON COLUMN sys_organization.created_at IS '创建时间';
COMMENT ON COLUMN sys_organization.updated_at IS '更新时间';

COMMENT ON TABLE sys_user IS '系统用户表，记录本地账号和外部账号映射。';
COMMENT ON COLUMN sys_user.id IS '用户主键';
COMMENT ON COLUMN sys_user.tenant_id IS '租户标识，默认保留为 default';
COMMENT ON COLUMN sys_user.organization_id IS '所属组织主键';
COMMENT ON COLUMN sys_user.username IS '登录用户名';
COMMENT ON COLUMN sys_user.display_name IS '用户显示名称';
COMMENT ON COLUMN sys_user.email IS '电子邮箱';
COMMENT ON COLUMN sys_user.mobile IS '手机号码';
COMMENT ON COLUMN sys_user.status IS '用户状态';
COMMENT ON COLUMN sys_user.source IS '用户来源';
COMMENT ON COLUMN sys_user.external_id IS '外部身份标识';
COMMENT ON COLUMN sys_user.created_at IS '创建时间';
COMMENT ON COLUMN sys_user.updated_at IS '更新时间';

COMMENT ON TABLE sys_role IS '系统角色表，记录权限集合。';
COMMENT ON COLUMN sys_role.id IS '角色主键';
COMMENT ON COLUMN sys_role.tenant_id IS '租户标识，默认保留为 default';
COMMENT ON COLUMN sys_role.code IS '角色编码';
COMMENT ON COLUMN sys_role.name IS '角色名称';
COMMENT ON COLUMN sys_role.description IS '角色说明';
COMMENT ON COLUMN sys_role.built_in IS '是否内置角色';
COMMENT ON COLUMN sys_role.enabled IS '是否启用';
COMMENT ON COLUMN sys_role.created_at IS '创建时间';
COMMENT ON COLUMN sys_role.updated_at IS '更新时间';

COMMENT ON TABLE sys_permission IS '系统权限表，记录可授权的操作能力。';
COMMENT ON COLUMN sys_permission.id IS '权限主键';
COMMENT ON COLUMN sys_permission.tenant_id IS '租户标识，默认保留为 default';
COMMENT ON COLUMN sys_permission.code IS '权限编码';
COMMENT ON COLUMN sys_permission.name IS '权限名称';
COMMENT ON COLUMN sys_permission.category IS '权限分类';
COMMENT ON COLUMN sys_permission.resource IS '资源标识';
COMMENT ON COLUMN sys_permission.action IS '操作类型';
COMMENT ON COLUMN sys_permission.description IS '权限说明';
COMMENT ON COLUMN sys_permission.enabled IS '是否启用';
COMMENT ON COLUMN sys_permission.created_at IS '创建时间';
COMMENT ON COLUMN sys_permission.updated_at IS '更新时间';

COMMENT ON TABLE sys_user_role IS '用户角色关系表。';
COMMENT ON COLUMN sys_user_role.tenant_id IS '租户标识，默认保留为 default';
COMMENT ON COLUMN sys_user_role.user_id IS '用户主键';
COMMENT ON COLUMN sys_user_role.role_id IS '角色主键';
COMMENT ON COLUMN sys_user_role.created_at IS '创建时间';

COMMENT ON TABLE sys_role_permission IS '角色权限关系表。';
COMMENT ON COLUMN sys_role_permission.tenant_id IS '租户标识，默认保留为 default';
COMMENT ON COLUMN sys_role_permission.role_id IS '角色主键';
COMMENT ON COLUMN sys_role_permission.permission_id IS '权限主键';
COMMENT ON COLUMN sys_role_permission.created_at IS '创建时间';

COMMENT ON TABLE sys_data_scope IS '数据范围授权表，记录角色或用户可访问的数据边界。';
COMMENT ON COLUMN sys_data_scope.id IS '数据范围主键';
COMMENT ON COLUMN sys_data_scope.tenant_id IS '租户标识，默认保留为 default';
COMMENT ON COLUMN sys_data_scope.subject_type IS '授权主体类型';
COMMENT ON COLUMN sys_data_scope.subject_id IS '授权主体主键';
COMMENT ON COLUMN sys_data_scope.scope_type IS '数据范围类型';
COMMENT ON COLUMN sys_data_scope.scope_value IS '数据范围取值';
COMMENT ON COLUMN sys_data_scope.created_at IS '创建时间';

COMMENT ON TABLE sys_audit_log IS '系统审计日志表，记录关键操作和结果。';
COMMENT ON COLUMN sys_audit_log.id IS '审计日志主键';
COMMENT ON COLUMN sys_audit_log.tenant_id IS '租户标识，默认保留为 default';
COMMENT ON COLUMN sys_audit_log.actor_id IS '操作者用户主键';
COMMENT ON COLUMN sys_audit_log.actor_name IS '操作者名称';
COMMENT ON COLUMN sys_audit_log.action IS '操作动作';
COMMENT ON COLUMN sys_audit_log.object_type IS '操作对象类型';
COMMENT ON COLUMN sys_audit_log.object_id IS '操作对象标识';
COMMENT ON COLUMN sys_audit_log.before_value IS '变更前内容';
COMMENT ON COLUMN sys_audit_log.after_value IS '变更后内容';
COMMENT ON COLUMN sys_audit_log.result IS '操作结果';
COMMENT ON COLUMN sys_audit_log.ip IS '来源 IP';
COMMENT ON COLUMN sys_audit_log.user_agent IS '客户端标识';
COMMENT ON COLUMN sys_audit_log.trace_id IS '链路追踪标识';
COMMENT ON COLUMN sys_audit_log.created_at IS '创建时间';
