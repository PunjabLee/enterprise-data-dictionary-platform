# 技术决策 ADR v0.1

## 1. 目的

本文档记录编码前必须冻结的关键技术决策，作为后续工程骨架、数据库迁移、API 设计、测试和部署的依据。

## 2. 决策清单

| ADR | 决策 | 结论 | 状态 |
| --- | --- | --- | --- |
| ADR-001 | 后端技术栈 | Java 21 + Spring Boot 3 + Maven | 已采纳 |
| ADR-002 | 前端技术栈 | Vue 3 + TypeScript + Vite + Ant Design Vue | 已采纳 |
| ADR-003 | 架构形态 | DDD 分层的模块化单体 | 已采纳 |
| ADR-004 | 数据访问 | Repository -> DAO -> MyBatis Mapper，禁止 Service 直连 Mapper | 已采纳 |
| ADR-005 | 主数据库 | PostgreSQL 16 | 已采纳 |
| ADR-006 | 数据库迁移 | Flyway 管理所有 DDL | 已采纳 |
| ADR-007 | 缓存 | Redis 纳入首期 | 已采纳 |
| ADR-008 | 搜索 | OpenSearch 预留，首期不强依赖 | 已采纳 |
| ADR-009 | 工作流 | 首期轻量审批任务模型，预留 Flowable | 已采纳 |
| ADR-010 | 消息 | 领域事件抽象，RocketMQ/Kafka 可插拔适配 | 已采纳 |
| ADR-011 | 规则引擎 | 首期轻量规则配置，预留 Drools/QLExpress/Aviator | 已采纳 |
| ADR-012 | 多租户 | 表结构预留 `tenant_id`，首期 `default` 单租户运行 | 已采纳 |
| ADR-013 | 权限 | RBAC + 数据范围 + 敏感字段控制 | 已采纳 |
| ADR-014 | 认证 | 首期本地账号/JWT，预留 OIDC/LDAP/AD | 已采纳 |

## 3. ADR-001 后端技术栈

采用：

```text
Java 21
Spring Boot 3
Maven
MyBatis-Plus
Flyway
```

理由：

- Maven 在企业 Java 项目中接受度高，适合私服、审计、安全扫描和 CI/CD。
- Spring Boot 3 生态成熟，适合企业级后台、权限、任务、集成和监控。
- Java 21 是长期支持版本，适合长期维护。

## 4. ADR-002 前端技术栈

采用：

```text
Vue 3
TypeScript
Vite
Ant Design Vue
Pinia
Vue Router
```

选择 Ant Design Vue 的理由：

- 更适合复杂中后台、信息密集型页面和治理平台。
- 表格、筛选、描述列表、抽屉、步骤流、复杂表单能力成熟。
- 更适合资产目录、血缘分析、评审流程、治理看板等复杂页面。

约束：

- 不混用 Element Plus。
- 前端按领域模块组织，不建全局巨型 `api.ts`。

## 5. ADR-003 模块化单体

模块化单体定义：

```text
一个部署单元
多个清晰业务模块
模块内 DDD 分层
模块间通过应用服务、领域事件或端口协作
共享数据库实例但禁止随意跨模块访问表
```

理由：

- 首期业务边界仍需演进，过早微服务会放大复杂度。
- 统一事务、部署、调试和测试更简单。
- 后续可按限界上下文拆分服务。

## 6. ADR-004 数据访问分层

硬性约束：

```text
Controller -> ApplicationService -> Repository -> DAO -> MyBatis Mapper
```

禁止：

- Application Service 直接访问 MyBatis Mapper。
- Domain Service 访问 MyBatis Mapper。
- Controller 访问 DAO、Mapper、RepositoryImpl。

理由：

- 隔离业务与持久化实现。
- 保持 DDD 依赖方向。
- 便于测试、缓存、审计、数据权限和后续替换持久化实现。

## 7. ADR-008 OpenSearch 策略

首期不强依赖 OpenSearch。

首期实现：

- PostgreSQL 索引和基础模糊搜索。
- `SearchGateway` 抽象。
- 搜索文档模型预留。

启用条件：

- 资产、字段、术语规模明显增长。
- PostgreSQL 搜索性能不足。
- 需要中文分词、相关性排序、高亮和高级全文搜索。

## 8. ADR-009 工作流策略

首期不强依赖 Flowable。

首期实现：

- 审批单。
- 审批任务。
- 审批节点。
- 审批意见。
- 状态机。

预留：

- `WorkflowEngine`。
- `WorkflowDefinition`。
- `WorkflowInstance`。
- `WorkflowTask`。

## 9. ADR-010 消息策略

业务代码只依赖：

```text
DomainEventPublisher
```

首期：

- 本地事件总线。
- 关键事件可落库。

后续：

- RocketMQ 适合企业治理业务事件。
- Kafka 适合数据平台元数据变更流。

## 10. ADR-012 多租户策略

首期不启用 SaaS 多租户，但所有核心业务表预留：

```text
tenant_id varchar(64) default 'default'
```

理由：

- 当前平台主要面向企业内部。
- 仍需支持未来集团化、多法人、多组织或外部化平台演进。

