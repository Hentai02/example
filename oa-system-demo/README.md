# OA 办公自动化管理系统

基于 **Vue 3** 前端 + **Spring Boot 3.4** 后端构建的企业级办公自动化管理系统，涵盖员工管理、考勤、请假、加班、报销、薪资、日程、任务、消息通知及文件管理等功能。

## 技术架构

| 层级 | 技术栈 |
|------|--------|
| 前端 | Vue 3 (Composition API) + Vite 6 + Vue Router 4 + Pinia + Axios |
| UI 样式 | Tailwind CSS 4 + Font Awesome + vue-cal |
| 代码规范 | ESLint + Prettier |
| 后端 | Spring Boot 3.4.2 + Spring Security + Spring Data JDBC |
| 数据库 | MySQL 8.0+ |
| 构建工具 | Maven (后端) / npm (前端) |
| 语言版本 | Java 17 / Node.js 18+ |

## 功能模块

### 用户与权限
- 用户注册、登录与密码修改
- 基于 Spring Security JDBC 的身份认证
- 多角色权限控制（管理员、人事主管、财务主管、生产主管、组长/超级管理员）

### 员工管理
- 员工信息增删改查、分页与关键字搜索
- 职位管理、基本工资配置

### 考勤管理
- 上班签到 / 下班签退
- 根据签到时间自动判定出勤状态（正常 / 迟到 / 缺勤）
- 考勤记录查询

### 请假管理
- 请假申请提交与审批流程（生产主管审批）
- 审批记录查询，审批通过后自动发送通知

### 加班管理
- 加班申请提交与审批流程
- 审批记录查询，审批通过后自动发送通知

### 报销管理
- 报销申请提交与审批流程（财务主管审批）
- 附件上传支持

### 薪资管理
- 根据职位自动计算基本工资
- 加班费、考勤扣款、全勤奖金核算
- 月度薪资汇总与查询

### 日程管理
- 个人日程增删改查、提醒时间设置、日程共享

### 任务管理
- 任务创建、分配、优先级与状态跟踪、截止日期管理

### 消息通知
- 系统消息收发、阅读状态管理
- 请假 / 加班 / 报销审批自动通知

### 文件管理
- 文件与文件夹的创建、删除
- 基于路径的文件权限控制（读写权限分配）
- 文件上传与下载

### 首页仪表盘
- 系统概览：用户个人信息卡片、任务卡片、近期日程、薪资信息等

### 工作台
- 员工个人的请假记录、加班记录、报销记录快速查看

## 项目结构

```
oa-system-demo/
├── oa-frontend/                # Vue 3 前端项目
│   ├── src/
│   │   ├── components/         # 组件（按模块划分 tab1~tab7）
│   │   ├── router/             # 路由配置
│   │   ├── stores/             # Pinia 状态管理
│   │   ├── views/              # 布局视图
│   │   └── assets/             # 样式与图片
│   ├── vite.config.js
│   └── package.json
├── oa-backend/                 # Spring Boot 后端项目
│   └── src/main/java/example/oasystem/
│       ├── config/             # 安全配置
│       ├── controller/         # 控制器层
│       ├── entries/            # 实体类
│       ├── repository/         # 数据访问层
│       ├── service/            # 业务逻辑层
│       └── storage/            # 文件存储服务
```

## 环境要求

| 依赖 | 版本 |
|------|------|
| JDK | 17+ |
| Maven | 3.6+ |
| MySQL | 8.0+ |
| Node.js | 18+ |
| npm | 9+ |

## 快速开始

### 1. 数据库初始化

```sql
CREATE DATABASE IF NOT EXISTS example DEFAULT CHARACTER SET utf8mb4;
CREATE DATABASE IF NOT EXISTS oa DEFAULT CHARACTER SET utf8mb4;
```

导入表结构与初始数据（SQL 文件位于 `oa-backend/` 目录下）：

```bash
mysql -u root -p example < oa-backend/structure.sql
mysql -u root -p example < oa-backend/data.sql
```

### 2. 配置数据库连接

编辑 `oa-backend/src/main/resources/application.properties`，修改数据库用户名和密码：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/example?allowPublicKeyRetrieval=true&useSSL=false
spring.datasource.username=root
spring.datasource.password=你的密码
```

### 3. 启动后端

```bash
cd oa-backend
mvnw.cmd spring-boot:run          # Windows
# 或
./mvnw spring-boot:run            # Linux / macOS
```

后端默认运行在 `http://localhost:8080`。

### 4. 启动前端

```bash
cd oa-frontend
npm install
npm run dev
```

前端开发服务器默认运行在 `http://localhost:5173`，`/api` 路径的请求会自动代理到后端服务。

## 角色权限

系统中各角色的访问权限如下：

| 角色 | 可访问模块 |
|------|-----------|
| ROLE_管理员 | 所有模块（系统管理） |
| ROLE_人事主管 | 人力资源管理相关模块 |
| ROLE_财务主管 | 报销审批、工资管理 |
| ROLE_生产主管 | 请假审批、加班审批、日程管理 |
| ROLE_组长 | 考勤管理、请假管理、任务管理 |

## API 概览

| 模块 | 基础路径 | 说明 |
|------|---------|------|
| 用户 | `/user` | 注册、登录、密码修改 |
| 员工 | `/employee` | 员工 CRUD、职位查询 |
| 考勤 | `/attendance` | 签到签退、记录查询 |
| 请假 | `/leave` | 申请、审批、记录查询 |
| 加班 | `/overtime` | 申请、审批、记录查询 |
| 报销 | `/reimbursement` | 申请、审批、记录查询 |
| 薪资 | `/payroll` | 薪资计算与查询 |
| 日程 | `/schedule` | 日程管理 |
| 任务 | `/task` | 任务分配与跟踪 |
| 消息 | `/message` | 消息收发 |
| 文件 | `/files` | 文件管理与权限控制 |
| 上传 | `/file-upload` | 文件上传下载 |

## 测试与代码规范

```bash
# 后端测试
cd oa-backend
mvnw.cmd test

# 前端代码检查与格式化
cd oa-frontend
npm run lint          # ESLint 检查并自动修复
npm run format        # Prettier 代码格式化
```
