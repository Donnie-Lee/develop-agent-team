# 数据库设计文档模板

## 文档信息

| 字段 | 内容 |
|------|------|
| 项目名称 | {project-name} |
| 文档版本 | v{version} |
| 创建日期 | {date} |
| 架构师 | {architect-name} |
| 状态 | {draft/review/approved} |

---

## 1. 数据库选型

### 1.1 数据库总览

| 数据库 | 版本 | 用途 | 选型原因 |
|--------|------|------|----------|
| MySQL | 8.0 | 关系型数据存储 | 成熟稳定、事务支持 |
| Redis | 6.0 | 缓存、会话存储 | 高性能、丰富数据结构 |
| MongoDB | 5.0 | 文档存储 | 灵活 schema |
| Elasticsearch | 8.0 | 全文检索 | 强大搜索能力 |

### 1.2 选型详细说明

#### MySQL 选型原因

| 评估项 | 说明 |
|--------|------|
| 功能满足度 | 支持事务、复杂查询、联表操作 |
| 成熟度 | 社区成熟，文档完善 |
| 性能 | 优化器成熟，性能稳定 |
| 运维成本 | DBA人才丰富，运维工具完善 |

#### Redis 选型原因

| 评估项 | 说明 |
|--------|------|
| 性能 | 内存数据库，纳秒级响应 |
| 数据结构 | 支持String/Hash/List/Set/ZSet |
| 持久化 | 支持RDB和AOF两种持久化方式 |

---

## 2. ER图

### 2.1 实体关系图

```
┌─────────────┐       ┌─────────────┐       ┌─────────────┐
│    User     │       │    Order    │       │   Product   │
├─────────────┤       ├─────────────┤       ├─────────────┤
│ id          │──┐    │ id          │──┐    │ id          │
│ username    │  │    │ user_id     │◄─┘    │ name        │
│ password    │  └──►│ product_id  │       │ price       │
│ email       │       │ amount      │       │ stock       │
│ phone       │       │ status      │       │ category    │
│ created_at  │       │ created_at  │       │ created_at  │
└─────────────┘       └─────────────┘       └─────────────┘
       │                                           │
       │              ┌─────────────┐              │
       │              │   Coupon   │
       └─────────────►├─────────────┤
                      │ id          │
                      │ user_id     │
                      │ code        │
                      │ discount    │
                      │ expire_at   │
                      │ status      │
                      └─────────────┘
```

---

## 3. 表结构设计

### 3.1 用户表 (t_user)

| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 主键 |
| username | VARCHAR(50) | UNIQUE, NOT NULL | 用户名 |
| password | VARCHAR(255) | NOT NULL | 密码(加密) |
| email | VARCHAR(100) | UNIQUE | 邮箱 |
| phone | VARCHAR(20) | UNIQUE | 手机号 |
| avatar | VARCHAR(255) | | 头像URL |
| status | TINYINT | NOT NULL, DEFAULT 1 | 状态:1正常,0禁用 |
| created_at | DATETIME | NOT NULL | 创建时间 |
| updated_at | DATETIME | NOT NULL | 更新时间 |

**索引**：

| 索引名 | 字段 | 类型 | 说明 |
|--------|------|------|------|
| idx_username | username | UNIQUE | 用户名唯一索引 |
| idx_email | email | UNIQUE | 邮箱唯一索引 |
| idx_phone | phone | UNIQUE | 手机号唯一索引 |

### 3.2 订单表 (t_order)

| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 主键 |
| order_no | VARCHAR(32) | UNIQUE, NOT NULL | 订单号 |
| user_id | BIGINT | NOT NULL, FK | 用户ID |
| total_amount | DECIMAL(10,2) | NOT NULL | 订单总额 |
| discount_amount | DECIMAL(10,2) | DEFAULT 0 | 优惠金额 |
| pay_amount | DECIMAL(10,2) | NOT NULL | 实付金额 |
| status | TINYINT | NOT NULL | 状态:1待支付,2已支付,3已取消,4已退款 |
| pay_time | DATETIME | | 支付时间 |
| created_at | DATETIME | NOT NULL | 创建时间 |
| updated_at | DATETIME | NOT NULL | 更新时间 |

**索引**：

| 索引名 | 字段 | 类型 | 说明 |
|--------|------|------|------|
| idx_order_no | order_no | UNIQUE | 订单号唯一索引 |
| idx_user_id | user_id | | 用户ID索引 |
| idx_status | status | | 状态索引 |
| idx_created_at | created_at | | 创建时间索引 |

### 3.3 订单商品表 (t_order_item)

| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 主键 |
| order_id | BIGINT | NOT NULL, FK | 订单ID |
| product_id | BIGINT | NOT NULL, FK | 商品ID |
| product_name | VARCHAR(100) | NOT NULL | 商品名称 |
| price | DECIMAL(10,2) | NOT NULL | 单价 |
| quantity | INT | NOT NULL | 数量 |
| subtotal | DECIMAL(10,2) | NOT NULL | 小计 |
| created_at | DATETIME | NOT NULL | 创建时间 |

### 3.4 商品表 (t_product)

| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 主键 |
| name | VARCHAR(100) | NOT NULL | 商品名称 |
| description | TEXT | | 商品描述 |
| price | DECIMAL(10,2) | NOT NULL | 价格 |
| stock | INT | NOT NULL, DEFAULT 0 | 库存 |
| category_id | BIGINT | | 分类ID |
| status | TINYINT | NOT NULL, DEFAULT 1 | 状态:1上架,0下架 |
| created_at | DATETIME | NOT NULL | 创建时间 |
| updated_at | DATETIME | NOT NULL | 更新时间 |

### 3.5 优惠券表 (t_coupon)

| 字段名 | 类型 | 约束 | 说明 |
|--------|------|------|------|
| id | BIGINT | PK, AUTO_INCREMENT | 主键 |
| code | VARCHAR(32) | UNIQUE, NOT NULL | 优惠券码 |
| user_id | BIGINT | FK | 用户ID(可为NULL表示通用券) |
| discount_type | TINYINT | NOT NULL | 优惠类型:1满减,2折扣 |
| discount_value | DECIMAL(10,2) | NOT NULL | 优惠值 |
| min_amount | DECIMAL(10,2) | | 最低消费金额 |
| start_at | DATETIME | NOT NULL | 开始时间 |
| expire_at | DATETIME | NOT NULL | 过期时间 |
| status | TINYINT | NOT NULL | 状态:1未使用,2已使用,3已过期 |
| created_at | DATETIME | NOT NULL | 创建时间 |

---

## 4. 命名规范

### 4.1 表命名规范

| 规范 | 说明 | 示例 |
|------|------|------|
| 前缀 | t_ | t_user |
| 单词分隔 | 下划线 | t_order_item |
| 全小写 | 全部小写 | t_product |

### 4.2 字段命名规范

| 规范 | 说明 | 示例 |
|------|------|------|
| 单词分隔 | 下划线 | created_at |
| 时间字段 | _at结尾 | created_at, updated_at |
| 状态字段 | status | status |
| 外键字段 | _id结尾 | user_id, order_id |

---

## 5. 索引设计原则

### 5.1 索引创建原则

- WHERE条件中使用的字段
- JOIN操作的连接字段
- ORDER BY排序的字段
- SELECT中频繁查询的字段

### 5.2 索引注意事项

- 避免在频繁更新的字段上建索引
- 避免在低区分度字段上建索引
- 复合索引注意字段顺序

---

## 6. 数据安全

### 6.1 敏感数据处理

| 字段 | 处理方式 |
|------|----------|
| password | BCrypt加密存储 |
| phone | 脱敏显示 |
| email | 部分隐藏 |

### 6.2 数据权限

| 策略 | 说明 |
|------|------|
| 行级权限 | 用户只能查询自己的数据 |
| 列级权限 | 敏感字段需要权限才能查询 |

---

## 7. 自审查清单

- [ ] ER图是否展示完整实体关系
- [ ] 表结构设计是否遵循命名规范
- [ ] 索引设计是否合理
- [ ] 是否考虑了数据安全
- [ ] 外键关系是否正确
- [ ] 是否有敏感数据处理
- [ ] 数据类型选择是否合适
