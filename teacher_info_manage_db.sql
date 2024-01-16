/*
 Navicat Premium Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 80019
 Source Host           : localhost:3306
 Source Schema         : teacher_info_manage_db

 Target Server Type    : MySQL
 Target Server Version : 80019
 File Encoding         : 65001

 Date: 16/01/2024 14:54:44
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for academic_paper_audit
-- ----------------------------
DROP TABLE IF EXISTS `academic_paper_audit`;
CREATE TABLE `academic_paper_audit`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '论文id',
  `tid` bigint NULL DEFAULT NULL COMMENT '所属教师id(创建人)',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '论文名称',
  `publish_journal` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发表期刊',
  `publish_date` date NULL DEFAULT NULL COMMENT '发表日期',
  `doi` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'doi号',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附件路径',
  `audit_status` int NULL DEFAULT NULL COMMENT '审核状态 0:待审核 1:通过 2:驳回',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `audit_time` datetime NULL DEFAULT NULL COMMENT '审核时间',
  `aid` bigint NULL DEFAULT NULL COMMENT '审核员id',
  `teacher_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '教师备注',
  `auditor_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核员备注',
  `is_show` int NULL DEFAULT NULL COMMENT '是否展示 0:不展示 1:展示',
  `delete_roles` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除角色',
  `deleted` int NULL DEFAULT 0 COMMENT '逻辑删除 0:未删除 1:删除',
  `academic_paper_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '论文类型',
  `test2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test2',
  `test3` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test3',
  `tset4` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test4',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '论文审核' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of academic_paper_audit
-- ----------------------------
INSERT INTO `academic_paper_audit` VALUES (1, 100001, '<论文名称1>', '<发表期刊1>', '2023-07-15', 'doidoidoidoidoi', NULL, 0, '2023-07-15 13:42:01', NULL, NULL, NULL, NULL, 1, NULL, 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (2, 100001, '<论文名称2>', '<发表期刊2>', '2023-07-15', 'doidoidoidoidoi', NULL, 1, '2023-07-15 13:43:11', NULL, NULL, NULL, NULL, 1, NULL, 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (3, 100001, '<论文3>', '<发表期刊3>', '2023-06-15', 'doidoidoidoidoi', NULL, 0, '2023-07-15 23:42:47', NULL, NULL, '请通过我的论文审核', NULL, 0, NULL, 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (4, 100001, 'consequat', 'aliquip', '2006-05-02', 'aliqua adipisicing elit labore Duis', NULL, 1, '2023-07-16 15:20:27', '2023-07-16 16:24:25', 200001, 'ea minim', '看了两眼,审核通过!!', 1, NULL, 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (5, 100001, 'Lorem ad', NULL, '2004-05-18', 'nulla in', NULL, 0, '2023-04-05 17:19:26', NULL, NULL, NULL, 'non ipsum veniam', 0, ',', 0, 'sunt aute incididunt', NULL, 'culpa nulla consequat aliquip', 'dolor');
INSERT INTO `academic_paper_audit` VALUES (6, 100001, 'aaaa', NULL, '2004-05-18', 'nulla in', NULL, 0, '2023-09-13 19:39:15', NULL, NULL, NULL, NULL, 0, ',', 0, 'sunt aute incididunt', NULL, 'culpa nulla consequat aliquip', 'dolor');
INSERT INTO `academic_paper_audit` VALUES (7, 100003, 'Lorem ad', NULL, '2004-05-18', 'nulla in', NULL, 0, '2023-09-13 17:19:00', NULL, NULL, NULL, 'non ipsum veniam', 0, ',', 0, 'sunt aute incididunt', NULL, 'culpa nulla consequat aliquip', 'dolor');
INSERT INTO `academic_paper_audit` VALUES (8, 100002, 'proident nostrud', NULL, '1981-09-13', NULL, NULL, 1, '2023-09-21 15:58:01', '2023-12-18 17:10:46', 100001, 'in', '', 1, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (9, 100001, '审核111', 'hhhh', '2023-11-13', '123', NULL, 0, '2023-12-25 15:10:37', NULL, NULL, 'dolore pariatur occaecat consectetur velit', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '管理员id',
  `admin_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '管理员名称',
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录账号',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录密码',
  `cid` bigint NULL DEFAULT NULL COMMENT '所属学院id',
  `create_date` date NULL DEFAULT NULL COMMENT '生效日期',
  `status` int NULL DEFAULT NULL COMMENT '是否启用 1:启用 0:禁用',
  `deleted` int NULL DEFAULT 0 COMMENT '逻辑删除 0:未删除 1:删除',
  `test1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test1',
  `test2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test2',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 200006 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '管理员' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES (200001, '小天管理员', 'xiaotian', '$2a$10$KU/Ik6IQvxji25h9I14b7OZfVBw58DmY9tLhCaBta9sxHPz9aT.Tu', 1, '2023-05-30', 1, 0, NULL, NULL);
INSERT INTO `admin` VALUES (200002, '小天管理员111', 'xiaotian111', '$2a$10$dbI87E1PpKxLVsU.abe11.1f9MdDyZhEe92HD8YuFNk2yz9kyPmWu', 1, '2023-06-01', 1, 0, NULL, NULL);
INSERT INTO `admin` VALUES (200004, '学院2管理员', 'xiaotian2', '$2a$10$qfu3RJoXBVF9duIExlTDre2xup7HIeWjWyg7OZEJNI4/.ynwG2/6.', 2, '2023-12-20', 1, 0, NULL, NULL);

-- ----------------------------
-- Table structure for college
-- ----------------------------
DROP TABLE IF EXISTS `college`;
CREATE TABLE `college`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '学院id',
  `college_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '学院名称',
  `deleted` int NULL DEFAULT 0 COMMENT '逻辑删除 0:未删除 1:删除',
  `test1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备用1',
  `test2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备用2',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '学院' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of college
-- ----------------------------
INSERT INTO `college` VALUES (1, '信息工程学院', 0, NULL, NULL);
INSERT INTO `college` VALUES (2, '电气工程学院', 0, NULL, NULL);
INSERT INTO `college` VALUES (3, '机械学院', 0, NULL, NULL);
INSERT INTO `college` VALUES (5, '无', 0, NULL, NULL);

-- ----------------------------
-- Table structure for education_experience_audit
-- ----------------------------
DROP TABLE IF EXISTS `education_experience_audit`;
CREATE TABLE `education_experience_audit`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '教育经历审核id',
  `tid` bigint NULL DEFAULT NULL COMMENT '所属教师id(创建人)',
  `sta_date` date NULL DEFAULT NULL COMMENT '开始日期',
  `end_date` date NULL DEFAULT NULL COMMENT '结束日期',
  `school` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '学校',
  `major` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '专业',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附件路径',
  `audit_status` int NULL DEFAULT NULL COMMENT '审核状态 0:待审核 1:通过 2:驳回',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `audit_time` datetime NULL DEFAULT NULL COMMENT '审核时间',
  `aid` bigint NULL DEFAULT NULL COMMENT '审核员id',
  `teacher_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '教师备注',
  `auditor_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核员备注',
  `is_show` int NULL DEFAULT NULL COMMENT '是否展示 0:不展示 1:展示',
  `delete_roles` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除角色',
  `deleted` int NULL DEFAULT 0 COMMENT '逻辑删除 0:未删除 1:删除',
  `test1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test1',
  `test2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test2',
  `test3` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test3',
  `test4` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test4',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '教育经历审核' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of education_experience_audit
-- ----------------------------
INSERT INTO `education_experience_audit` VALUES (1, 100001, '2023-06-01', '2023-06-03', '湖工院', '软件技术', NULL, 1, '2023-07-15 13:46:54', NULL, NULL, NULL, NULL, 1, NULL, 0, NULL, NULL, NULL, NULL);
INSERT INTO `education_experience_audit` VALUES (2, 100001, '2009-02-09', '1975-08-22', 'dolore fugiat', 'ut', NULL, 0, '2024-01-03 19:36:25', NULL, NULL, 'please', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for honorary_award_audit
-- ----------------------------
DROP TABLE IF EXISTS `honorary_award_audit`;
CREATE TABLE `honorary_award_audit`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '荣誉奖项id',
  `tid` bigint NULL DEFAULT NULL COMMENT '所属教师id(创建人)',
  `get_date` date NULL DEFAULT NULL COMMENT '获奖时间',
  `type` int NULL DEFAULT NULL COMMENT '类型 0:团队 1:个人',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '内容',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附件路径 ',
  `audit_status` int NULL DEFAULT NULL COMMENT '审核状态 0:待审核 1:通过 2:驳回',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `audit_time` datetime NULL DEFAULT NULL COMMENT '审核时间',
  `aid` bigint NULL DEFAULT NULL COMMENT '审核员id',
  `teacher_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '教师备注',
  `auditor_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核员备注',
  `is_show` int NULL DEFAULT NULL COMMENT '是否展示 0:不展示 1:展示',
  `delete_roles` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除角色',
  `deleted` int NULL DEFAULT 0 COMMENT '逻辑删除 0:未删除 1:删除',
  `test1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test1',
  `test2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test2',
  `test3` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test3',
  `test4` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test4',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '荣誉奖项审核' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of honorary_award_audit
-- ----------------------------
INSERT INTO `honorary_award_audit` VALUES (1, 100001, '2023-07-01', 1, '三好学生', NULL, 1, '2023-07-15 13:48:24', NULL, NULL, NULL, NULL, 1, NULL, 0, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for office
-- ----------------------------
DROP TABLE IF EXISTS `office`;
CREATE TABLE `office`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '教研室id',
  `office_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '教研室名称',
  `cid` bigint NULL DEFAULT NULL COMMENT '所属学院id',
  `deleted` int NULL DEFAULT 0 COMMENT '逻辑删除 0:未删除 1:删除',
  `test1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test1',
  `test2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test2',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '教研室' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of office
-- ----------------------------
INSERT INTO `office` VALUES (1, '软件教研室', 1, 0, NULL, NULL);
INSERT INTO `office` VALUES (2, '网络教研室', 1, 0, NULL, NULL);
INSERT INTO `office` VALUES (3, '电气教研室1', 2, 0, NULL, NULL);
INSERT INTO `office` VALUES (4, '电气教研室2', 2, 0, NULL, NULL);
INSERT INTO `office` VALUES (5, '机械办公室A', 3, 0, NULL, NULL);
INSERT INTO `office` VALUES (6, '测试添加电气的某教研室', 2, 0, NULL, NULL);
INSERT INTO `office` VALUES (8, '无', 0, 0, NULL, NULL);

-- ----------------------------
-- Table structure for project_audit
-- ----------------------------
DROP TABLE IF EXISTS `project_audit`;
CREATE TABLE `project_audit`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '项目id',
  `tid` bigint NULL DEFAULT NULL COMMENT '所属教师id(创建人)',
  `project_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '项目名称',
  `completion_date` date NULL DEFAULT NULL COMMENT '完成发布日期',
  `team_size` int NULL DEFAULT NULL COMMENT '团队人数',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附件路径',
  `audit_status` int NULL DEFAULT NULL COMMENT '审核状态 0:待审核 1:通过 2:驳回',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `audit_time` datetime NULL DEFAULT NULL COMMENT '审核时间',
  `aid` bigint NULL DEFAULT NULL COMMENT '审核员id',
  `teacher_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '教师备注',
  `auditor_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核员备注',
  `is_show` int NULL DEFAULT NULL COMMENT '是否展示 0:不展示 1:展示',
  `delete_roles` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除角色',
  `deleted` int NULL DEFAULT 0 COMMENT '逻辑删除 0:未删除 1:删除',
  `project_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '项目类型',
  `test2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test2',
  `test3` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test3',
  `tset4` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test4',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '项目审核' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of project_audit
-- ----------------------------
INSERT INTO `project_audit` VALUES (1, 100001, '<项目名称>', '2023-06-01', 3, NULL, 1, '2023-07-15 13:50:48', NULL, NULL, NULL, NULL, 1, NULL, 0, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for request_college_change
-- ----------------------------
DROP TABLE IF EXISTS `request_college_change`;
CREATE TABLE `request_college_change`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '转学院申请id',
  `tid` bigint NULL DEFAULT NULL COMMENT '教师id',
  `old_aid` bigint NULL DEFAULT NULL COMMENT '原管理员id',
  `old_cid` bigint NULL DEFAULT NULL COMMENT '原学院id',
  `new_aid` bigint NULL DEFAULT NULL COMMENT '新管理员id',
  `new_cid` bigint NULL DEFAULT NULL COMMENT '新学院id',
  `old_admin_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '原管理员备注',
  `new_admin_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '新管理员备注',
  `create_time` datetime NULL DEFAULT NULL COMMENT '申请时间',
  `audit_time` datetime NULL DEFAULT NULL COMMENT '审核时间',
  `audit_status` int NULL DEFAULT NULL COMMENT '申请状态 0待审核 1通过 2未通过',
  `delete_roles` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '删除角色',
  `deleted` int NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '变更学院申请' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of request_college_change
-- ----------------------------
INSERT INTO `request_college_change` VALUES (1, 100001, 200001, 1, 200004, 2, '经讨论转移到2学院', 'ok', '2023-12-20 16:57:30', '2023-12-20 17:12:16', 1, ',', 0);

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '职务id',
  `role_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '职务名称',
  `test1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test1',
  `test2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test2',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '职务' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (1, '教师', NULL, NULL);
INSERT INTO `role` VALUES (2, '辅导员', NULL, NULL);
INSERT INTO `role` VALUES (3, '副院长', NULL, NULL);
INSERT INTO `role` VALUES (4, '院长', NULL, NULL);

-- ----------------------------
-- Table structure for software_audit
-- ----------------------------
DROP TABLE IF EXISTS `software_audit`;
CREATE TABLE `software_audit`  (
  `id` bigint(20) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT COMMENT '软件著作id',
  `tid` bigint NULL DEFAULT NULL COMMENT '所属教师id(创建人)',
  `software_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '软件名称',
  `stage` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '软著阶段',
  `status` int NULL DEFAULT NULL COMMENT '软著状态',
  `completion_date` date NULL DEFAULT NULL COMMENT '完成/发布日期',
  `team_size` int NULL DEFAULT NULL COMMENT '团队人数',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附件路径',
  `audit_status` int NULL DEFAULT NULL COMMENT '审核状态 0:待审核 1:通过 2:驳回',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `audit_time` datetime NULL DEFAULT NULL COMMENT '审核时间',
  `aid` bigint NULL DEFAULT NULL COMMENT '审核员id',
  `teacher_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '教师备注',
  `auditor_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核员备注',
  `is_show` int NULL DEFAULT NULL COMMENT '是否展示 0:不展示 1:展示',
  `delete_roles` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除角色',
  `deleted` int NULL DEFAULT 0 COMMENT '逻辑删除 0:未删除 1:删除',
  `test3` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test3',
  `tset4` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test4',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '软件著作审核' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of software_audit
-- ----------------------------
INSERT INTO `software_audit` VALUES (00000000000000000001, 100001, '<软件著作名称>', NULL, NULL, '2023-06-28', 3, NULL, 1, NULL, NULL, NULL, NULL, NULL, 1, NULL, 0, NULL, NULL);

-- ----------------------------
-- Table structure for super_admin
-- ----------------------------
DROP TABLE IF EXISTS `super_admin`;
CREATE TABLE `super_admin`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '超级管理员id',
  `superadmin_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '超级管理员名称',
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录账号',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登陆密码',
  `deleted` int NULL DEFAULT 0 COMMENT '逻辑删除 0:未删除 1:删除',
  `test1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test1',
  `test2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test2',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 300002 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '超级管理员' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of super_admin
-- ----------------------------
INSERT INTO `super_admin` VALUES (300001, '小天超级管理员', 'xiaotian', '$2a$10$t6TrefQO2do68Nd3Oy378uS6FbQNUy6Waxywt9cJwFnVyu803W3yu', 0, NULL, NULL);

-- ----------------------------
-- Table structure for teacher
-- ----------------------------
DROP TABLE IF EXISTS `teacher`;
CREATE TABLE `teacher`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '教师id',
  `teacher_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录账号',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录密码',
  `gender` int NULL DEFAULT NULL COMMENT '性别   0:女 1:男',
  `ethnic` enum('汉族','阿昌族','白族','保安族','布朗族','布依族','朝鲜族','达干尔族','傣族','德昂族','东乡族','侗族','独龙族','俄罗斯族',' 鄂伦春人','鄂温克族','高山族','仡佬族','哈尼族','哈萨克族','赫哲族','回族','基诺族','京族','景颇族','柯尔克孜族','拉祜族','黎族','僳僳族','珞巴族','满族','毛南族','门巴族','蒙古族','苗族','仫佬族族','纳西族','怒族','普米族','羌族','撒拉族','畲族','水族','塔塔尔族','塔吉克族','土家族','土族','佤族','维吾尔族','乌孜别克族','锡伯族','瑶族','彝族','裕固族','藏族','壮族') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '\r\n民族',
  `native_place` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '籍贯',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '住址',
  `phone` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电话号码',
  `cid` bigint NULL DEFAULT NULL COMMENT '所属学院id',
  `oid` bigint NULL DEFAULT NULL COMMENT '所属教研室id',
  `education_degree` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文化程度',
  `id_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '身份证号',
  `politics_status` enum('群众','共青团员','预备党员','正式党员','其他') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '政治面貌',
  `is_auditor` int NULL DEFAULT NULL COMMENT '是否审核员 0:不是 1:是',
  `create_date` date NULL DEFAULT NULL COMMENT '权限生效日期',
  `start_date` date NULL DEFAULT NULL COMMENT '入校日期',
  `deleted` int NULL DEFAULT 0 COMMENT '逻辑删除 0:未删除 1:删除',
  `test2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备用2',
  `test3` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备用3',
  `test4` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备用4',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100036 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '教师(用户)' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of teacher
-- ----------------------------
INSERT INTO `teacher` VALUES (100001, 'xiaotian_', 'xiaotian', '$2a$10$tU84XaoShAx4GkOO1v07mO8jjeK6J5TJduqH9gQNbETNxNYp6LkFi', 0, '汉族', '广东', '湖南长沙天心区', '12345678910', 1, 1, '硕士', 'sfz123123123123', '共青团员', 1, '2022-08-12', '2022-01-14', 0, NULL, NULL, NULL);
INSERT INTO `teacher` VALUES (100002, '3333333333', '3老师', '$2a$10$oXJF38ma673Rqod75EwBZeH94pvwRoPdgBapzxuVApFssp8.HRHM2', 1, '汉族', NULL, 'aliquip reprehenderit incididunt adipisicing Duis', '17171474743', 1, 1, NULL, NULL, NULL, 0, '2023-12-12', '2022-01-01', 0, NULL, NULL, NULL);
INSERT INTO `teacher` VALUES (100003, 'xiaotian111', 'xiaotian111', '$2a$10$VXtCuwtbYyWZLV5PqwtD6ObdNfgL4NxsgFfejzah0zYzYGg/yGjRO', 0, NULL, NULL, NULL, NULL, 1, 1, NULL, NULL, NULL, 0, '2023-12-12', '2022-07-14', 0, NULL, NULL, NULL);
INSERT INTO `teacher` VALUES (100006, '小天222', 'xiaotian222', '$2a$10$OLzkSlDk.uEd3B3uAmhLvOSQFbrSYVnWTYyp.nC1yBcrKe7w80IEC', 1, NULL, NULL, NULL, NULL, 2, 2, NULL, NULL, NULL, 0, '2023-12-12', '2011-12-14', 0, NULL, NULL, NULL);
INSERT INTO `teacher` VALUES (100008, '机械老师', '机械老师', '$10$OLzkSlDk.uEd3B3uAmhLvOSQFbrSYVnWTYyp.nC1yBcrKe7w80IEC', 0, '汉族', '广东', 'hunan', '123', 3, 5, '博士', NULL, NULL, 0, '2023-10-01', '2004-12-14', 0, NULL, NULL, NULL);
INSERT INTO `teacher` VALUES (100011, NULL, '15711111111', '$2a$10$Zepk1v1hVGUNvFVW1Q3tsOoVHMMr6EgCYoZCku8U0v7ttg6pUo2sa', NULL, NULL, NULL, NULL, '15711111111', 2, 3, NULL, NULL, NULL, NULL, '2024-01-02', NULL, 0, NULL, NULL, NULL);
INSERT INTO `teacher` VALUES (100012, NULL, '15722222222', '$2a$10$T.Zls/2oXpX2X6uFca97DOZMIev/gRDC7rUrmXUVK4Xu.6JMIgVn6', NULL, NULL, NULL, NULL, '15711111111', 1, 1, NULL, NULL, NULL, NULL, '2024-01-02', NULL, 0, NULL, NULL, NULL);
INSERT INTO `teacher` VALUES (100013, NULL, '15711111111', '$2a$10$50voTmWCu7BiqjvvolWur.enLxtfpw3kVeLKdzyFojjo/i2E7dpm.', NULL, NULL, NULL, NULL, '15711111111', 1, 1, NULL, NULL, NULL, NULL, '2024-01-08', NULL, 0, NULL, NULL, NULL);
INSERT INTO `teacher` VALUES (100014, NULL, '15722222222', '$2a$10$kg5Op5H8CgxMakk6A5led.iUPMs.IFx2qdRSqRV4tCC.Ej2xEFG5y', NULL, NULL, NULL, NULL, '15722222222', 1, 2, NULL, NULL, NULL, NULL, '2024-01-08', NULL, 0, NULL, NULL, NULL);
INSERT INTO `teacher` VALUES (100019, NULL, '15711111111', '$2a$10$XSC/OCXzVXeG.PFQV2DIK.fG.L6H2sCeT1fWoGGDXP2cGJMl5nqai', NULL, NULL, NULL, NULL, '15711111111', 1, 1, NULL, NULL, NULL, NULL, '2024-01-08', NULL, 0, NULL, NULL, NULL);
INSERT INTO `teacher` VALUES (100020, NULL, '15722222222', '$2a$10$LAc3MxBFbD6gs0SX4.h9tuKpBnyvBt.kN5v8n5kV8hqRc1fUWnKCq', NULL, NULL, NULL, NULL, '15722222222', 1, 2, NULL, NULL, NULL, NULL, '2024-01-08', NULL, 0, NULL, NULL, NULL);
INSERT INTO `teacher` VALUES (100021, NULL, '15711111111', '$2a$10$FEBIf5BIQwFbGojfzGSpt.SZbeDrc1jx4GTn6P2i.HWVymqSyW5ji', NULL, NULL, NULL, NULL, '15711111111', 1, 1, NULL, NULL, NULL, NULL, '2024-01-08', NULL, 0, NULL, NULL, NULL);
INSERT INTO `teacher` VALUES (100022, NULL, '15722222222', '$2a$10$jjep3hXgc00XtcgYAC1Ns.XJmGRvCPUADhbvZcYRBfb//nBlfbMLq', NULL, NULL, NULL, NULL, '15722222222', 1, 2, NULL, NULL, NULL, NULL, '2024-01-08', NULL, 0, NULL, NULL, NULL);
INSERT INTO `teacher` VALUES (100023, NULL, '15411111111', '$2a$10$8wOQL746CwtN/wJL98sVZO48UgUoyqdxkx3uayfYkGE3wKLghGfjG', NULL, NULL, NULL, NULL, '15411111111', 1, 1, NULL, NULL, NULL, NULL, '2024-01-08', NULL, 0, NULL, NULL, NULL);
INSERT INTO `teacher` VALUES (100024, NULL, '18722222222', '$2a$10$eFHWgaKVn0av9O3uESS7a.zifckF5QPAa6cS8Xb4fDEJdigilS4f6', NULL, NULL, NULL, NULL, '18722222222', 1, 2, NULL, NULL, NULL, NULL, '2024-01-08', NULL, 0, NULL, NULL, NULL);
INSERT INTO `teacher` VALUES (100025, NULL, '18722222223', '$2a$10$dSLJPILVNw.q7eOMRDhVg.I.ir0MqnlaUHtRJRoWyqlSiwtdaoD..', NULL, NULL, NULL, NULL, '18722222223', 1, NULL, NULL, NULL, NULL, NULL, '2024-01-08', NULL, 0, NULL, NULL, NULL);
INSERT INTO `teacher` VALUES (100026, NULL, '18722222224', '$2a$10$BbCdJeYuogAP1gPCQ73FB.hJcsj8xM8jNu.CJrILQn9elzdaLDkb6', NULL, NULL, NULL, NULL, '18722222224', 1, NULL, NULL, NULL, NULL, NULL, '2024-01-08', NULL, 0, NULL, NULL, NULL);
INSERT INTO `teacher` VALUES (100027, NULL, '18722222225', '$2a$10$bdvPMA9Hfu4c8ingfyUntOzv6NtFMqXFtBkphbuuL9SBn.JLLd7PS', NULL, NULL, NULL, NULL, '18722222225', 1, 1, NULL, NULL, NULL, NULL, '2024-01-08', NULL, 0, NULL, NULL, NULL);
INSERT INTO `teacher` VALUES (100028, NULL, '18722222226', '$2a$10$MQVAE8mVebKfyD/TSsbbluuJZWMROrK52fRAAzYFVBHpWIi6Gyxbe', NULL, NULL, NULL, NULL, '18722222226', 1, 2, NULL, NULL, NULL, NULL, '2024-01-08', NULL, 0, NULL, NULL, NULL);
INSERT INTO `teacher` VALUES (100029, NULL, '18722222227', '$2a$10$fbJSvAKhmT8lR1m3SBQTxukPYjWnVMNQ5JA.i9J60LAw6xBHOAXkC', NULL, NULL, NULL, NULL, '18722222227', 1, NULL, NULL, NULL, NULL, NULL, '2024-01-08', NULL, 0, NULL, NULL, NULL);
INSERT INTO `teacher` VALUES (100030, NULL, '18722222228', '$2a$10$vePhs2ONxHE1PLuyLxZgp.LbP4NLkPgdN4Hlk9aquORAuI6..Kx2C', NULL, NULL, NULL, NULL, '18722222228', 1, NULL, NULL, NULL, NULL, NULL, '2024-01-08', NULL, 0, NULL, NULL, NULL);
INSERT INTO `teacher` VALUES (100031, NULL, '18722222229', '$2a$10$8tRQJeKDplZTZffIHbEpk.d9a2/Vq0T6Obw1JhPL/Np0btsXOI.My', NULL, NULL, NULL, NULL, '18722222229', 1, 1, NULL, NULL, NULL, NULL, '2024-01-08', NULL, 0, NULL, NULL, NULL);
INSERT INTO `teacher` VALUES (100032, NULL, '18722222230', '$2a$10$crL1JUMSiH9SEgPHpxvZz.EMPKRrqbwdYdab1d32zvaj3MU7OmfQm', NULL, NULL, NULL, NULL, '18722222230', 1, NULL, NULL, NULL, NULL, NULL, '2024-01-08', NULL, 0, NULL, NULL, NULL);
INSERT INTO `teacher` VALUES (100033, NULL, '18722222231', '$2a$10$hDrWYC.lz5SAmlsBHfaIA.we4y5yfKots.5g61oq8vuU/RUdjFZu2', NULL, NULL, NULL, NULL, '18722222231', 1, NULL, NULL, NULL, NULL, NULL, '2024-01-08', NULL, 0, NULL, NULL, NULL);
INSERT INTO `teacher` VALUES (100034, NULL, '15711111111', '$2a$10$tBjJpmHV02bBaJR0Grmngeqwp.N6W9IVPdmc7fmrsdvH2TRTBk8Ei', NULL, NULL, NULL, NULL, '15711111111', 2, 3, NULL, NULL, NULL, NULL, '2024-01-08', NULL, 0, NULL, NULL, NULL);
INSERT INTO `teacher` VALUES (100035, NULL, '15722222222', '$2a$10$wLHnUhi7iqx27ir2bvRc9e4V0WhGF3AFv3eBKz8Ao6e5cdf/JLXT2', NULL, NULL, NULL, NULL, '15722222222', 1, 1, NULL, NULL, NULL, NULL, '2024-01-08', NULL, 0, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for teacher_role
-- ----------------------------
DROP TABLE IF EXISTS `teacher_role`;
CREATE TABLE `teacher_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色_职务_关系id',
  `tid` bigint NULL DEFAULT NULL COMMENT '教师id',
  `rid` bigint NULL DEFAULT NULL COMMENT '职务id',
  `deleted` int NULL DEFAULT 0 COMMENT '逻辑删除 0:未删除 1:删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '教师-职务关系表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of teacher_role
-- ----------------------------
INSERT INTO `teacher_role` VALUES (6, 100001, 1, 0);
INSERT INTO `teacher_role` VALUES (7, 100001, 2, 0);
INSERT INTO `teacher_role` VALUES (10, 100002, 1, 0);
INSERT INTO `teacher_role` VALUES (11, 100002, 2, 0);
INSERT INTO `teacher_role` VALUES (13, 100025, 3, 0);

-- ----------------------------
-- Table structure for topic_audit
-- ----------------------------
DROP TABLE IF EXISTS `topic_audit`;
CREATE TABLE `topic_audit`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '课题id',
  `tid` bigint NULL DEFAULT NULL COMMENT '所属教师id(创建人)',
  `topic_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '课题名称',
  `topic_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '课题类型',
  `sta_date` date NULL DEFAULT NULL COMMENT '开始日期',
  `end_date` date NULL DEFAULT NULL COMMENT '结束日期',
  `team_size` int NULL DEFAULT NULL COMMENT '团队人数',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附件路径',
  `audit_status` int NULL DEFAULT NULL COMMENT '审核状态 0:待审核 1:通过 2:驳回',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `audit_time` datetime NULL DEFAULT NULL COMMENT '审核时间',
  `aid` bigint NULL DEFAULT NULL COMMENT '审核员id',
  `teacher_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '教师备注',
  `auditor_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核员备注',
  `is_show` int NULL DEFAULT NULL COMMENT '是否展示 0:不展示 1:展示',
  `delete_roles` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除角色',
  `deleted` int NULL DEFAULT 0 COMMENT '逻辑删除 0:未删除 1:删除',
  `test1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test1',
  `test2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test2',
  `test3` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test3',
  `tset4` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test4',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '课题审核' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of topic_audit
-- ----------------------------
INSERT INTO `topic_audit` VALUES (1, 100001, '<课题名称>', '天文类型', '2023-06-08', '2023-08-08', 1, NULL, 1, '2023-07-15 13:52:32', NULL, NULL, '希望通过审核', '审核通过！', 1, NULL, 0, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for work_experience_audit
-- ----------------------------
DROP TABLE IF EXISTS `work_experience_audit`;
CREATE TABLE `work_experience_audit`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '工作经历审核id',
  `tid` bigint NULL DEFAULT NULL COMMENT '所属教师id(创建人)',
  `sta_date` date NULL DEFAULT NULL COMMENT '开始日期',
  `end_date` date NULL DEFAULT NULL COMMENT '结束日期',
  `company_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单位名称',
  `position` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '职务',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附件路径',
  `audit_status` int NULL DEFAULT NULL COMMENT '审核状态 0:待审核 1:通过 2:驳回',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `audit_time` datetime NULL DEFAULT NULL COMMENT '审核时间',
  `aid` bigint NULL DEFAULT NULL COMMENT '审核员id',
  `teacher_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '教师备注',
  `auditor_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核员备注',
  `is_show` int NULL DEFAULT NULL COMMENT '是否展示 0:不展示 1:展示',
  `delete_roles` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除角色',
  `deleted` int NULL DEFAULT 0 COMMENT '逻辑删除 0:未删除 1:删除',
  `test1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test1',
  `test2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test2',
  `test3` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test3',
  `test4` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test4',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '工作经历审核' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of work_experience_audit
-- ----------------------------
INSERT INTO `work_experience_audit` VALUES (1, 100001, '2023-06-01', '2023-07-01', '湖工院', '教师', NULL, 1, '2023-07-15 13:54:31', NULL, NULL, NULL, NULL, 1, NULL, 0, NULL, NULL, NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;
