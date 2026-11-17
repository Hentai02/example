# OA 办公自动化系统 - 前端

基于 Vue 3 + Vite 构建的企业级办公自动化系统前端项目。

## 技术栈

- **框架**: Vue 3 (Composition API)
- **构建工具**: Vite 6
- **路由**: Vue Router 4
- **状态管理**: Pinia
- **HTTP 客户端**: Axios
- **UI 样式**: Tailwind CSS 4
- **图标**: Font Awesome
- **日历**: vue-cal
- **代码校验**: ESLint + Prettier

## 功能模块

- **首页仪表盘**: 系统概览，包括用户个人信息卡片、任务卡片、近期日程、薪资信息等
- **工作台**: 员工个人的请假记录、加班记录、报销记录快速查看
- **审批管理**: 请假审批、加班审批、报销审批（支持多角色权限）
- **日程与任务管理**: 日程安排与任务分配追踪
- **人力资源管理**: 考勤管理、请假管理、工资管理、员工管理
- **本地消息**: 内部消息通知
- **文件管理**: 文件上传下载及权限分配
- **系统设置**: 系统级配置管理

## 角色权限

系统支持以下角色，不同模块对角色有访问限制：

| 角色 | 说明 |
|------|------|
| ROLE_管理员 | 系统管理员，可访问所有模块 |
| ROLE_人事主管 | 人力资源相关模块 |
| ROLE_财务主管 | 报销审批、工资管理 |
| ROLE_生产主管 | 请假审批、加班审批、日程管理 |
| ROLE_组长 | 考勤管理、请假管理、任务管理 |

## 环境要求

- Node.js >= 18
- npm >= 9

## 项目启动

```sh
# 安装依赖
npm install

# 启动开发服务器
npm run dev

# 构建生产版本
npm run build

# 预览生产构建
npm run preview
```

## 开发说明

### 代理配置

开发环境下，`/api` 路径的请求会被代理到后端服务 `http://localhost:8080`。代理配置位于 `vite.config.js`。

### 代码规范

```sh
# ESLint 检查并自动修复
npm run lint

# Prettier 代码格式化
npm run format
```

### 目录结构

```
oa-frontend/
├── public/                  # 静态资源
├── src/
│   ├── assets/              # 样式与图片
│   ├── components/          # 组件
│   │   ├── dialog/          # 弹窗组件（新建/编辑实体）
│   │   ├── icons/           # 图标组件
│   │   ├── model_view/      # 详情/编辑视图
│   │   ├── tab1/            # 工作台相关
│   │   ├── tab2/            # 审批管理相关
│   │   ├── tab3/            # 日程与任务管理
│   │   ├── tab4/            # 人力资源管理
│   │   ├── tab5/            # 本地消息
│   │   ├── tab6/            # 文件管理
│   │   ├── tab7/            # 系统设置
│   │   └── deprecated/      # 已废弃组件
│   ├── router/              # 路由配置
│   ├── stores/              # Pinia 状态管理
│   ├── views/               # 布局视图
│   ├── App.vue              # 根组件
│   └── main.js              # 入口文件
├── index.html
├── vite.config.js
├── eslint.config.js
├── .prettierrc.json
└── package.json
```

## 后端依赖

本前端项目需要配合对应的 OA 后端服务运行，后端默认地址为 `http://localhost:8080`。
