# OA-System 办公自动化系统

基于 Spring Boot 3.4 的企业办公自动化管理系统，提供员工管理、考勤、请假、加班、报销、薪资、日程、任务、消息通知及文件管理等功能。

## 技术栈

- **后端框架：** Spring Boot 3.4.2
- **安全框架：** Spring Security
- **数据访问：** Spring Data JDBC
- **数据库：** MySQL
- **构建工具：** Maven
- **Java 版本：** 17

## 功能模块

### 用户管理 (`/user`)
- 用户注册与登录
- 密码修改
- 基于 Spring Security JDBC 的身份认证

### 员工管理 (`/employee`)
- 员工信息的增删改查
- 分页查询与关键字搜索
- 职位管理

### 考勤管理 (`/attendance`)
- 上班签到 / 下班签退
- 根据签到时间自动判定出勤状态（正常/迟到/缺勤）
- 考勤记录查询

### 请假管理 (`/leave`)
- 请假申请提交
- 审批流程（生产主管审批）
- 审批记录查询
- 自动发送通知消息

### 加班管理 (`/overtime`)
- 加班申请提交
- 审批流程
- 审批记录查询

### 报销管理 (`/reimbursement`)
- 报销申请提交
- 审批流程（财务主管审批）
- 附件上传支持

### 薪资管理 (`/payroll`)
- 根据职位自动计算基本工资
- 加班费、考勤扣款、全勤奖金核算
- 月度薪资汇总

### 日程管理 (`/schedule`)
- 个人日程的增删改查
- 提醒时间设置
- 日程共享功能

### 任务管理 (`/task`)
- 任务创建与分配
- 任务优先级与状态跟踪
- 截止日期管理

### 消息通知 (`/message`)
- 系统消息收发
- 消息阅读状态管理
- 请假/加班/报销审批自动通知

### 文件管理 (`/files`)
- 文件与文件夹的创建、删除
- 基于路径的文件权限控制（读写权限分配）
- 文件上传下载

## 项目结构

```
src/main/java/example/oasystem/
├── OaSystemApplication.java          # 应用入口
├── config/
│   └── SecurityConfig.java           # 安全配置
├── controller/                       # 控制器层
│   ├── UserController.java
│   ├── EmployeeController.java
│   ├── AttendanceRecordController.java
│   ├── LeaveRequestController.java
│   ├── OvertimeRequestController.java
│   ├── ReimbursementRequestController.java
│   ├── PayrollController.java
│   ├── ScheduleController.java
│   ├── TaskController.java
│   ├── MessageController.java
│   ├── FileItemController.java
│   ├── FileUploadController.java
│   ├── approval/                     # 审批相关控制器
│   │   ├── LeaveApprovalController.java
│   │   ├── OvertimeRequestController.java
│   │   └── ReimbursementRequestController.java
│   └── personresources/              # 人事管理控制器
│       └── ...
├── entries/                          # 实体类
│   ├── Employee.java
│   ├── Position.java
│   ├── LeaveRequest.java
│   ├── ApprovalRecord.java
│   ├── AttendanceRecord.java
│   ├── OvertimeRequest.java
│   ├── ReimbursementRequest.java
│   ├── Payroll.java
│   ├── SalaryPayment.java
│   ├── Schedule.java
│   ├── Task.java
│   ├── Message.java
│   ├── FileItem.java
│   ├── FilePermission.java
│   └── Authorities.java
├── repository/                       # 数据访问层
├── service/                          # 业务逻辑层
└── storage/                          # 文件存储服务
```

## 环境要求

- JDK 17+
- Maven 3.6+
- MySQL 8.0+

## 快速开始

### 1. 创建数据库

导入项目根目录下的 SQL 文件：

```sql
-- 创建数据库（application.properties 中配置的数据库）
CREATE DATABASE IF NOT EXISTS example DEFAULT CHARACTER SET utf8mb4;
-- Spring Security 使用的数据库
CREATE DATABASE IF NOT EXISTS oa DEFAULT CHARACTER SET utf8mb4;
```

导入表结构与初始数据：

```bash
mysql -u root -p example < structure.sql
mysql -u root -p example < data.sql
```

### 2. 配置数据库连接

编辑 `src/main/resources/application.properties`：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/example?allowPublicKeyRetrieval=true&useSSL=false
spring.datasource.username=root
spring.datasource.password=你的密码
```

### 3. 启动项目

```bash
# Windows
mvnw.cmd spring-boot:run

# Linux / macOS
./mvnw spring-boot:run
```

### 4. 运行测试

```bash
mvnw.cmd test
```

## 职位与权限说明

| 职位 ID | 职位名称 | 基本工资 | 权限说明 |
|---------|---------|---------|---------|
| 1 | - | 8000 | - |
| 2 | 财务主管 | 6000 | 审批报销申请 |
| 3 | - | 5000 | - |
| 4 | 生产主管 | 10000 | 审批请假、加班申请 |
| 5 | 管理员 | 7000 | 系统管理 |
| 6 | - | 7000 | - |
| 7 | 超级管理员 | - | 查看所有审批记录 |

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

## 前端配合

CORS 已配置允许 `https://localhost:5173`（Vue.js / Vite 开发服务器），可直接与前端项目联调。
