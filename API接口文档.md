# 智慧煤矿ERP管理系统 API接口文档

## 基础信息

- **基础URL**: `http://localhost:8080`
- **数据格式**: JSON
- **认证方式**: JWT Token（登录后获取，在请求头中携带：`Authorization: Bearer {token}`）

## 通用响应格式

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {}
}
```

---

## 一、认证模块

### 1.1 用户登录
- **接口**: `POST /api/auth/login`
- **权限**: 无需认证
- **请求参数**:
```json
{
  "username": "admin",
  "password": "admin123"
}
```
- **响应示例**:
```json
{
  "code": 200,
  "msg": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "userInfo": {
      "userId": 1,
      "username": "admin",
      "nickName": "管理员"
    },
    "permissions": ["system:user:list", "asset:archive:add", ...]
  }
}
```

### 1.2 用户登出
- **接口**: `POST /api/auth/logout`
- **权限**: 需要认证

---

## 二、系统管理模块

### 2.1 用户管理

| 接口 | 方法 | 说明 | 权限 |
|------|------|------|------|
| `/api/system/user/page` | GET | 分页查询用户 | `system:user:list` |
| `/api/system/user` | POST | 新增用户 | `system:user:add` |
| `/api/system/user` | PUT | 更新用户 | `system:user:edit` |
| `/api/system/user/{id}` | DELETE | 删除用户 | `system:user:remove` |
| `/api/system/user/change-password` | POST | 修改密码 | - |
| `/api/system/user/{userId}/roles` | GET | 获取用户角色 | - |
| `/api/system/user/{userId}/roles` | POST | 分配用户角色 | `system:user:edit` |

**请求示例（分页查询）**:
```
GET /api/system/user/page?current=1&size=10&username=admin
```

### 2.2 角色管理

| 接口 | 方法 | 说明 | 权限 |
|------|------|------|------|
| `/api/system/role/page` | GET | 分页查询角色 | `system:role:list` |
| `/api/system/role` | POST | 新增角色 | `system:role:add` |
| `/api/system/role` | PUT | 更新角色 | `system:role:edit` |
| `/api/system/role/{id}` | DELETE | 删除角色 | `system:role:remove` |

### 2.3 菜单管理

| 接口 | 方法 | 说明 | 权限 |
|------|------|------|------|
| `/api/system/menu/list` | GET | 获取菜单列表 | `system:menu:list` |
| `/api/system/menu` | POST | 新增菜单 | `system:menu:add` |
| `/api/system/menu` | PUT | 更新菜单 | `system:menu:edit` |
| `/api/system/menu/{id}` | DELETE | 删除菜单 | `system:menu:remove` |

---

## 三、资产中心模块

### 3.1 资产管理

| 接口 | 方法 | 说明 | 权限 |
|------|------|------|------|
| `/api/asset/page` | GET | 分页查询资产 | `asset:archive:list` |
| `/api/asset` | POST | 新增资产 | `asset:archive:add` |
| `/api/asset` | PUT | 更新资产 | `asset:archive:edit` |
| `/api/asset/{id}` | DELETE | 删除资产 | `asset:archive:remove` |
| `/api/asset/{id}` | GET | 查询资产详情 | - |
| `/api/asset/types` | GET | 获取资产类型列表 | - |
| `/api/asset/manufacturers` | GET | 获取制造商列表 | - |

**请求示例（新增资产）**:
```json
POST /api/asset
{
  "assetName": "挖掘机",
  "assetCode": "WJ001",
  "assetType": "机械设备",
  "manufacturer": "三一重工",
  "model": "SY235C",
  "status": "0"
}
```

### 3.2 资产入库

| 接口 | 方法 | 说明 | 权限 |
|------|------|------|------|
| `/api/asset/storage` | POST | 创建入库单 | `asset:storage:add` |
| `/api/asset/storage/page` | GET | 分页查询入库单 | `asset:storage:list` |
| `/api/asset/storage/{id}/confirm` | POST | 确认入库 | `asset:storage:confirm` |
| `/api/asset/storage/{id}` | GET | 查询入库单详情 | - |

---

## 四、采购中心模块

### 4.1 采购申请

| 接口 | 方法 | 说明 | 权限 |
|------|------|------|------|
| `/api/purchase/requisition` | POST | 创建采购申请 | `purchase:requisition:add` |
| `/api/purchase/requisition/page` | GET | 分页查询申请 | `purchase:requisition:list` |
| `/api/purchase/requisition/{id}/submit` | POST | 提交审批 | `purchase:requisition:submit` |
| `/api/purchase/requisition/{id}/approve` | POST | 审批通过 | `purchase:requisition:approve` |
| `/api/purchase/requisition/{id}/reject` | POST | 审批驳回 | `purchase:requisition:approve` |
| `/api/purchase/requisition/{id}/details` | GET | 获取申请明细 | `purchase:requisition:list` |

### 4.2 采购订单

| 接口 | 方法 | 说明 | 权限 |
|------|------|------|------|
| `/api/purchase/order` | POST | 创建采购订单 | `purchase:order:add` |
| `/api/purchase/order/page` | GET | 分页查询订单 | `purchase:order:list` |
| `/api/purchase/order/{id}/submit` | POST | 提交审批 | `purchase:order:submit` |
| `/api/purchase/order/{id}/approve` | POST | 审批通过 | `purchase:order:approve` |
| `/api/purchase/order/{id}/confirm` | POST | 确认订单 | `purchase:order:confirm` |
| `/api/purchase/order/{id}/details` | GET | 获取订单明细 | `purchase:order:list` |

### 4.3 采购收货

| 接口 | 方法 | 说明 | 权限 |
|------|------|------|------|
| `/api/purchase/receiving` | POST | 创建收货单 | `purchase:receiving:add` |
| `/api/purchase/receiving/from-order/{orderId}` | POST | 从订单创建收货单 | `purchase:receiving:add` |
| `/api/purchase/receiving/page` | GET | 分页查询收货单 | `purchase:receiving:list` |
| `/api/purchase/receiving/{id}/confirm` | POST | 确认收货 | `purchase:receiving:confirm` |

### 4.4 供应商管理

| 接口 | 方法 | 说明 | 权限 |
|------|------|------|------|
| `/api/purchase/supplier/page` | GET | 分页查询供应商 | `purchase:supplier:list` |
| `/api/purchase/supplier` | POST | 新增供应商 | `purchase:supplier:add` |
| `/api/purchase/supplier` | PUT | 更新供应商 | `purchase:supplier:edit` |
| `/api/purchase/supplier/{id}` | DELETE | 删除供应商 | `purchase:supplier:remove` |
| `/api/purchase/supplier/{id}/evaluate` | POST | 评价供应商 | `purchase:supplier:evaluate` |

### 4.5 采购合同

| 接口 | 方法 | 说明 | 权限 |
|------|------|------|------|
| `/api/purchase/contract` | POST | 创建采购合同 | `purchase:contract:add` |
| `/api/purchase/contract/from-order/{orderId}` | POST | 从订单创建合同 | `purchase:contract:add` |
| `/api/purchase/contract/page` | GET | 分页查询合同 | `purchase:contract:list` |
| `/api/purchase/contract/{id}/submit` | POST | 提交审批 | `purchase:contract:submit` |
| `/api/purchase/contract/{id}/approve` | POST | 审批通过 | `purchase:contract:approve` |
| `/api/purchase/contract/{id}/sign` | POST | 签订合同 | `purchase:contract:sign` |

---

## 五、维修管理模块

### 5.1 维修工单

| 接口 | 方法 | 说明 | 权限 |
|------|------|------|------|
| `/api/maintenance/work-order` | POST | 创建维修工单 | `maintenance:workorder:add` |
| `/api/maintenance/work-order/page` | GET | 分页查询工单 | `maintenance:workorder:list` |
| `/api/maintenance/work-order/{id}` | GET | 查询工单详情 | - |
| `/api/maintenance/work-order/{id}/assign` | POST | 分配工单 | `maintenance:workorder:assign` |
| `/api/maintenance/work-order/{id}/start` | POST | 开始维修 | `maintenance:workorder:start` |
| `/api/maintenance/work-order/{id}/complete` | POST | 完成工单 | `maintenance:workorder:complete` |
| `/api/maintenance/work-order/{id}/details` | GET | 获取工单明细 | - |

**请求示例（创建工单）**:
```json
POST /api/maintenance/work-order
{
  "workOrder": {
    "workOrderType": "REPAIR",
    "priority": "HIGH",
    "assetId": 1,
    "faultType": "机械故障",
    "faultDescription": "发动机异常"
  },
  "details": [
    {
      "stepName": "检查发动机",
      "stepDescription": "检查发动机运行状态"
    }
  ]
}
```

### 5.2 维护计划

| 接口 | 方法 | 说明 | 权限 |
|------|------|------|------|
| `/api/maintenance/plan` | POST | 创建维护计划 | `maintenance:plan:add` |
| `/api/maintenance/plan/page` | GET | 分页查询计划 | `maintenance:plan:list` |
| `/api/maintenance/plan/{id}` | GET | 查询计划详情 | - |
| `/api/maintenance/plan/{id}/execute` | POST | 执行计划 | `maintenance:plan:execute` |
| `/api/maintenance/plan/{id}/executions` | GET | 获取执行记录 | - |

---

## 六、库存中心模块

### 6.1 库存物品

| 接口 | 方法 | 说明 | 权限 |
|------|------|------|------|
| `/api/inventory/material` | POST | 创建物品 | `inventory:material:add` |
| `/api/inventory/material/page` | GET | 分页查询物品 | `inventory:material:list` |
| `/api/inventory/material/{id}` | GET | 查询物品详情 | - |
| `/api/inventory/material` | PUT | 更新物品 | `inventory:material:edit` |
| `/api/inventory/material/{id}` | DELETE | 删除物品 | `inventory:material:remove` |

### 6.2 仓库管理

| 接口 | 方法 | 说明 | 权限 |
|------|------|------|------|
| `/api/inventory/warehouse` | POST | 创建仓库 | `inventory:warehouse:add` |
| `/api/inventory/warehouse/page` | GET | 分页查询仓库 | `inventory:warehouse:list` |
| `/api/inventory/warehouse/{id}` | GET | 查询仓库详情 | - |
| `/api/inventory/warehouse` | PUT | 更新仓库 | `inventory:warehouse:edit` |
| `/api/inventory/warehouse/{id}` | DELETE | 删除仓库 | `inventory:warehouse:remove` |

### 6.3 入库管理

| 接口 | 方法 | 说明 | 权限 |
|------|------|------|------|
| `/api/inventory/inbound` | POST | 创建入库单 | `inventory:inbound:add` |
| `/api/inventory/inbound/page` | GET | 分页查询入库单 | `inventory:inbound:list` |
| `/api/inventory/inbound/{id}` | GET | 查询入库单详情 | - |
| `/api/inventory/inbound/{id}/submit` | POST | 提交入库 | `inventory:inbound:submit` |
| `/api/inventory/inbound/{id}/approve` | POST | 审批入库 | `inventory:inbound:approve` |
| `/api/inventory/inbound/{id}/receive` | POST | 确认收货 | `inventory:inbound:receive` |

### 6.4 出库管理

| 接口 | 方法 | 说明 | 权限 |
|------|------|------|------|
| `/api/inventory/outbound` | POST | 创建出库单 | `inventory:outbound:add` |
| `/api/inventory/outbound/page` | GET | 分页查询出库单 | `inventory:outbound:list` |
| `/api/inventory/outbound/{id}` | GET | 查询出库单详情 | - |
| `/api/inventory/outbound/{id}/submit` | POST | 提交出库 | `inventory:outbound:submit` |
| `/api/inventory/outbound/{id}/approve` | POST | 审批出库 | `inventory:outbound:approve` |
| `/api/inventory/outbound/{id}/issue` | POST | 确认出库 | `inventory:outbound:issue` |

---

## 七、预警中心模块

### 7.1 预警规则

| 接口 | 方法 | 说明 | 权限 |
|------|------|------|------|
| `/api/warning/rule` | POST | 创建预警规则 | `warning:rule:add` |
| `/api/warning/rule/page` | GET | 分页查询规则 | `warning:rule:list` |
| `/api/warning/rule/{id}` | GET | 查询规则详情 | - |
| `/api/warning/rule` | PUT | 更新规则 | `warning:rule:edit` |
| `/api/warning/rule/{id}` | DELETE | 删除规则 | `warning:rule:remove` |
| `/api/warning/rule/{id}/enable` | PUT | 启用/禁用规则 | `warning:rule:enable` |

### 7.2 预警记录

| 接口 | 方法 | 说明 | 权限 |
|------|------|------|------|
| `/api/warning/monitor/page` | GET | 分页查询预警记录 | `warning:monitor:list` |
| `/api/warning/monitor/{id}` | GET | 查询预警详情 | - |
| `/api/warning/monitor` | POST | 创建预警记录 | `warning:monitor:add` |
| `/api/warning/monitor/{id}/handle` | POST | 处理预警 | `warning:monitor:handle` |
| `/api/warning/monitor/{id}/ignore` | POST | 忽略预警 | `warning:monitor:ignore` |
| `/api/warning/monitor/{id}/close` | POST | 关闭预警 | `warning:monitor:close` |

**请求示例（处理预警）**:
```json
POST /api/warning/monitor/{id}/handle
{
  "handleResult": "已修复故障",
  "handlerId": 1,
  "handlerName": "张三"
}
```

### 7.3 预警看板

| 接口 | 方法 | 说明 | 权限 |
|------|------|------|------|
| `/api/warning/dashboard/summary` | GET | 获取预警统计 | `warning:dashboard:view` |
| `/api/warning/dashboard/recent` | GET | 获取最近预警 | `warning:dashboard:view` |

---

## 八、仪表盘模块

### 8.1 统计数据

| 接口 | 方法 | 说明 | 权限 |
|------|------|------|------|
| `/api/dashboard/stats` | GET | 获取统计数据 | - |
| `/api/dashboard/asset-status` | GET | 获取设备状态分布 | - |
| `/api/dashboard/repair-trend` | GET | 获取维修趋势 | - |

**响应示例（统计数据）**:
```json
{
  "code": 200,
  "data": {
    "totalAssets": 100,
    "repairingAssets": 5,
    "inventoryAlerts": 3,
    "explosionProofWarnings": 2
  }
}
```

---

## 九、通用说明

### 9.1 分页参数

所有分页接口通用参数：
- `current`: 当前页码（默认：1）
- `size`: 每页数量（默认：10）

### 9.2 状态码说明

| 状态码 | 说明 |
|--------|------|
| 200 | 操作成功 |
| 401 | 未认证或Token过期 |
| 403 | 无权限访问 |
| 500 | 服务器错误 |

### 9.3 权限说明

- 所有接口（除登录接口）都需要在请求头中携带Token
- 权限标识格式：`模块:功能:操作`，如 `asset:archive:add`
- 超级管理员拥有所有权限
- 模块负责人拥有本模块全部权限，其他模块只读权限

### 9.4 请求头示例

```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json
```

---

## 十、快速开始

1. **登录获取Token**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

2. **使用Token访问接口**
```bash
curl -X GET http://localhost:8080/api/asset/page?current=1&size=10 \
  -H "Authorization: Bearer {your_token}"
```

---

**文档版本**: v1.0  
**最后更新**: 2025年

