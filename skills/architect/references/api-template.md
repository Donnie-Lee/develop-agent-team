# 接口设计文档模板

## 文档信息

| 字段 | 内容 |
|------|------|
| 项目名称 | {project-name} |
| 文档版本 | v{version} |
| 创建日期 | {date} |
| 架构师 | {architect-name} |
| 状态 | {draft/review/approved} |

---

## 1. 接口规范

### 1.1 RESTful规范

| 规范 | 说明 |
|------|------|
| URL规范 | `/api/v{version}/{module}/{resource}` |
| 动词规范 | GET/POST/PUT/DELETE |
| 认证方式 | Bearer Token |

### 1.2 通用响应格式

```json
{
  "code": 0,
  "message": "success",
  "data": {},
  "timestamp": 1709395200000
}
```

### 1.3 错误码定义

| 错误码 | 说明 | HTTP Status |
|--------|------|-------------|
| 0 | 成功 | 200 |
| 1001 | 参数错误 | 400 |
| 1002 | 未授权 | 401 |
| 1003 | 权限不足 | 403 |
| 1004 | 资源不存在 | 404 |
| 2001 | 服务器内部错误 | 500 |
| 2002 | 服务不可用 | 503 |

---

## 2. 接口列表

### 2.1 用户模块

| 接口 | 方法 | 路径 | 描述 |
|------|------|------|------|
| 用户注册 | POST | /api/v1/user/register | 用户注册 |
| 用户登录 | POST | /api/v1/user/login | 用户登录 |
| 用户登出 | POST | /api/v1/user/logout | 用户登出 |
| 获取用户信息 | GET | /api/v1/user/info | 获取当前用户信息 |
| 更新用户信息 | PUT | /api/v1/user/info | 更新用户信息 |

### 2.2 订单模块

| 接口 | 方法 | 路径 | 描述 |
|------|------|------|------|
| 创建订单 | POST | /api/v1/order | 创建新订单 |
| 查询订单列表 | GET | /api/v1/order | 查询订单列表 |
| 查询订单详情 | GET | /api/v1/order/{id} | 查询订单详情 |
| 取消订单 | PUT | /api/v1/order/{id}/cancel | 取消订单 |
| 申请退款 | POST | /api/v1/order/{id}/refund | 申请退款 |

{根据实际需求添加更多模块}

---

## 3. 接口详情

### 3.1 用户注册

**基本信息**：

| 字段 | 内容 |
|------|------|
| 请求路径 | /api/v1/user/register |
| 请求方法 | POST |
| 认证方式 | 无 |

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| username | string | 是 | 用户名 |
| password | string | 是 | 密码 |
| email | string | 否 | 邮箱 |
| phone | string | 否 | 手机号 |

**请求示例**：

```json
{
  "username": "user001",
  "password": "encrypted_password",
  "email": "user@example.com",
  "phone": "13800138000"
}
```

**响应参数**：

| 参数名 | 类型 | 说明 |
|--------|------|------|
| code | int | 状态码 |
| message | string | 状态信息 |
| data | object | 返回数据 |

**响应示例**：

```json
{
  "code": 0,
  "message": "success",
  "data": {
    "userId": "10001",
    "username": "user001",
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  },
  "timestamp": 1709395200000
}
```

---

## 4. 版本管理

### 4.1 版本策略

| 策略 | 说明 |
|------|------|
| URL版本 | `/api/v1/`、`/api/v2/` |
| 兼容性 | 主版本不兼容，次版本向后兼容 |
| 废弃策略 | 提前2个版本通知废弃 |

### 4.2 版本变更记录

| 版本 | 日期 | 变更内容 |
|------|------|----------|
| v1.0.0 | {date} | 初始版本 |

---

## 5. 自审查清单

- [ ] 所有接口是否遵循RESTful规范
- [ ] 请求/响应格式是否统一
- [ ] 错误码定义是否完整
- [ ] 是否有版本管理策略
- [ ] 接口是否覆盖所有业务场景
- [ ] 参数验证规则是否明确
