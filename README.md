# 教师信息管理系统 (TeacherInfoManage)

![License](https://img.shields.io/badge/License-MIT-blue.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.13-green.svg)
![Java](https://img.shields.io/badge/Java-1.8-orange.svg)
![MyBatis-Plus](https://img.shields.io/badge/MyBatis--Plus-3.5.2-red.svg)

一个基于Spring Boot的教师信息管理系统，用于管理教师信息、学术成果审核、数据导出等功能。

## 项目概述

本项目是一个完整的教师信息管理平台，支持教师信息的录入、查询、修改；学术成果（论文、项目、软件等）的提交与审核；数据的导入导出等功能。系统实现了基于角色的权限控制，包括超级管理员、学院管理员、教师和审核员等角色。

## 技术栈

- **后端框架**：Spring Boot 2.7.13
- **ORM框架**：MyBatis-Plus 3.5.2
- **数据库**：MySQL
- **缓存**：Redis
- **安全认证**：JWT (jsonwebtoken 0.9.1)
- **密码加密**：BCrypt (Spring Security)
- **文档工具**：Swagger 2.9.2
- **工具库**：
  - Hutool 5.8.21
  - fastjson2 2.0.23
  - Lombok 1.18.10
- **文件处理**：
  - POI-OOXML 5.2.3 (Excel)
  - POI-TL 1.12.1 (Word)

## 系统功能

1. **用户管理**
   - 多角色系统：超级管理员、学院管理员、教师用户、审核员
   - 基于JWT的身份验证
   - 基于角色的权限控制

2. **教师信息管理**
   - 基础信息维护
   - 学院、教研室管理
   - 数据导入导出

3. **审核系统**
   - 学术论文审核
   - 教育经历审核
   - 荣誉奖励审核
   - 项目审核
   - 软件著作权审核
   - 课题审核
   - 工作经历审核
   - 身份证审核

4. **数据导入导出**
   - 模板化导入导出
   - 多格式支持
   - 批量操作

5. **文件管理**
   - 文件上传、下载
   - 文件重命名、删除

## 文件导入导出功能

系统提供了强大的数据导入导出功能，支持Excel和Word格式，便于信息管理和共享。

### Excel导入导出

#### 导入功能
- **模板下载**：系统提供了标准模板，管理员可下载Excel模板进行批量导入
- **角色适配**：根据不同角色（超级管理员/学院管理员）提供不同的导入模板
- **数据校验**：导入时自动校验数据格式、手机号唯一性、工位号唯一性等
- **字段映射**：支持智能字段映射，如"手机号"→"phone"、"教师姓名"→"teacherName"等
- **批量处理**：一次性导入多条教师信息，提高工作效率

#### 导出功能
- **灵活筛选**：支持按教师ID、教研室ID进行筛选导出
- **字段选择**：可选择性导出指定字段，如ID、姓名、账号、性别等
- **权限控制**：基于角色的导出权限控制
- **自定义文件名**：可自定义导出文件名称

### Word导出

- **个人信息报表**：支持将教师个人信息导出为Word文档
- **模板渲染**：基于POI-TL实现的模板渲染技术
- **自动文件命名**：以教师姓名自动命名导出文件

### 批量导出压缩包

- **多种格式组合**：支持Word、Excel和附件的组合导出
- **批量处理**：一次性打包多个教师的信息
- **类型选择**：
  - type=0：仅导出Word文档
  - type=1：导出Word文档+附件
  - type=2：导出Word文档+附件+Excel表格
- **临时文件管理**：自动创建和清理临时文件，避免占用存储空间

### 模板文件

系统内置了多种模板文件，位于`resources/template/`目录下：
- `TemplateTeacherInfo.docx`：教师信息Word模板
- `SuperAdminImport.xlsx`：超级管理员用的Excel导入模板
- `AdminImport.xlsx`：学院管理员用的Excel导入模板

## 系统结构

```
com.aih
├── common         // 通用组件
│   ├── aop_log    // AOP日志
│   ├── exception  // 异常处理
│   ├── interceptor// 拦截器
│   └── meta       // 元数据处理
├── config         // 配置类
├── controller     // 控制器
│   └── audit      // 审核相关控制器
├── entity         // 实体类
│   ├── audit      // 审核相关实体
│   ├── dto        // 数据传输对象
│   └── vo         // 视图对象
├── mapper         // MyBatis映射接口
├── service        // 服务接口
│   └── impl       // 服务实现类
└── utils          // 工具类
    ├── excel      // Excel处理
    ├── http       // HTTP工具
    ├── ip         // IP处理
    ├── jwt        // JWT工具
    └── vo         // 值对象
```

## 安装与运行

### 环境要求

- JDK 1.8
- MySQL 数据库
- Maven 3.x

### 配置说明

1. 数据库配置在 `application-dev.yml` 中：

```yaml
spring:
  datasource:
    username: root
    password: 123456
    url: jdbc:mysql://localhost:3306/teacher_info_manage_db?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC
```

2. 文件存储路径配置：

```yaml
file:
  root-path: D:\idea\2023\files   # 根路径
  temporary-path: D:\idea\2023\files\temporary  # 临时目录
  template-file-word: template/TemplateTeacherInfo.docx  # word模板文件
  template-file-excel-superadmin: template/SuperAdminImport.xlsx # excel模板文件
  template-file-excel-admin: template/AdminImport.xlsx # excel模板文件
```

### 部署步骤

1. 克隆项目到本地
2. 在MySQL中创建数据库并导入`teacher_info_manage_db.sql`文件
3. 修改`application-dev.yml`中的数据库连接信息和文件存储路径
4. 执行以下命令构建项目：

```bash
mvn clean package
```

5. 运行生成的jar包：

```bash
java -jar target/TeacherInfoManage-0.0.1-SNAPSHOT.jar
```

### 默认端口

应用默认运行在 `9998` 端口


## 项目特点

1. 基于JWT的身份验证和授权
2. 全局异常处理
3. AOP日志记录
4. 基于角色的权限控制
5. 多环境配置支持(dev/pro)
6. 文件上传与管理
7. 数据导入导出(Excel/Word)
