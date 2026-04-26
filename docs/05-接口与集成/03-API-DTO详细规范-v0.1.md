# API DTO 详细规范 v0.1

## 1. 设计目标

本文档定义前后端并行开发所需的 API 契约，包括统一响应、分页、筛选、错误、权限、异步任务和核心 DTO。

## 2. 通用响应

```json
{
  "code": "0",
  "message": "success",
  "data": {},
  "traceId": "018f9b7a"
}
```

## 3. 通用分页请求

```json
{
  "page": 1,
  "pageSize": 20,
  "keyword": "customer",
  "sorts": [
    {
      "field": "updatedAt",
      "direction": "desc"
    }
  ]
}
```

## 4. 通用分页响应

```json
{
  "items": [],
  "page": 1,
  "pageSize": 20,
  "total": 100
}
```

## 5. 资产 DTO

### 5.1 AssetCreateCommand

```json
{
  "assetType": "table",
  "code": "crm.customer",
  "name": "customer",
  "displayName": "客户表",
  "description": "客户基础信息",
  "businessDomain": "customer",
  "systemCode": "crm",
  "ownerUserId": 1001,
  "stewardUserId": 1002,
  "attributes": {
    "schema": "public",
    "database": "crm_db"
  }
}
```

### 5.2 AssetDTO

```json
{
  "id": 1,
  "tenantId": "default",
  "assetType": "table",
  "code": "crm.customer",
  "name": "customer",
  "displayName": "客户表",
  "description": "客户基础信息",
  "businessDomain": "customer",
  "systemCode": "crm",
  "status": "published",
  "version": 1,
  "owner": {
    "id": 1001,
    "name": "张三"
  },
  "steward": {
    "id": 1002,
    "name": "李四"
  },
  "createdAt": "2026-04-26T10:00:00+08:00",
  "updatedAt": "2026-04-26T10:00:00+08:00"
}
```

## 6. 字段字典 DTO

### 6.1 FieldDictionaryUpdateCommand

```json
{
  "displayName": "客户编号",
  "businessDefinition": "企业内部客户唯一标识",
  "businessRule": "由 CRM 系统生成，不允许人工修改",
  "isKeyField": true,
  "isSensitive": false,
  "classificationLevel": "internal",
  "standardIds": [10],
  "businessTermIds": [20],
  "ownerUserId": 1001,
  "stewardUserId": 1002
}
```

## 7. 业务术语 DTO

### 7.1 BusinessTermCreateCommand

```json
{
  "termCode": "TERM_CUSTOMER",
  "termName": "客户",
  "englishName": "Customer",
  "abbreviation": "CUST",
  "aliases": ["客户主体"],
  "definition": "与企业发生业务关系的个人或组织",
  "scope": "适用于客户管理、订单、服务等场景",
  "businessDomain": "customer",
  "departmentId": 1,
  "ownerId": 1001,
  "stewardId": 1002
}
```

### 7.2 TermReferenceCommand

```json
{
  "termId": 20,
  "assetId": 1,
  "assetType": "field",
  "referenceType": "field_semantic"
}
```

## 8. 血缘 DTO

### 8.1 LineageRelationCreateCommand

```json
{
  "fromAssetId": 1,
  "toAssetId": 2,
  "relationType": "transform",
  "lineageLevel": "table",
  "sourceType": "manual",
  "confidence": "high",
  "description": "客户基础表加工生成客户宽表"
}
```

### 8.2 ImpactAnalysisCommand

```json
{
  "assetId": 1,
  "changeType": "schema_change",
  "direction": "downstream",
  "maxDepth": 5,
  "confirmedOnly": true
}
```

### 8.3 AsyncTaskDTO

```json
{
  "taskId": "task-20260426-0001",
  "taskType": "impact_analysis",
  "status": "pending",
  "createdAt": "2026-04-26T10:00:00+08:00"
}
```

## 9. 导入 DTO

### 9.1 ImportBatchDTO

```json
{
  "batchId": 1,
  "batchNo": "IMP-20260426-0001",
  "templateType": "asset_inventory",
  "fileName": "首批资产盘点模板.xlsx",
  "status": "partial",
  "totalRows": 1000,
  "successRows": 980,
  "failedRows": 20,
  "createdAt": "2026-04-26T10:00:00+08:00"
}
```

### 9.2 ImportErrorDTO

```json
{
  "rowNo": 12,
  "fieldName": "系统编码",
  "errorCode": "REQUIRED",
  "errorMessage": "系统编码不能为空"
}
```

## 10. 工作流 DTO

### 10.1 ApprovalCommand

```json
{
  "taskId": 10001,
  "action": "approve",
  "comment": "同意",
  "attachments": []
}
```

### 10.2 WorkflowTaskDTO

```json
{
  "taskId": 10001,
  "workflowType": "model_change",
  "title": "客户表字段变更审批",
  "status": "pending",
  "assigneeId": 1001,
  "createdAt": "2026-04-26T10:00:00+08:00",
  "dueDate": "2026-04-30T18:00:00+08:00"
}
```

## 11. 权限要求

| API 类型 | 权限 |
| --- | --- |
| 查询资产 | 菜单权限 + 数据范围权限 |
| 修改资产 | 操作权限 + 数据范围权限 |
| 发布资产 | 发布权限 |
| 导出资产 | 导出权限 + 审计 |
| 查看敏感字段 | 敏感字段权限 + 审计 |
| 审批任务 | 当前任务处理人或管理员 |

## 12. 错误码

| 错误码 | HTTP | 说明 |
| --- | --- | --- |
| AUTH_401 | 401 | 未登录 |
| AUTH_403 | 403 | 无权限 |
| VALIDATION_400 | 400 | 参数校验失败 |
| ASSET_404 | 404 | 资产不存在 |
| STATE_409 | 409 | 状态不允许操作 |
| IMPORT_422 | 422 | 导入校验失败 |
| TASK_404 | 404 | 任务不存在 |
| SYSTEM_500 | 500 | 系统异常 |

