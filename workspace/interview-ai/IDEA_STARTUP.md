# IDEA 本地启动指南

## 环境要求
- JDK 17+ (推荐 GraalVM CE 17.0.9 或更高版本)
- Maven 3.9+
- Docker Desktop (用于启动基础设施)

## 1. 启动基础设施

```bash
cd interview-ai
docker-compose up -d
```

验证服务状态:
```bash
docker-compose ps
```

## 2. IDEA 配置

### 2.1 设置 Maven JDK
1. File → Settings → Build, Execution, Deployment → Build Tools → Maven → Runner
2. JRE 选择: `/Users/zhlee/Library/Java/JavaVirtualMachines/graalvm-ce-17.0.9/Contents/Home`

### 2.2 导入项目
1. File → Open → 选择 `interview-ai` 文件夹
2. 选择「作为 Maven 项目导入」

### 2.3 启用 Spring Boot 运行配置
1. 安装「Spring Boot」插件 (如果未安装)
2. 右键点击各个服务 `*ServiceApplication.java` → Run

## 3. 配置文件说明

项目支持多环境配置：
- `application.yml` - 基础配置，使用环境变量占位符
- `application-dev.yml` - 开发环境配置
- `application-prod.yml` - 生产环境配置

### 3.1 环境切换

在运行配置中添加：
```
Active Profile: dev
```

或设置环境变量：
```bash
SPRING_PROFILES_ACTIVE=dev
```

## 4. 启动顺序

推荐按以下顺序启动:

| 顺序 | 服务 | 端口 | 说明 |
|------|------|------|------|
| 1 | nacos | 8848 | 服务发现/注册中心 |
| 2 | postgres | 5432 | 数据库 |
| 3 | redis | 6379 | 缓存 |
| 4 | user-service | 8081 | 用户服务 (其他服务依赖) |
| 5 | resume-service | 8082 | 简历服务 |
| 6 | question-service | 8084 | 题库服务 |
| 7 | member-service | 8085 | 会员服务 |
| 8 | interview-service | 8083 | 面试服务 |
| 9 | notify-service | 8086 | 通知服务 |
| 10 | ai-gateway | 8087 | AI网关 |

## 5. 运行配置

各服务的主类:
- user-service: `com.interviewai.user.UserServiceApplication`
- resume-service: `com.interviewai.resume.ResumeServiceApplication`
- interview-service: `com.interviewai.interview.InterviewServiceApplication`
- question-service: `com.interviewai.question.QuestionServiceApplication`
- member-service: `com.interviewai.member.MemberServiceApplication`
- notify-service: `com.interviewai.notify.NotifyServiceApplication`
- ai-gateway: `com.interviewai.aigateway.AiGatewayApplication`

### VM Options (可选)
```
-Xms512m -Xmx1024m -XX:+UseG1GC
```

### Active Profiles
```
dev
```

## 6. 环境变量参考

### Dev 环境 (.dev 文件)
```bash
# 数据库
DB_HOST=localhost
DB_PORT=5432
DB_USERNAME=postgres
DB_PASSWORD=postgres

# Redis
REDIS_HOST=localhost
REDIS_PORT=6379

# Nacos
NACOS_HOST=localhost

# JWT
JWT_SECRET=dev-secret-key-change-in-production-12345678
```

### Prod 环境 (系统环境或 K8s ConfigMap)
```bash
# 数据库
DB_HOST=prod-postgres.internal
DB_PORT=5432
DB_NAME=interview_ai
DB_USERNAME=app_user
DB_PASSWORD=<secret>

# Redis
REDIS_HOST=prod-redis.internal
REDIS_PORT=6379
REDIS_PASSWORD=<secret>

# Nacos
NACOS_HOST=prod-nacos.internal
NACOS_PORT=8848

# JWT
JWT_SECRET=<production-secret>

# 阿里云
ALIYUN_ACCESS_KEY_ID=<key>
ALIYUN_ACCESS_KEY_SECRET=<secret>
DASHSCOPE_API_KEY=<api-key>
```

## 7. 验证服务

访问 Nacos 控制台: http://localhost:8848/nacos
- 用户名: nacos
- 密码: nacos

## 8. 常见问题

### Q: 服务启动失败，连接 Nacos 超时
A: 确保 Nacos 先启动并运行正常

### Q: 数据库连接失败
A: 确保 PostgreSQL 已启动且数据库 `interview_ai` 已创建

### Q: Redis 连接失败
A: 确保 Redis 容器正在运行

### Q: Lombok 注解未生效
A: 确保 IDEA 启用了 Annotation Processing:
   Settings → Build, Execution, Deployment → Compiler → Annotation Processors → Enable annotation processing
