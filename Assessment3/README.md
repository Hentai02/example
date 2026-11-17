# RobotCo 3000

一个 C++ 工业机器人管理系统，用于跟踪机器人、任务和健康状态指标。系统使用**策略模式**根据任务类型计算优先级/告警等级，并使用**观察者模式**向外部系统发送通知。

## 设计模式

| 模式     | 实现说明                                         |
| -------- | ------------------------------------------------ |
| 策略     | 按任务类型（补料、入库、出库）计算优先级         |
| 观察者   | 优先级变化时通知工厂告警系统和充电通知系统       |
| 外观     | 封装外部系统接口                                 |
| 适配器   | 将 `RobotFileLoader` 适配到 `AbstractRobotDatabaseLoader` 接口 |
| 组合     | 将数据库加载器和文件加载器组合为统一的加载器     |

## 环境要求

- Visual Studio 2022 或更高版本（MSBuild v143 工具集）
- Windows 10+

## 构建

```powershell
msbuild Assessment3.sln /p:Configuration=Debug /p:Platform=x64
```

或在 Visual Studio IDE 中打开 `Assessment3.sln` 进行构建。

## 运行

需在 `Assessment3` 目录下运行（以确保能读取 `robots.txt`）：

```powershell
.\Assessment3\x64\Debug\Assessment3.exe
```

### 菜单选项

1. **列出机器人** — 显示所有已加载的机器人及其状态指标和优先级。
2. **添加当前指标** — 为指定机器人录入电量、气压和连接强度值，触发优先级重算和观察者通知。
3. **退出** — 退出程序。

## 项目结构

```
Assessment3/
├── Assessment3.cpp                       # 入口函数 (main)
├── Robot.h / Robot.cpp                   # 机器人基类
├── RobotWorker.h / RobotWorker.cpp       # 具备任务、指标、优先级的扩展机器人
├── Vitals.h / Vitals.cpp                 # 状态指标（电量、气压、连接强度）
├── PriorityLevel.h                       # 优先级枚举（绿、黄、橙、红）
├── PriorityCalculationStrategy.h/.cpp    # 策略模式 — 按任务计算优先级
├── RobotObserver.h / RobotObserver.cpp   # 观察者模式 — 告警与通知
├── FactoryAlertSystemFacade.h/.cpp       # 工厂告警系统外观
├── ChargerNotificationSystemFacade.h/.cpp# 充电通知系统外观
├── AbstractRobotDatabaseLoader.h         # 抽象加载器接口
├── RobotDatabaseLoader.h/.cpp            # 模拟数据库加载器
├── RobotFileLoader.h/.cpp               # CSV 文件解析器
├── RobotFileLoaderAdapter.h/.cpp         # 文件加载器适配器
├── CompositeRobotLoader.h/.cpp           # 组合加载器
├── RobotManagementSystem.h/.cpp          # 系统协调器与命令行界面
├── robots.txt                            # 机器人数据文件（CSV 格式）
├── Assessment3.vcxproj                   # MSBuild 项目文件
└── Assessment3.sln                       # Visual Studio 解决方案
```

## 数据文件

`robots.txt` 以 CSV 格式存储机器人记录，包含以下字段：

```
identifier,classCode,manufactureDate,task,powerSupply,pneumaticsPressure,connectionStrength
```
