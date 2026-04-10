# AI面试平台 - 前端自动化测试

## 概述

本项目使用 Playwright 实现 AI 面试平台的前端自动化测试，覆盖 H5 移动端和 Admin 管理端的所有核心功能。

## 测试范围

### H5 移动端
- 登录模块（手机号验证、万能验证码登录）
- 首页模块（职位推荐、热门面试题）
- 个人中心（用户信息、面试统计）
- 简历管理（CRUD）
- 面试流程（岗位选择、配置、面试）

### Admin 管理端
- 管理员登录
- 仪表盘（统计数据、最近活动）
- 题库管理（题目增删改查、分页）
- 用户管理（用户列表、搜索）

## 环境要求

- Node.js >= 16.0
- npm >= 8.0
- Chromium / Firefox / WebKit（Playwright会自动安装）

## 安装

```bash
# 安装依赖
npm install

# 安装浏览器
npx playwright install chromium --with-deps
```

## 配置

### 环境变量

复制 `.env.example` 为 `.env` 并配置：

```bash
cp .env.example .env
```

配置项：
- `H5_BASE_URL`: H5前端地址（默认 http://localhost:5173）
- `ADMIN_BASE_URL`: Admin管理端地址（默认 http://localhost:5174）
- `BASE_URL`: 默认基础URL

### 测试账号

| 平台 | 手机号 | 验证码 | 说明 |
|------|--------|--------|------|
| H5 / Admin | 13800138000 | 000000 | 万能验证码 |

## 运行测试

### 运行所有测试
```bash
npm test
```

### 运行带UI的测试
```bash
npm run test:ui
```

### 运行 H5 测试
```bash
npm run test:h5
```

### 运行 Admin 测试
```bash
npm run test:admin
```

### 运行 Headless 测试
```bash
npm run test:headed
```

### 生成HTML报告
```bash
npm run test:ci
```

## 查看报告

```bash
npm run report
```

## 测试结构

```
playwright/
├── pages/              # 页面对象模型
│   ├── h5-pages.js     # H5页面元素和方法
│   └── admin-pages.js  # Admin页面元素和方法
├── tests/              # 测试用例
│   ├── h5.spec.js      # H5功能测试
│   └── admin.spec.js   # Admin功能测试
├── utils/              # 辅助工具
│   └── test-data.js    # 测试配置和数据
├── playwright.config.js # Playwright配置
└── package.json
```

## 页面对象模型

测试使用 Page Object 模式，提高代码复用性和可维护性。

### H5页面
- `H5LoginPage` - 登录页
- `H5HomePage` - 首页
- `H5ProfilePage` - 个人中心
- `H5ResumeListPage` - 简历列表
- `H5ResumeEditPage` - 简历编辑
- `H5JobSelectPage` - 岗位选择
- `H5InterviewConfigPage` - 面试配置
- `H5TextInterviewPage` - 文字面试
- `H5InterviewReportPage` - 面试报告

### Admin页面
- `AdminLoginPage` - 登录页
- `AdminDashboardPage` - 仪表盘
- `AdminQuestionManagementPage` - 题库管理
- `AdminUserManagementPage` - 用户管理
- `AdminResumeManagementPage` - 简历管理
- `AdminInterviewManagementPage` - 面试管理
- `AdminSettingsPage` - 系统设置

## 测试用例

详细测试用例请参考：
- `../功能自动化/H5功能测试用例.md`
- `../功能自动化/Admin功能测试用例.md`

## CI/CD集成

可在CI/CD流水线中运行：

```bash
# 设置环境变量
export H5_BASE_URL=http://h5-app:80
export ADMIN_BASE_URL=http://admin-app:80

# 运行测试
npm run test:ci
```

## 常见问题

### Q: 浏览器未安装
```bash
npx playwright install chromium
```

### Q: 端口被占用
修改 `playwright.config.js` 中的 `webServer.port`

### Q: 登录测试失败
确认后端服务已启动，且万能验证码功能已启用

## 维护

- 定期更新 Playwright 版本
- 页面元素变更时更新页面对象模型
- 新功能添加后补充测试用例
