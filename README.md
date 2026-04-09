# Develop Agent Team

通用开发团队多Agent协作系统，通过多个专业化Agent的协同工作，模拟真实软件开发团队的完整开发流程。

## 项目结构

```
develop-agent-team/
├── CLAUDE.md              # 项目核心文档（必读）
├── README.md              # 项目入口说明
├── LICENSE                # 项目授权
├── .claude/skills/                # 各角色Agent技能定义
│   ├── pm-manager/       # 产品经理
│   ├── architect/        # 架构师
│   ├── backend-dev/      # 后端开发
│   ├── frontend-admin/   # 前端-管理端
│   ├── frontend-h5/      # 前端-H5
│   ├── frontend-app/     # 前端-移动端App
│   ├── frontend-miniprogram/  # 前端-小程序
│   ├── ui-ue/            # UI/UE设计师
│   └── qa-tester/        # 测试工程师
└── workspace/            # 工作目录
    └── docs/              # 文档输出目录
```

## 角色与职责

| 角色 | Skill路径 | 职责 |
|------|-----------|------|
| 产品经理 | `.claude/skills/pm-manager` | 需求分析、PRD编写、项目规划 |
| 架构师 | `.claude/skills/architect` | 技术架构设计、技术选型 |
| 后端开发 | `.claude/skills/backend-dev` | API开发、数据库设计 |
| 管理端开发 | `.claude/skills/frontend-admin` | Web管理后台开发 |
| H5开发 | `.claude/skills/frontend-h5` | 移动端H5开发 |
| App开发 | `.claude/skills/frontend-app` | iOS/Android App开发 |
| 小程序开发 | `.claude/skills/frontend-miniprogram` | 微信/支付宝小程序开发 |
| UI/UE设计师 | `.claude/skills/ui-ue` | 视觉设计、交互设计 |
| 测试工程师 | `.claude/skills/qa-tester` | 测试用例、自动化测试 |

## 工作流程

```
PM → 架构师 → UI/UE + 前后端开发 → 联调 → QA测试 → 交付
```

详见 `CLAUDE.md` 第2.7节「Agent协作流程」

## 快速开始

### 1. 创建新项目

启动PM Agent进行需求分析和PRD编写：
```
/使用的skill: pm-manager
新项目启动：创建一个名为xxx的项目...
```

### 2. 技术架构设计

启动Architect Agent进行技术选型和架构设计：
```
/使用的skill: architect
针对xxx项目进行技术架构设计...
```

### 3. UI/UE设计

启动UI/UE Agent进行多端设计：
```
/使用的skill: ui-ue
针对xxx项目进行UI设计...
```

### 4. 前后端开发

启动各端开发Agent进行开发：
```
/使用的skill: frontend-admin   # 管理端
/使用的skill: frontend-h5      # H5端
/使用的skill: frontend-app     # App端
/使用的skill: frontend-miniprogram  # 小程序端
/使用的skill: backend-dev      # 后端
```

### 5. 测试

启动QA Agent进行测试：
```
/使用的skill: qa-tester
针对xxx项目进行测试...
```

## 文档输出规范

所有文档输出到 `workspace/docs/` 目录：

```
workspace/docs/
├── pm/v{version}/           # PM文档
├── architect/v{version}/     # 架构师文档
├── ui-ue/v{version}/         # UI/UE文档
├── frontend/{端}/v{version}/ # 前端文档
├── backend/v{version}/       # 后端文档
└── qa/v{version}/           # QA文档
```

## 更多信息

- 完整项目规范：见 `CLAUDE.md`
- 各角色详细规范：见 `.claude/skills/{role}/SKILL.md`
- 模板文件：见 `.claude/skills/{role}/references/`
