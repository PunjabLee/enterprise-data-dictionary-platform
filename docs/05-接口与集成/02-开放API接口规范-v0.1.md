# 开放 API 接口规范 v0.1

## 1. 设计原则

- API 采用 REST 风格。
- 请求和响应使用 JSON。
- 所有接口必须鉴权。
- 所有写操作必须记录审计日志。
- 批量导入、大规模影响分析采用异步任务。

## 2. 通用响应

```json
{
  "code": "0",
  "message": "success",
  "data": {},
  "traceId": "string"
}
```

## 3. 分页响应

```json
{
  "code": "0",
  "message": "success",
  "data": {
    "items": [],
    "page": 1,
    "pageSize": 20,
    "total": 0
  },
  "traceId": "string"
}
```

## 4. 资产接口

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | `/api/assets` | 查询资产列表 |
| POST | `/api/assets` | 创建资产 |
| GET | `/api/assets/{id}` | 查询资产详情 |
| PUT | `/api/assets/{id}` | 修改资产 |
| POST | `/api/assets/{id}/submit` | 提交确认 |
| POST | `/api/assets/{id}/publish` | 发布资产 |
| GET | `/api/assets/{id}/versions` | 查询版本 |
| GET | `/api/assets/{id}/relations` | 查询关系 |

## 5. 数据字典接口

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | `/api/dictionary/tables` | 查询表字典 |
| GET | `/api/dictionary/fields` | 查询字段字典 |
| PUT | `/api/dictionary/fields/{id}` | 更新字段业务定义 |
| POST | `/api/dictionary/fields/{id}/standard-reference` | 引用数据标准 |
| GET | `/api/dictionary/fields/{id}/diff` | 查看采集差异 |

## 6. 标准术语接口

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | `/api/standards` | 查询数据标准 |
| POST | `/api/standards` | 创建数据标准 |
| POST | `/api/standards/{id}/submit` | 提交评审 |
| POST | `/api/standards/{id}/publish` | 发布标准 |
| GET | `/api/terms` | 查询业务术语 |
| POST | `/api/terms` | 创建业务术语 |

## 7. 血缘与影响分析接口

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | `/api/lineage/graph` | 查询血缘图 |
| POST | `/api/lineage/relations` | 创建血缘关系 |
| POST | `/api/lineage/relations/{id}/confirm` | 确认血缘 |
| POST | `/api/impact-analysis` | 发起影响分析 |
| GET | `/api/impact-analysis/{taskId}` | 查询影响分析结果 |

## 8. 流程接口

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| POST | `/api/workflows/architecture-review` | 发起架构评审 |
| POST | `/api/workflows/model-change` | 发起模型变更 |
| GET | `/api/workflows/tasks` | 查询我的待办 |
| POST | `/api/workflows/tasks/{taskId}/approve` | 审批通过 |
| POST | `/api/workflows/tasks/{taskId}/reject` | 驳回 |
| POST | `/api/workflows/tasks/{taskId}/transfer` | 转办 |

## 9. 导入导出接口

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| POST | `/api/imports` | 上传导入文件 |
| GET | `/api/imports/{batchId}` | 查询导入批次 |
| GET | `/api/imports/{batchId}/errors` | 查询错误明细 |
| GET | `/api/templates/{templateType}` | 下载模板 |
| POST | `/api/exports/assets` | 导出资产 |

## 10. 错误码

| 错误码 | 说明 |
| --- | --- |
| AUTH_401 | 未登录或 Token 无效 |
| AUTH_403 | 无权限 |
| VALIDATION_400 | 参数校验失败 |
| ASSET_404 | 资产不存在 |
| WORKFLOW_409 | 当前状态不允许操作 |
| IMPORT_422 | 导入文件校验失败 |
| SYSTEM_500 | 系统异常 |

