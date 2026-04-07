# InterviewAI H5

移动端 H5 前端项目

## 技术栈

- Vue 3
- Vite
- Vant 4
- Pinia
- Axios
- Vue Router

## 开发

```bash
# 安装依赖
npm install

# 启动开发服务器
npm run dev

# 构建生产版本
npm run build
```

## 项目结构

```
src/
├── api/          # API 请求封装
├── assets/       # 静态资源
├── components/   # 公共组件
├── router/       # 路由配置
├── stores/       # Pinia 状态管理
├── styles/       # 公共样式
└── views/        # 页面组件
```

## 页面说明

- `/login` - 登录注册页面
- `/home` - 首页
- `/profile` - 个人中心
- `/resume` - 简历列表
- `/resume/edit` - 简历编辑
