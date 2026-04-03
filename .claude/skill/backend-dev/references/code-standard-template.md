# 后端代码规范文档模板

## 项目信息

| 字段 | 内容 |
|------|------|
| 项目名称 | {project-name} |
| 版本 | v{version} |
| 最后更新 | {date} |
| 负责人 | {architect-name} |

---

## 1. 开发环境

| 项目 | 版本 | 说明 |
|------|------|------|
| JDK | {version} | Java开发环境 |
| Maven/Gradle | {version} | 构建工具 |
| MySQL | {version} | 数据库 |
| Redis | {version} | 缓存 |

---

## 2. 项目结构规范

### 2.1 目录结构

```
{project-root}/
├── src/main/java/
│   └── com/{company}/{project}/
│       ├── controller/     # Controller层
│       ├── service/        # Service层
│       │   └── impl/       # Service实现
│       ├── repository/     # Repository层
│       ├── model/          # 实体类
│       │   ├── entity/     # 数据实体
│       │   ├── dto/        # 数据传输对象
│       │   └── vo/         # 视图对象
│       ├── config/         # 配置类
│       ├── common/         # 公共类
│       │   ├── constant/   # 常量
│       │   ├── enums/      # 枚举
│       │   ├── exception/   # 异常
│       │   └── utils/      # 工具类
│       └── {Project}Application.java
├── src/main/resources/
│   ├── mapper/             # MyBatis映射文件
│   ├── application.yml     # 应用配置
│   └── logback-spring.xml  # 日志配置
├── src/test/
│   └── java/              # 测试代码
├── pom.xml
└── README.md
```

---

## 3. 命名规范

### 3.1 类命名

| 类型 | 规范 | 示例 |
|------|------|------|
| Controller | {Name}Controller | UserController |
| Service | I{Name}Service | IUserService |
| ServiceImpl | {Name}ServiceImpl | UserServiceImpl |
| Repository | {Name}Repository | UserRepository |
| Entity | {Name} | User |
| DTO | {Name}DTO | UserDTO |
| VO | {Name}VO | UserVO |
| Config | {Name}Config | WebConfig |

### 3.2 方法命名

| 操作 | 规范 | 示例 |
|------|------|------|
| 查询单个 | get{Name}ById | getUserById |
| 查询列表 | list{Name}s | listUsers |
| 分页查询 | page{Name}s | pageUsers |
| 新增 | save{Name} | saveUser |
| 更新 | update{Name} | updateUser |
| 删除 | delete{Name}ById | deleteUserById |
| 批量操作 | batchSave{Name}s | batchSaveUsers |

### 3.3 变量命名

| 类型 | 规范 | 示例 |
|------|------|------|
| 普通变量 | lowerCamelCase | userName |
| 常量 | UPPER_SNAKE_CASE | MAX_COUNT |
| 集合变量 | 复数或List后缀 | users / userList |
| 布尔变量 | is/has/can前缀 | isActive |

---

## 4. 代码格式规范

### 4.1 缩进与空格

- 使用4个空格缩进
- 运算符前后加空格
- 逗号后加空格
- 大括号前后加空格

### 4.2 方法规范

```java
// 正确示例
public UserDTO getUserById(Long id) {
    if (id == null) {
        return null;
    }
    return userRepository.findById(id);
}

// 错误示例 - 过长的方法
public UserDTO getUserById(Long id) { if (id == null) { return null; } return userRepository.findById(id); }
```

### 4.3 类规范

```java
@RestController
@RequestMapping("/api/v1/users")
@Slf4j
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }
}
```

---

## 5. 注释规范

### 5.1 类注释

```java
/**
 * 用户管理Controller
 * 负责用户注册、登录、信息查询等操作
 *
 * @author {author}
 * @date {date}
 */
@RestController
public class UserController {
}
```

### 5.2 方法注释

```java
/**
 * 根据用户ID获取用户信息
 *
 * @param id 用户ID
 * @return 用户信息
 * @throws BusinessException 当用户不存在时抛出
 */
public UserDTO getUserById(Long id) {
}
```

### 5.3 行内注释

```java
// 用于解释复杂逻辑
int maxRetries = 3;  // 最大重试次数
```

---

## 6. API设计规范

### 6.1 URL规范

```
/api/v{version}/{module}/{resource}
/api/v1/user/register
```

### 6.2 响应格式

```json
{
  "code": 0,
  "message": "success",
  "data": {},
  "timestamp": 1709395200000
}
```

### 6.3 错误码规范

| 错误码范围 | 说明 |
|-----------|------|
| 0 | 成功 |
| 1001-1999 | 参数错误 |
| 2001-2999 | 业务错误 |
| 3001-3999 | 权限错误 |
| 5001-5999 | 系统错误 |

---

## 7. 数据库规范

### 7.1 表命名

- 使用小写字母
- 单词间用下划线分隔
- 添加适当前缀

### 7.2 字段规范

| 字段类型 | 规范 |
|----------|------|
| 主键 | id BIGINT PK AUTO_INCREMENT |
| 创建时间 | created_at DATETIME |
| 更新时间 | updated_at DATETIME |
| 状态 | status TINYINT |
| 逻辑删除 | deleted TINYINT |

---

## 8. Git提交规范

### 8.1 提交格式

```
<type>: <subject>

<body>

<footer>
```

### 8.2 Type类型

| type | 说明 |
|------|------|
| feat | 新功能 |
| fix | Bug修复 |
| docs | 文档更新 |
| style | 代码格式 |
| refactor | 重构 |
| test | 测试 |
| chore | 构建/工具 |

### 8.3 提交示例

```
feat(user): add user login functionality

- implement login API
- add JWT token generation
- add login validation

Closes #123
```
