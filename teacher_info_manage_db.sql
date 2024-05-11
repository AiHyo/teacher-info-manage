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

 Date: 11/05/2024 10:49:34
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for academic_paper_audit
-- ----------------------------
DROP TABLE IF EXISTS `academic_paper_audit`;
CREATE TABLE `academic_paper_audit`  (
  `zw_id` bigint NOT NULL AUTO_INCREMENT COMMENT '论文id',
  `zw_tid` bigint NULL DEFAULT NULL COMMENT '所属教师id(创建人)',
  `zw_title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '论文名称',
  `zw_publish_journal` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发表期刊',
  `zw_publish_date` date NULL DEFAULT NULL COMMENT '发表日期',
  `zw_doi` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'doi号',
  `zw_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附件路径',
  `zw_audit_status` int NULL DEFAULT NULL COMMENT '审核状态 0:待审核 1:通过 2:驳回',
  `zw_create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `zw_audit_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `zw_aid` bigint NULL DEFAULT NULL COMMENT '审核员id',
  `zw_teacher_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '教师备注',
  `zw_auditor_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核员备注',
  `zw_is_show` int NULL DEFAULT NULL COMMENT '是否展示 0:不展示 1:展示',
  `zw_delete_roles` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除角色',
  `zw_deleted` int NULL DEFAULT 0 COMMENT '逻辑删除 0:未删除 1:删除',
  `zw_academic_paper_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '论文类型',
  `test2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test2',
  `test3` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test3',
  `tset4` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test4',
  PRIMARY KEY (`zw_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 140 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '论文审核' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of academic_paper_audit
-- ----------------------------
INSERT INTO `academic_paper_audit` VALUES (1, 100001, '<论文名称1>', '<发表期刊1>', '2023-07-15', 'doidoidoidoidoi', NULL, 1, '2023-07-15 13:42:01', '2024-05-10 15:49:02', 200006, NULL, '查看了，可以', 1, NULL, 0, '类型1', NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (2, 100001, '<论文名称2>', '<发表期刊2>', '2023-07-15', 'doidoidoidoidoi', NULL, 0, '2023-07-15 13:43:11', '2024-05-10 15:49:08', 200006, NULL, '查看了，可以的', 1, NULL, 0, '类型2', NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (3, 100001, '<论文3>', '<发表期刊3>', '2023-06-15', 'doidoidoidoidoi', NULL, 0, '2023-07-15 23:42:47', NULL, NULL, '请通过我的论文审核', NULL, NULL, NULL, 0, '科研', NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (4, 100001, 'consequat', 'aliquip', '2006-05-02', 'aliqua adipisicing elit labore Duis', NULL, 1, '2023-07-16 15:20:27', '2023-07-16 16:24:25', 3, 'ea minim', '看了两眼,审核通过!!', 0, NULL, 0, '语言', NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (5, 100001, 'Lorem ad', NULL, '2004-05-18', 'nulla in', NULL, 0, '2023-09-13 17:19:26', NULL, NULL, NULL, 'non ipsum veniam', 0, ',', 1, 'sunt aute incididunt', NULL, 'culpa nulla consequat aliquip', 'dolor');
INSERT INTO `academic_paper_audit` VALUES (6, 100001, 'aaaa', NULL, '2004-05-18', 'nulla in', NULL, 0, '2023-09-13 19:39:15', NULL, NULL, NULL, NULL, 0, ',', 1, 'sunt aute incididunt', NULL, 'culpa nulla consequat aliquip', 'dolor');
INSERT INTO `academic_paper_audit` VALUES (7, 100001, '论文5', '发表杂志', '2023-09-27', 'doidoidoi', '', 0, '2023-10-18 16:15:16', NULL, NULL, '管理大人帮我通过一下', NULL, 0, ',', 0, '数学', NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (8, 100002, '论文1', '新华社', '2023-10-26', 'doidoidoi', '', 0, '2023-10-27 15:33:48', NULL, NULL, '妖魔鬼怪快离开，妖魔鬼怪快离开', NULL, 0, ',', 0, '计算机', NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (9, 100002, '论文2', '广播电视台', '2023-10-10', 'doidoidoi', '', 0, '2023-10-27 15:46:52', NULL, NULL, '卧龙凤雏何在', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (10, 100002, '论世界和平', '王大师作品', '2023-10-17', 'doidoidoi', '', 0, '2023-10-27 15:58:29', NULL, NULL, '今天晚上吃什么菜呢', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (11, 100002, '论人类的生活指数', '人类健康生活', '2023-10-10', 'doidoidoi', '666.zip', 0, '2023-10-28 10:20:53', NULL, NULL, '我是时长联系两年半的练习生', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (12, 100002, '科技对人类的重要性', '科学发展', '2023-09-24', 'doidoidoi', '111.docx', 0, '2023-10-28 14:34:23', NULL, NULL, '我国是科技大国', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (13, 100002, '综合国力', '21313213', '2023-10-09', 'doidoidoi', 'http://47.113.150.138:9998/academicPaperAudit/file/download/100002/摆台题目.txt', 0, '2023-10-28 14:38:22', NULL, NULL, '大哥包邮', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (14, 100011, '这是一个测试', '测试1', '2023-12-13', 'doidoidoi', 'http://47.113.150.138:9998/academicPaperAudit/file/download/100011/1701653908765_pic4.png', 1, '2023-12-04 09:38:37', '2023-12-22 19:31:32', 100001, '教师王某', '我通过你的申请了', 1, ',100011,', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (15, 100011, '这也是一个测试', '测试2', '2023-12-07', 'doidoidoi', 'http://47.113.150.138:9998/academicPaperAudit/file/download/100011/1703321978224_pic4.png', 2, '2023-12-23 16:59:42', '2023-12-23 17:05:10', 100001, '123456', '你没提交证明材料', 0, ',100011,', 0, '计算机', NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (16, 100011, '测试11111', '11122', '2023-12-13', 'doidoidoi', 'http://47.113.150.138:9998/academicPaperAudit/file/download/100011/1703469889454_pic4.png', 0, '2023-12-25 10:04:53', NULL, NULL, '123456', NULL, 0, ',', 1, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (17, 100011, '测试11111', '11122', '2023-12-13', 'doidoidoi', 'http://47.113.150.138:9998/academicPaperAudit/file/download/100011/1703469889454_pic4.png', 0, '2023-12-25 10:22:50', NULL, NULL, '123456', NULL, 0, ',', 1, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (18, 100011, '测试11111', '11122', '2023-12-13', 'doidoidoi', 'http://47.113.150.138:9998/academicPaperAudit/file/download/100011/1703469889454_pic4.png', 1, '2023-12-25 10:22:55', '2023-12-26 16:16:11', 100001, '123456', 'hh', 1, ',', 0, '科学', NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (19, 100011, '测试3', '测试5', '2023-12-07', 'doidoidoi', '', 0, '2023-12-26 14:51:00', NULL, NULL, '123464', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (20, 100011, '测试A', '11111', '2023-12-04', 'doidoidoi', '', 0, '2023-12-26 15:00:11', NULL, NULL, '1111', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (21, 100011, '测试555', '123456', '2023-12-06', 'doidoidoi', '', 0, '2023-12-27 21:29:56', NULL, NULL, 'hhhhh', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (22, 100011, 'hhhhhhh', '111111111', '2023-12-06', 'doidoidoi', '', 0, '2023-12-27 21:32:04', NULL, NULL, '111111111111111111111111', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (23, 100011, '11111111111111111', '11111111111111111111', '2023-12-14', 'doidoidoi', '', 0, '2023-12-27 21:33:42', NULL, NULL, '11111111111111111111111', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (24, 100011, '222222222222222222222222', '222222222222222', '2023-12-13', 'doidoidoi', '', 0, '2023-12-27 21:34:51', NULL, NULL, '2222222222222222222', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (25, 100011, '77777777777', '77777777', '2023-12-12', 'doidoidoi', '', 0, '2023-12-27 21:43:58', NULL, NULL, '77777777', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (26, 100011, '111111111111', '11111111111111111111', '2023-12-06', 'doidoidoi', '', 0, '2023-12-27 21:44:35', NULL, NULL, '111111111111111', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (27, 100011, '444444444444444', '4444444444444', '2023-12-06', 'doidoidoi', '', 0, '2023-12-27 21:52:36', NULL, NULL, '44444444444444444444', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (28, 100011, '11111', '111111111111111', '2023-11-29', 'doidoidoi', '', 0, '2023-12-28 09:46:36', NULL, NULL, '11111111111111111', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (29, 100011, '11111111111111111111', '1111111111111111111111', '2023-12-07', 'doidoidoi', '', 0, '2023-12-28 09:47:18', NULL, NULL, '111111111111111111111111111111111', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (30, 100011, '111111', '11111', '2023-12-14', 'doidoidoi', '', 0, '2023-12-28 09:50:40', NULL, NULL, '111111', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (31, 100011, '1111111111111111111', '111111111111111', '2023-12-13', 'doidoidoi', '', 0, '2023-12-28 09:51:16', NULL, NULL, '11111111111111', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (32, 100011, '1111111111111111', '11111111111111111111', '2023-12-06', 'doidoidoi', '', 1, '2023-12-28 09:51:44', '2024-04-30 00:42:43', 100001, '1111111111111', 'test', 1, ',', 0, '科学', NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (33, 100011, '111111111', '11111111111111111111', '2023-11-29', 'doidoidoi', '', 0, '2023-12-28 09:59:00', NULL, NULL, '1111111111111111111111', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (34, 100011, '1111', '111111111111111111111', '2023-12-06', 'doidoidoi', '', 0, '2023-12-28 10:00:16', NULL, NULL, '1111111111111111111', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (35, 100011, '1111111', '111111111111111111111', '2023-11-29', 'doidoidoi', '', 0, '2023-12-28 10:02:46', NULL, NULL, '1111111111111111111', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (36, 100011, '1', '1111111111111111', '2023-12-06', 'doidoidoi', '', 0, '2023-12-28 10:06:32', NULL, NULL, '1111111111', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (37, 100011, '111111111111', '1111111111111111111', '2023-12-20', 'doidoidoi', '', 0, '2023-12-28 18:51:13', NULL, NULL, '11111111111111111', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (38, 100011, '11', '1111', '2023-12-20', 'doidoidoi', '', 0, '2023-12-28 18:52:20', NULL, NULL, '11111111111111', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (39, 100011, '1111111111111', '111111111111111', '2023-12-14', 'doidoidoi', '', 0, '2023-12-28 18:56:25', NULL, NULL, '11111111111111111111', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (40, 100011, '1111111', '111111111111111111111111', '2023-12-08', 'doidoidoi', '', 0, '2023-12-28 19:00:59', NULL, NULL, '11111111111111', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (41, 100011, '11111111111', '1111111111111111111111', '2023-12-07', 'doidoidoi', '', 0, '2023-12-28 19:01:30', NULL, NULL, '111111111111111111', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (42, 100011, '11111111111', '111111111111111111', '2023-12-07', 'doidoidoi', '', 0, '2023-12-28 19:03:27', NULL, NULL, '111111111111111111111', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (43, 100011, '111111', '111111111111111111', '2023-12-14', 'doidoidoi', '', 0, '2023-12-28 19:06:16', NULL, NULL, '1111111111111111111111', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (44, 100011, '111111111111111111111111', '1111111111111111111', '2023-12-07', 'doidoidoi', '', 0, '2023-12-28 19:06:48', NULL, NULL, '11111111111111111111111', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (45, 100011, '111111', '1111111111111111111', '2023-12-15', 'doidoidoi', '', 0, '2023-12-28 19:07:58', NULL, NULL, '1111111111111111', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (46, 100011, '111111', '11111111111111111111', '2023-12-13', 'doidoidoi', '', 0, '2023-12-28 19:09:07', NULL, NULL, '1111111111111', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (47, 100011, '11111222', '2222222222222222222222', '2023-12-20', 'doidoidoidoidoi', '', 0, '2023-12-28 19:14:44', NULL, NULL, '2222222222', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (48, 100011, '111', '1111111111111111111', '2023-12-06', 'doidoidoidoidoi', '', 0, '2023-12-28 19:23:35', NULL, NULL, '1111111111111111111', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (49, 100011, '1111111111111', '11111111111111111', '2023-12-13', 'doidoidoidoidoi', '', 0, '2023-12-28 19:28:46', NULL, NULL, '111111111111', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (50, 100011, '11111111111', '11111111111111111', '2023-12-13', 'doidoidoidoidoi', '', 0, '2023-12-28 19:58:52', NULL, NULL, '1111111111111111111111111', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (51, 100011, '11111111111111111111111', '111111111111111111', '2024-01-18', 'doidoidoidoidoi', '', 0, '2024-01-02 15:19:02', NULL, NULL, '111111111111111111111111111', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (52, 100011, '1111111111111', '111111111111111111', '2024-01-30', 'doidoidoidoidoi', '', 0, '2024-01-02 15:20:27', NULL, NULL, '111111111111111111111111111', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (53, 100011, '111111111111111', '111111111111111', '2024-01-18', 'doidoidoidoidoi', '', 0, '2024-01-02 15:21:18', NULL, NULL, '11111111111111111111111111111111111', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (54, 100011, '111', '111111111111111111111', '2024-01-09', 'doidoidoi', '', 0, '2024-01-02 15:22:13', NULL, NULL, '111111111111111111111111', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (55, 100011, '111111111111', '11111111111111111111', '2024-01-17', 'doidoidoidoidoi', '', 0, '2024-01-02 15:31:14', NULL, NULL, '111111111111111', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (56, 100011, '1111111111111111111111111111', '11111111111111111111', '2024-01-09', 'doidoidoidoidoi', '', 0, '2024-01-02 15:32:02', NULL, NULL, '11111111111111111111111111111111', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (57, 100011, '11111111111', '1111111111111111', '2024-01-16', 'doidoidoidoidoi', '', 0, '2024-01-02 15:32:34', NULL, NULL, '1111111111111', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (58, 100011, '1111', '1111111111111111111111', '2024-01-15', 'doidoidoidoidoi', '', 0, '2024-01-03 19:55:00', NULL, NULL, '111111111111111111111', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (59, 100011, '1', '111111111111111111111', '2024-01-03', 'doidoidoidoidoi', '', 0, '2024-01-03 20:00:15', NULL, NULL, '111111111111111111', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (60, 100011, '论文', '1111', '2024-01-03', 'doidoidoidoidoi', '', 0, '2024-01-04 16:11:50', NULL, NULL, '11111111111111', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (61, 100011, '撒大大撒大苏打', '撒大苏打的是', '2024-01-08', 'doidoidoidoidoi', '', 0, '2024-01-04 16:14:19', NULL, NULL, '撒大苏打撒旦', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (62, 100011, '11111112', '3123213123213', '2024-01-03', 'doidoidoidoidoi', '', 0, '2024-01-04 16:17:29', NULL, NULL, '123123213123123', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (63, 100011, '12312312312321', '3123123123123123', '2024-01-17', 'doidoidoidoidoi', '', 0, '2024-01-04 16:18:37', NULL, NULL, '213123213123123123', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (64, 100011, '12321212121212121213', '333333333333333333', '2024-01-16', 'doidoidoidoidoi', '', 0, '2024-01-04 16:20:41', NULL, NULL, '112333333333333333333333', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (65, 100011, '撒大苏打撒旦', '啊实打实大苏打', '2024-01-16', 'doidoidoidoidoi', '', 0, '2024-01-04 16:22:11', NULL, NULL, '阿萨大大撒旦', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (66, 100011, '阿四实打实大苏打撒旦', '阿斯达撒大苏打撒旦', '2024-01-08', 'doidoidoidoidoi', '', 0, '2024-01-04 16:23:01', NULL, NULL, '阿萨大大撒旦', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (67, 100011, '是顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶', '顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶', '2024-01-17', 'doidoidoidoidoi', '', 0, '2024-01-04 16:25:51', NULL, NULL, '顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (68, 100011, '说的都是', '说的是对的', '2024-01-17', 'doidoidoidoidoi', '', 0, '2024-01-04 16:26:54', NULL, NULL, '撒大苏打', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (69, 100011, '撒大大', '阿松大', '2024-01-10', 'doidoidoidoidoi', '', 0, '2024-01-04 16:31:44', NULL, NULL, '萨达', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (70, 100011, '杀杀杀', '杀杀杀', '2024-01-09', 'doidoidoidoidoi', '', 0, '2024-01-04 16:33:09', NULL, NULL, '杀杀杀', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (71, 100001, 'zzzz', 'zzz', '2024-01-08', 'doidoidoidoidoi', '', 0, '2024-01-04 16:33:57', NULL, NULL, 'zzzz', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (72, 100001, '65165465', '1354654', '2024-01-02', 'doidoidoidoidoi', '', 0, '2024-01-04 16:36:46', NULL, NULL, '15654', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (73, 100001, '1321654', '1111111111', '2024-01-02', 'doidoidoidoidoi', '', 0, '2024-01-04 16:38:04', NULL, NULL, '11111111111111', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (74, 100011, '嗷嗷嗷s', 'sssss', '2024-01-09', 'doidoidoidoidoi', '', 0, '2024-01-04 17:13:49', NULL, NULL, '', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (75, 100011, '123123123', '12312312', '2024-01-09', 'doidoidoidoidoi', '', 0, '2024-01-04 17:16:04', NULL, NULL, '123123123', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (76, 100011, '1231312', '3123123', '2024-01-10', 'doidoidoidoidoi', '', 0, '2024-01-04 17:17:04', NULL, NULL, '123123213', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (77, 100011, 'aaaaa', 'aaaa', '2024-01-09', 'doidoidoidoidoi', '', 0, '2024-01-04 17:24:16', NULL, NULL, 'aaaaaaa', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (78, 100011, 'ccccc', 'ccc', '2024-01-08', 'doidoidoidoidoi', '', 0, '2024-01-04 17:26:34', NULL, NULL, 'cccc', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (79, 100011, 'sds', 'sd', '2024-01-03', 'doidoidoidoidoi', '', 0, '2024-01-04 17:28:18', NULL, NULL, 'sdsd', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (80, 100011, '111111111111111', '111111111111111111', '2024-01-24', 'doidoidoidoidoi', '', 0, '2024-01-04 20:19:21', NULL, NULL, '111111111111111111111', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (81, 100011, '123123123', '2131231', '2024-01-09', 'doidoidoidoidoi', '', 0, '2024-01-04 20:21:35', NULL, NULL, '12312312', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (82, 100011, '11111111111', '1111111111111111', '2024-01-09', 'doidoidoidoidoi', '', 0, '2024-01-04 20:24:06', NULL, NULL, '11111111111111', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (83, 100011, '123123123', '123123', '2024-01-01', 'doidoidoidoidoi', '', 0, '2024-01-04 20:25:11', NULL, NULL, '123123123', NULL, 0, ',', 0, '语文', NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (84, 100011, 'sadasd', 'sadad', '2024-01-16', 'doidoidoidoidoi', '', 0, '2024-01-04 20:26:13', NULL, NULL, 'asdasd', NULL, 0, ',', 0, '数学', NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (85, 100011, 'aaaa', 'aaaa', '2024-01-09', 'doidoidoidpo', '', 0, '2024-01-04 20:28:05', NULL, NULL, 'aaaaa', NULL, 0, ',', 0, 'mysql', NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (86, 100011, 'bvbbb', 'bbb', '2024-01-09', 'oidoidoi', '', 0, '2024-01-04 20:29:27', NULL, NULL, 'bbbb', NULL, 0, ',', 0, 'mybatis', NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (87, 100001, '111111111111111', '1111111111111111111', '2024-01-01', 'doidoidoi', '', 1, '2024-01-05 15:40:03', '2024-04-29 10:02:45', 200006, '11111111111111111111', 'test', 1, ',200006,', 0, 'spring', NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (88, 100001, '222222222222222222', '111111111111111', '1111-11-10', 'doidoidoidoidoi', '', 0, '2024-01-05 15:41:21', NULL, NULL, '1111111111111111', NULL, 0, ',', 0, 'spring', NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (89, 100001, 'zsdasdas', 'asdasda', '2024-01-16', 'doidoidoidoidoi', '', 0, '2024-01-05 15:44:06', NULL, NULL, 'asdasdasd', NULL, 0, ',', 0, 'spring', NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (90, 100001, 'asdasdasd', 'asdasdasdasd', '2024-01-03', 'doidoidoidoidoi', '', 0, '2024-01-05 15:46:48', NULL, NULL, 'asdadasd', NULL, 0, ',', 0, 'spring', NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (91, 100001, 'bbbbbbbbb', 'bbbbbbbbbbbbbbbb', '2024-01-09', 'doidoidoidoidoi', '', 0, '2024-01-05 15:51:00', NULL, NULL, 'bbbbbbbbbbbbbbbb', NULL, 0, ',', 0, '计算机', NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (92, 100001, 'cccccccccc', 'cccccccccccccccccccc', '2024-01-16', 'doidoidoidoidoi', '', 0, '2024-01-05 15:51:20', NULL, NULL, 'cccccccccccccccc', NULL, 0, ',', 0, '数学', NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (93, 100001, 'wwwww', 'wwwwwwwwwwwwwwwww', '2024-01-09', 'doidoidoidoidoi', '', 0, '2024-01-05 15:56:40', NULL, NULL, 'wwwwwwwwwww', NULL, 0, ',', 0, '英语', NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (94, 100001, 'qqqqqq', 'qqqqqqqqqqqqqqqq', '2024-01-01', 'doidoidoidoidoi', '', 0, '2024-01-05 15:58:35', NULL, NULL, 'qqqqqqqqqqqqqqq', NULL, 0, ',', 0, '英语', NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (95, 100001, '11111111', '11111111111111111111', '2024-01-17', 'doidoidoidoidoi', '', 0, '2024-01-05 16:00:33', NULL, NULL, '111111111111111', NULL, 0, ',', 0, '英语', NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (96, 100001, '11111111111', '11111111111111', '2024-01-10', 'doidoidoidoidoi', '', 0, '2024-01-05 16:01:32', NULL, NULL, 'asdadas', NULL, 0, ',', 1, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (97, 100011, '1111', 'qqqqq', '2024-01-09', 'doidoidoidoidoi', '', 0, '2024-01-05 16:34:16', NULL, NULL, 'qqqq', NULL, 0, ',', 0, '语文', NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (98, 100011, '1111', '1111111111111111111111', '2023-12-06', 'doidoidoidoidoi', '', 1, '2024-01-05 16:37:18', '2024-01-08 21:56:14', 100001, '1111111111111111111', '123', 0, ',', 0, '数学', NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (99, 100011, 'sdasdasd', 'sadasda', '2024-01-09', 'doidoidoidoidoi', '', 0, '2024-01-05 16:43:12', NULL, NULL, 'asdasd', NULL, 0, ',', 1, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (100, 100011, '1111111111111111', '111111111111111111111', '2024-01-10', 'doidoidoidoidoi', '', 2, '2024-01-05 16:45:15', '2024-01-08 03:36:24', 100001, '11111111111111111111111111', '123', 0, ',', 0, '数学', NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (101, 100011, 'aaaaaaaaaaaaaaaaaaaaaaaa', 'aaaaaaaaaaaaaaaaaaa', '2024-01-12', 'doidoidoidoidoi', '', 2, '2024-01-05 16:49:15', '2024-01-08 03:36:06', 100001, 'aaaaaaaaaaaaaaa', '123456', 0, ',', 0, '语文', NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (102, 100011, '1321655465', '65145465', '2024-01-03', 'doidoidoidoidoi', '', 2, '2024-01-05 22:43:03', '2024-01-07 20:36:38', 100001, '1654654', '123', 0, ',', 0, '数学', NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (103, 100011, '论文111', '11111', '2024-01-17', 'doidoidoidoidoi', NULL, 0, '2024-01-06 16:29:54', NULL, NULL, '1111111', NULL, 0, ',', 1, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (104, 100011, '1365456', '654545', '2024-01-02', 'doidoidoidoidoi', '', 1, '2024-01-07 20:34:52', '2024-01-07 20:36:03', 100001, '465465', '1322', 0, ',', 0, '语文', NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (105, 100001, '', '', NULL, 'doidoidoidoidoi', '', 0, '2024-01-10 09:18:51', NULL, NULL, '', NULL, 0, ',', 1, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (106, 100001, '', '', NULL, NULL, '', 0, '2024-01-10 09:18:59', NULL, NULL, '', NULL, 0, ',', 1, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (107, 100001, '', '', NULL, NULL, '', 0, '2024-01-10 09:23:18', NULL, NULL, '', NULL, 0, ',', 1, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (108, 100001, '', '', NULL, NULL, '', 0, '2024-01-10 09:26:47', NULL, NULL, '', NULL, 0, ',', 1, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (109, 100001, '1', '1', '2024-01-09', 'doidoidoidoidoi', '', 0, '2024-01-10 09:26:57', NULL, NULL, '1', NULL, 0, ',', 1, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (110, 100001, '111', '11', '2024-01-01', 'doidoidoidoidoi', '', 0, '2024-01-10 09:27:22', NULL, NULL, '11', NULL, 0, ',', 1, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (111, 100001, '11', '11', '2024-01-14', 'doidoidoidoidoi', '', 0, '2024-01-10 09:30:23', NULL, NULL, '12', NULL, 0, ',', 1, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (112, 100001, '12', '123', '2024-01-09', 'doidoidoidoidoi', '', 0, '2024-01-10 09:30:54', NULL, NULL, '12', NULL, 0, ',', 0, '类型1', NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (113, 100001, '123', '123', '2024-01-07', 'doidoidoidoidoi', '', 0, '2024-01-10 09:41:12', NULL, NULL, '123', NULL, 0, ',', 0, '数学', NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (114, 100001, '', '', NULL, NULL, '', 0, '2024-01-10 09:41:49', NULL, NULL, '', NULL, 0, ',', 1, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (115, 100001, '', '', NULL, NULL, '', 0, '2024-01-10 09:42:24', NULL, NULL, '', NULL, 0, ',', 1, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (116, 100001, '', '', NULL, NULL, '', 0, '2024-01-10 09:42:30', NULL, NULL, '', NULL, 0, ',', 1, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (117, 100001, '', '', NULL, NULL, '', 0, '2024-01-10 09:43:25', NULL, NULL, '', NULL, 0, ',', 1, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (118, 100001, '', '', NULL, NULL, '', 0, '2024-01-10 09:43:32', NULL, NULL, '', NULL, 0, ',', 1, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (119, 100001, '', '', NULL, NULL, '', 0, '2024-01-10 09:43:53', NULL, NULL, '', NULL, 0, ',', 1, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (120, 100011, '测试111111', '科技大世界', '2023-12-31', 'doidoidoidoidoi', '', 0, '2024-01-10 14:52:07', NULL, NULL, '嘿嘿嘿', NULL, 0, ',', 0, '科技', NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (121, 100011, '论文1222', '生物大事件', '2024-01-01', 'doidoidoidoidoi', '', 0, '2024-01-10 14:54:14', NULL, NULL, '11111', NULL, 0, ',', 0, '生物', NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (122, 100001, '123123', '12312312312', '2024-01-09', 'doidoidoi', '', 1, '2024-01-10 19:32:52', '2024-01-11 19:00:12', 200006, '111', '同意', 0, ',100001,', 0, '类型222', NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (123, 100001, '123', '123123', '2024-01-10', 'doidoidoi', '', 0, '2024-01-10 20:47:56', NULL, NULL, '123', NULL, 0, ',', 1, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (124, 100001, '213', '123', '2024-01-08', 'doidoidoi', '', 2, '2024-01-10 21:04:27', '2024-01-11 18:03:41', 200006, '123', '不给', 0, ',100001,', 0, '数学', NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (125, 100001, '99999', '99999', '2024-01-03', 'doidoidoi', '', 1, '2024-01-10 21:05:21', '2024-01-11 09:49:32', 200006, '123', 'test', 0, ',100001,', 0, '语文', NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (126, 100011, '论文1111', '11111', '2024-01-01', 'doidoidoi', '', 0, '2024-01-11 00:02:31', NULL, NULL, '1111111', NULL, 0, ',', 0, '11111', NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (127, 100011, '111111', '111111', '2024-01-01', 'doidoidoi', '', 0, '2024-01-11 00:03:26', NULL, NULL, '111111111111', NULL, 0, ',', 0, '111', NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (128, 100011, '2222', '222', '2024-01-08', 'doidoidoi', '', 0, '2024-01-11 00:04:07', NULL, NULL, '22222', NULL, 0, ',', 0, '222', NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (129, 100011, '1111', '1111', '2023-12-31', 'doidoidoi', '', 0, '2024-01-11 00:05:28', NULL, NULL, '111111111111', NULL, 0, ',', 0, 'springboot', NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (130, 100011, '111111111', '111111111111111111111', '2024-01-09', 'doidoidoi', '', 1, '2024-01-11 00:05:55', '2024-05-08 15:15:59', 100001, '11111111111111111111111111111111111111111', '通过test', 1, ',', 0, 'springboot', NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (131, 100011, 'aaa', 'aaaa', '2023-12-31', 'doidoidoi', '', 1, '2024-01-11 00:08:12', '2024-04-28 23:05:36', 100001, 'aaaaaaaaaaaaa', '', 1, ',', 0, 'springboot', NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (132, 100011, 'zzzz', 'zzzzzz', '2023-12-31', 'doidoidoi', '', 1, '2024-01-11 00:11:12', '2024-04-28 22:52:24', 100001, 'zzzzzzzzzzzzz', '', 1, ',', 0, 'zzzzzzzzz', NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (133, 100011, '123', '123', '2024-01-17', 'doidoidoi', '', 2, '2024-01-11 10:14:07', '2024-01-11 17:45:36', 100001, '123', '123', 0, ',', 0, '123', NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (134, 100011, '论文1', '科学大世界', '2024-01-01', 'doidoidoi', '', 1, '2024-01-11 14:11:10', '2024-01-11 17:27:06', 100001, '123', '5123', 0, ',', 0, '科技', NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (135, 100001, '论文1', '科技', '2020-03-01', 'doidoidoi', NULL, 0, '2024-01-11 17:25:20', NULL, NULL, '123', NULL, 0, ',', 1, NULL, NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (136, 100011, '论文1111', '测试', '2023-12-31', 'doidoidoi2', '', 1, '2024-01-11 17:42:07', '2024-01-11 17:45:31', 100001, '111111', '123', 1, ',', 0, '测试111', NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (137, 100011, '论文1111', '论文类型', '2023-12-31', 'doidoidoi', '', 2, '2024-01-11 18:46:16', '2024-01-11 18:48:51', 100001, '11111', '123', 0, ',', 0, '科技', NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (138, 100001, '科技大创新', '科技大创新', '2024-03-12', 'doidoidoi', '', 1, '2024-03-22 13:23:17', '2024-03-22 13:23:59', 200006, '希望能通过', '恭喜你已经顺利通过了', 1, ',100001,200006,', 0, '科技', NULL, NULL, NULL);
INSERT INTO `academic_paper_audit` VALUES (139, 100001, '论文2', '3213', '2024-02-13', 'doidoidoi1', '', 0, '2024-05-11 10:02:30', NULL, NULL, '321', NULL, 0, ',', 0, '数学', NULL, NULL, NULL);

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `zw_id` bigint NOT NULL AUTO_INCREMENT COMMENT '管理员id',
  `zw_admin_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '管理员名称',
  `zw_username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录账号',
  `zw_password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录密码',
  `zw_cid` bigint NULL DEFAULT NULL COMMENT '所属学院id',
  `zw_create_date` date NULL DEFAULT NULL COMMENT '生效日期',
  `zw_status` int NULL DEFAULT NULL COMMENT '是否启用 1:启用 0:禁用',
  `zw_deleted` int NULL DEFAULT 0 COMMENT '逻辑删除 0:未删除 1:删除',
  `test1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test1',
  `test2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test2',
  PRIMARY KEY (`zw_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 200038 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '管理员' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES (200001, '小天管理员', 'xiaotian', '$10$3Pfm0av7Kxj4nt3OIe2oBe2jnMI13wOKyxSljqVl9JLH5K20Cn9iO', 1, '2021-10-27', 1, 1, NULL, NULL);
INSERT INTO `admin` VALUES (200002, '小天管理员111', 'xiaotian111', '$2a$10$dbI87E1PpKxLVsU.abe11.1f9MdDyZhEe92HD8YuFNk2yz9kyPmWu', 1, '2022-11-10', 1, 1, NULL, NULL);
INSERT INTO `admin` VALUES (200004, '王宝强', 'xiaowang', '$2a$10$YueMVzHKgFF3iQ.br75TZO/9v7DlHs1xF/m01IAZSjzBtuGuVh0di', 1, '2022-12-08', 1, 1, NULL, NULL);
INSERT INTO `admin` VALUES (200005, 'XinX', 'xfg', '$2a$10$5Wa5OpG9XSCh60GNzDlKD.NyYw.U6w54vLHdYhyAsolyIeOMCfo8O', 2, '2023-12-21', 1, 0, NULL, NULL);
INSERT INTO `admin` VALUES (200006, '小天', 'xiaotian', '$2a$10$7MAtVW/YQkBzYP1HQG1R1u3pAC/fGwmkB4no9WE65ScfZ45ZtTAOK', 1, '2022-03-01', 1, 0, NULL, NULL);
INSERT INTO `admin` VALUES (200009, '小天', '小天', '$2a$10$sfMubyrXkByWvf2CLK3cxe.CNes6TM/WqIveoAA7HXj53etljOp46', 4, '2024-01-11', 1, 1, NULL, NULL);
INSERT INTO `admin` VALUES (200010, '小天', '小天', '$2a$10$yYEu6YDUh3rgye6XJWb.p.gUhWcE/C2frWT9.N7rnC/rTtCPvFbrW', 4, '2024-01-11', 1, 0, NULL, NULL);
INSERT INTO `admin` VALUES (200011, '123', '123', '$2a$10$3Pfm0av7Kxj4nt3OIe2oBe2jnMI13wOKyxSljqVl9JLH5K20Cn9iO', 1, '2024-01-11', 1, 1, NULL, NULL);
INSERT INTO `admin` VALUES (200012, '123', '123', '$2a$10$X/jrq9mRfqJH1Rus7blVz.CKhn3lKyFi8AjN3ZPp29BvEIeNuZyt6', 2, '2024-01-11', 1, 1, NULL, NULL);
INSERT INTO `admin` VALUES (200013, '123', '789', '$2a$10$Ueut3UsEzevVNmXUSPDz8ePG0o3ReykXgW7bY2pguRTGBV0dnN.vq', 4, '2024-04-28', 1, 1, NULL, NULL);
INSERT INTO `admin` VALUES (200014, 'DQ', 'dianqi', '$2a$10$/nNLGz1iY/MoY8wVLSHEnurbHadA21Yr9G6KJIvS46Yw/EAYdSaVG', 2, '2024-04-29', 1, 0, NULL, NULL);
INSERT INTO `admin` VALUES (200015, 'XinXi', 'xinxi', '$2a$10$pGYT3WHOb5pRkIaIj9pS9O2Qi1C0.SHPIUllih6WjBBKUe65R3Mny', 1, '2024-04-30', 1, 0, NULL, NULL);
INSERT INTO `admin` VALUES (200016, 'teste', 'test', '$2a$10$LlFvjqLvVxcNxc4OR6AMTOSXfxAYyco8YcG7U9ivgvZQxlZkrhPlS', 1, '2024-05-11', 1, 1, NULL, NULL);
INSERT INTO `admin` VALUES (200017, 'tset', 'test', '$2a$10$5Y3pZgHPZnbWWCFxTzM4L.yS1xEuRiFZ69Y6.jR/oDnmEwS.rZv0K', 1, '2024-05-11', 1, 1, NULL, NULL);
INSERT INTO `admin` VALUES (200018, 'test', 'test', '$2a$10$kf2m2xiZxAVTSz23nBWGyeNV8LVUsBbxTl5vGtVBsDgk00QYMtMtK', 1, '2024-05-11', 1, 1, NULL, NULL);
INSERT INTO `admin` VALUES (200019, 'test', 'tett', '$2a$10$Yq/8TvMh8dgXbnFaVgY63e.okS4bPacEjWHuFq8yAI7Gv8ofdJA82', 2, '2024-05-11', 1, 1, NULL, NULL);
INSERT INTO `admin` VALUES (200020, 'tset', 'ttt', '$2a$10$8dCI109BHWPdG1WfPozDruIm8L2MKkLyia/zCoiqcw89/g4kGfVhC', 1, '2024-05-11', 1, 1, NULL, NULL);
INSERT INTO `admin` VALUES (200021, 'test', 'test', '$2a$10$CM9XBnwAudqNlS54puSd7uMTd8OwYGYlfRAV3J0LL/9VF27tbaRDe', 2, '2024-05-11', 1, 1, NULL, NULL);
INSERT INTO `admin` VALUES (200022, 'X123', 'test', '$2a$10$GRRe/JshUfscZ8Fh4bC1Lu.56JVimPjeJ7CNTZSyMm5frlcUoh/uG', 1, '2024-04-03', 1, 0, NULL, NULL);
INSERT INTO `admin` VALUES (200023, '3', 'tttt', '$2a$10$imm8WF85Ng43B2wRY0YJo.vbdgvXBZYyZMH/NWoA5CrFYvuCV7BSS', 2, '2024-05-11', 1, 1, NULL, NULL);
INSERT INTO `admin` VALUES (200024, 'test', 'ttt', '$2a$10$15RPmbfg9a.VIYX9AOtW6OajkMenFA9m0B3l5mrMfHk3E9Ik8onZW', 1, '2024-05-11', 1, 0, NULL, NULL);
INSERT INTO `admin` VALUES (200025, 'tet', 'tet', '$2a$10$j9uWM5SnW.iIudhkzB5PvuWYDjKGGrdnSiO0s4q9Vh/hIHO1vyhqC', 1, '2024-05-11', 1, 1, NULL, NULL);
INSERT INTO `admin` VALUES (200026, 'tes', 'te', '$2a$10$tuRT2W7ROECuXpfZvhhjs.DGRVd4gvQQj1x1NlKssF6Wrj4TY/fl.', 1, '2024-05-11', 1, 0, NULL, NULL);
INSERT INTO `admin` VALUES (200027, 'test', 'tttt', '$2a$10$sG4MT6MCFM5H/fgeyObtpeK3Vnctl/C3NIPfxWVspvRSTX20S4Jp2', 2, '2024-05-11', 1, 1, NULL, NULL);
INSERT INTO `admin` VALUES (200028, '11', '11', '$2a$10$Gr7p2nE8rCee75s.OT9UHOIJbCikk6Mpdi8./WttC13IE2q2gwfEC', 1, '2024-05-11', 1, 1, NULL, NULL);
INSERT INTO `admin` VALUES (200029, 'qqq', 'qq', '$2a$10$FvAQtz9Np40TbUxGtxGcuexZCcYHTpW0hfT9fqtiCFUDzBOQuY31S', 4, '2024-05-11', 1, 1, NULL, NULL);
INSERT INTO `admin` VALUES (200030, 'aaa', 'aaa', '$2a$10$s797VntuuOZIsD1e5XLJnumrIq7qduC0s42uko7qRFwudy9gPcai6', 1, '2024-05-11', 1, 1, NULL, NULL);
INSERT INTO `admin` VALUES (200031, 'ccc', 'ccc', '$2a$10$EGvD2VLepSUa4nwQCFJOYOHm2qXslbxYp09bX0.QQ20bvAQ8ty8Cm', 4, '2024-05-11', 1, 1, NULL, NULL);
INSERT INTO `admin` VALUES (200032, 'ww', 'ww', '$2a$10$ATVysJxyoRm/mNF19979Qud7g7OZKFiYPOkyjdO9uXN9S008DIZ72', 7, '2024-05-11', 1, 1, NULL, NULL);
INSERT INTO `admin` VALUES (200033, 'test', 'tttttt', '$2a$10$iv8dnpCpI4ALT6P2Tx9bkedzwcSfl/P78lBj2W1Rm0Bf9GkaT/lCe', 2, '2024-05-11', 1, 1, NULL, NULL);
INSERT INTO `admin` VALUES (200034, 'test', 'qqq', '$2a$10$inaK7oMUVQcXYS4zRtyPOeypDn6Cc8EF9ZzMyxL9y40NquqJPEZ9m', 1, '2024-05-11', 1, 1, NULL, NULL);
INSERT INTO `admin` VALUES (200035, 'TEST', 'tttt', '$2a$10$L6OXFKQNKQTcumES2R2KFePG.uwLv..48eFOX5qIiSZ69akPfF6wW', 1, '2024-05-11', 1, 0, NULL, NULL);
INSERT INTO `admin` VALUES (200036, 'test', 'testttt', '$2a$10$vZ8p2SIqj.CV1MU/M2jrce/1bFxhXMhKsaYhLqLO2LVobP6uyF1a.', 2, '2024-05-11', 1, 0, NULL, NULL);
INSERT INTO `admin` VALUES (200037, '123', '123', '$2a$10$E1nbKwRQP6C5uQtI6d7HTOACANfKDjc9WfOJaGlifQqGvZFv5M2B.', 1, '2024-05-11', 1, 0, NULL, NULL);

-- ----------------------------
-- Table structure for college
-- ----------------------------
DROP TABLE IF EXISTS `college`;
CREATE TABLE `college`  (
  `zw_id` bigint NOT NULL AUTO_INCREMENT COMMENT '学院id',
  `zw_college_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '学院名称',
  `zw_deleted` int NULL DEFAULT 0 COMMENT '逻辑删除 0:未删除 1:删除',
  `test1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备用1',
  `test2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备用2',
  PRIMARY KEY (`zw_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 31 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '学院' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of college
-- ----------------------------
INSERT INTO `college` VALUES (0, '无', 0, NULL, NULL);
INSERT INTO `college` VALUES (1, '信息工程学院', 0, NULL, NULL);
INSERT INTO `college` VALUES (2, '电气工程学院', 0, NULL, NULL);
INSERT INTO `college` VALUES (4, '机械工程学院', 0, NULL, NULL);
INSERT INTO `college` VALUES (5, '电气工程学院', 1, NULL, NULL);
INSERT INTO `college` VALUES (7, '马克思学院', 0, NULL, NULL);
INSERT INTO `college` VALUES (8, '111', 1, NULL, NULL);
INSERT INTO `college` VALUES (9, '123', 1, NULL, NULL);
INSERT INTO `college` VALUES (10, '213', 1, NULL, NULL);
INSERT INTO `college` VALUES (11, '123', 1, NULL, NULL);
INSERT INTO `college` VALUES (12, '信息工程', 1, NULL, NULL);
INSERT INTO `college` VALUES (14, '123', 1, NULL, NULL);
INSERT INTO `college` VALUES (27, '信息学院', 0, NULL, NULL);
INSERT INTO `college` VALUES (28, '321', 1, NULL, NULL);
INSERT INTO `college` VALUES (29, '123', 1, NULL, NULL);
INSERT INTO `college` VALUES (30, '333', 1, NULL, NULL);

-- ----------------------------
-- Table structure for education_experience_audit
-- ----------------------------
DROP TABLE IF EXISTS `education_experience_audit`;
CREATE TABLE `education_experience_audit`  (
  `zw_id` bigint NOT NULL AUTO_INCREMENT COMMENT '教育经历审核id',
  `zw_tid` bigint NULL DEFAULT NULL COMMENT '所属教师id(创建人)',
  `zw_sta_date` date NULL DEFAULT NULL COMMENT '开始日期',
  `zw_end_date` date NULL DEFAULT NULL COMMENT '结束日期',
  `zw_school` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '学校',
  `zw_major` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '专业',
  `zw_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附件路径',
  `zw_audit_status` int NULL DEFAULT NULL COMMENT '审核状态 0:待审核 1:通过 2:驳回',
  `zw_create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `zw_audit_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `zw_aid` bigint NULL DEFAULT NULL COMMENT '审核员id',
  `zw_teacher_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '教师备注',
  `zw_auditor_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核员备注',
  `zw_is_show` int NULL DEFAULT NULL COMMENT '是否展示 0:不展示 1:展示',
  `zw_delete_roles` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除角色',
  `zw_deleted` int NULL DEFAULT 0 COMMENT '逻辑删除 0:未删除 1:删除',
  `test1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test1',
  `test2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test2',
  `test3` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test3',
  `test4` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test4',
  PRIMARY KEY (`zw_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '教育经历审核' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of education_experience_audit
-- ----------------------------
INSERT INTO `education_experience_audit` VALUES (1, 100001, '2023-06-01', '2023-06-03', 'xx大学', '软件技术', NULL, 1, '2024-02-03 13:46:54', '2024-05-07 14:39:52', 200006, '123', '属实', 1, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `education_experience_audit` VALUES (2, 100001, '2009-02-09', '2024-01-19', 'xx大学', '软件工程', NULL, 0, '2024-01-03 20:21:52', NULL, NULL, '11111111', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `education_experience_audit` VALUES (3, 100001, '2024-01-08', '2024-01-09', 'xxx大学', '软件工程1', '', 0, '2024-01-03 20:41:53', NULL, NULL, '11111111111', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `education_experience_audit` VALUES (4, 100011, '2024-01-08', '2024-01-18', 'xxx大学', '软件工程', '', 0, '2024-01-04 15:11:24', NULL, NULL, '111111111111111111', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `education_experience_audit` VALUES (5, 100001, '2024-01-10', '2024-01-19', '1111111111111111', '11111111111111111111', '', 1, '2024-01-05 16:02:14', '2024-01-08 21:24:08', 200006, '11111111111111111111', 'ztesttesttest', 1, ',200006,', 0, NULL, NULL, NULL, NULL);
INSERT INTO `education_experience_audit` VALUES (6, 100011, '2023-12-31', '2024-01-17', 'xxx大学', '软件技术', '', 0, '2024-01-10 14:55:26', NULL, NULL, '11111', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `education_experience_audit` VALUES (7, 100011, '2018-08-31', '2018-08-31', 'xxx大学', '计算机应用技术', NULL, 0, '2024-01-10 23:52:52', NULL, NULL, '123', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `education_experience_audit` VALUES (8, 100011, NULL, NULL, '111', '111', NULL, 2, '2024-01-10 23:54:49', '2024-01-11 15:18:31', 100001, '111', '123', 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `education_experience_audit` VALUES (9, 100011, '2024-02-06', '2024-02-07', '123', '123', NULL, 1, '2024-01-11 10:10:29', '2024-01-11 15:18:22', 100001, '123', '123', 1, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `education_experience_audit` VALUES (10, 100001, '2021-09-01', '2024-06-01', 'xxx大学', '计算机', NULL, 0, '2024-04-25 20:27:19', NULL, NULL, '123456', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `education_experience_audit` VALUES (11, 100001, '2023-05-16', '2024-06-21', 'xx小学', '数学', NULL, 1, '2024-05-07 10:44:34', '2024-05-07 10:45:30', 200006, '接受教育', '已查看附件，情况属实', 1, ',', 0, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for honorary_award_audit
-- ----------------------------
DROP TABLE IF EXISTS `honorary_award_audit`;
CREATE TABLE `honorary_award_audit`  (
  `zw_id` bigint NOT NULL AUTO_INCREMENT COMMENT '荣誉奖项id',
  `zw_tid` bigint NULL DEFAULT NULL COMMENT '所属教师id(创建人)',
  `zw_get_date` date NULL DEFAULT NULL COMMENT '获奖时间',
  `zw_type` int NULL DEFAULT NULL COMMENT '类型 0:团队 1:个人',
  `zw_content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '内容',
  `zw_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附件路径 ',
  `zw_audit_status` int NULL DEFAULT NULL COMMENT '审核状态 0:待审核 1:通过 2:驳回',
  `zw_create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `zw_audit_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `zw_aid` bigint NULL DEFAULT NULL COMMENT '审核员id',
  `zw_teacher_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '教师备注',
  `zw_auditor_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核员备注',
  `zw_is_show` int NULL DEFAULT NULL COMMENT '是否展示 0:不展示 1:展示',
  `zw_delete_roles` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除角色',
  `zw_deleted` int NULL DEFAULT 0 COMMENT '逻辑删除 0:未删除 1:删除',
  `test1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test1',
  `test2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test2',
  `test3` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test3',
  `test4` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test4',
  PRIMARY KEY (`zw_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '荣誉奖项审核' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of honorary_award_audit
-- ----------------------------
INSERT INTO `honorary_award_audit` VALUES (1, 100001, '2023-07-01', 1, '三好学生', NULL, 1, '2023-07-15 13:48:24', '2023-07-29 14:45:28', 20006, '111', '已审阅', 1, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `honorary_award_audit` VALUES (3, 100011, '2023-12-31', 1, '校二等奖', '', 0, '2024-01-09 20:29:50', NULL, NULL, '123', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `honorary_award_audit` VALUES (4, 100011, '2023-12-31', 1, '湖南省十大优秀教师', '', 0, '2024-01-10 14:56:17', NULL, NULL, '1111', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `honorary_award_audit` VALUES (5, 100011, '2023-12-31', 1, '湖南省优秀教师', '', 0, '2024-01-10 14:56:58', NULL, NULL, '222', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `honorary_award_audit` VALUES (6, 100011, '2024-01-09', 1, 'aa啊啊啊', NULL, 0, '2024-01-11 10:13:00', NULL, NULL, '12312啊啊啊啊', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for office
-- ----------------------------
DROP TABLE IF EXISTS `office`;
CREATE TABLE `office`  (
  `zw_id` bigint NOT NULL AUTO_INCREMENT COMMENT '教研室id',
  `zw_office_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '教研室名称',
  `zw_cid` bigint NULL DEFAULT NULL COMMENT '所属学院id',
  `zw_deleted` int NULL DEFAULT 0 COMMENT '逻辑删除 0:未删除 1:删除',
  `test1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test1',
  `test2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test2',
  PRIMARY KEY (`zw_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 89 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '教研室' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of office
-- ----------------------------
INSERT INTO `office` VALUES (0, '暂无隶属办公室', 0, 0, NULL, NULL);
INSERT INTO `office` VALUES (1, '软件教研室', 1, 0, NULL, NULL);
INSERT INTO `office` VALUES (2, '网络教研室', 1, 0, NULL, NULL);
INSERT INTO `office` VALUES (3, '电气教研室1', 2, 0, NULL, NULL);
INSERT INTO `office` VALUES (4, '电气教研室2', 2, 1, NULL, NULL);
INSERT INTO `office` VALUES (5, '云计算教研室', 1, 0, NULL, NULL);
INSERT INTO `office` VALUES (6, '4', 1, 1, NULL, NULL);
INSERT INTO `office` VALUES (7, '大数据协会', 1, 0, NULL, NULL);
INSERT INTO `office` VALUES (9, '数媒教研室', 1, 1, NULL, NULL);
INSERT INTO `office` VALUES (10, '啊啊啊', 1, 1, NULL, NULL);
INSERT INTO `office` VALUES (11, '电气教研室2', 2, 0, NULL, NULL);
INSERT INTO `office` VALUES (12, 'aaa', 4, 1, NULL, NULL);
INSERT INTO `office` VALUES (13, '', 4, 1, NULL, NULL);
INSERT INTO `office` VALUES (14, '数字媒体教研室', 1, 1, NULL, NULL);
INSERT INTO `office` VALUES (15, '123', 1, 1, NULL, NULL);
INSERT INTO `office` VALUES (16, 'abc', 1, 1, NULL, NULL);
INSERT INTO `office` VALUES (17, '213', 1, 1, NULL, NULL);
INSERT INTO `office` VALUES (19, '1123', 1, 1, NULL, NULL);
INSERT INTO `office` VALUES (20, '111', 2, 1, NULL, NULL);
INSERT INTO `office` VALUES (21, '123', 1, 1, NULL, NULL);
INSERT INTO `office` VALUES (22, '11', 1, 1, NULL, NULL);
INSERT INTO `office` VALUES (23, '党办', 1, 0, 'string', 'string');
INSERT INTO `office` VALUES (24, '11122', 1, 1, NULL, NULL);
INSERT INTO `office` VALUES (25, '123123123', 1, 1, NULL, NULL);
INSERT INTO `office` VALUES (26, '办公室2号修改', 1, 0, NULL, NULL);
INSERT INTO `office` VALUES (27, '1211', 4, 1, NULL, NULL);
INSERT INTO `office` VALUES (28, '111222', 1, 1, NULL, NULL);
INSERT INTO `office` VALUES (29, '111', 1, 1, NULL, NULL);
INSERT INTO `office` VALUES (30, '123', 2, 1, NULL, NULL);
INSERT INTO `office` VALUES (31, 'aaaa', 1, 1, NULL, NULL);
INSERT INTO `office` VALUES (32, '1111', 4, 1, NULL, NULL);
INSERT INTO `office` VALUES (33, '11122222222', 4, 1, NULL, NULL);
INSERT INTO `office` VALUES (34, '111', 2, 1, NULL, NULL);
INSERT INTO `office` VALUES (35, '办公室3号', 1, 1, NULL, NULL);
INSERT INTO `office` VALUES (36, '234', 1, 1, NULL, NULL);
INSERT INTO `office` VALUES (37, '234', 4, 0, NULL, NULL);
INSERT INTO `office` VALUES (38, 'mks', 7, 0, NULL, NULL);
INSERT INTO `office` VALUES (39, '', 2, 1, NULL, NULL);
INSERT INTO `office` VALUES (40, 'mks学习小组', 7, 0, NULL, NULL);
INSERT INTO `office` VALUES (80, '234', 1, 1, NULL, NULL);
INSERT INTO `office` VALUES (81, '234', 1, 0, NULL, NULL);
INSERT INTO `office` VALUES (82, '112', 1, 0, NULL, NULL);
INSERT INTO `office` VALUES (83, '信息办公室', 27, 0, NULL, NULL);
INSERT INTO `office` VALUES (84, '123', 2, 0, NULL, NULL);
INSERT INTO `office` VALUES (85, '243', 7, 1, NULL, NULL);
INSERT INTO `office` VALUES (86, '234', 7, 0, NULL, NULL);
INSERT INTO `office` VALUES (87, '111', 1, 1, NULL, NULL);
INSERT INTO `office` VALUES (88, '333', 30, 1, NULL, NULL);

-- ----------------------------
-- Table structure for project_audit
-- ----------------------------
DROP TABLE IF EXISTS `project_audit`;
CREATE TABLE `project_audit`  (
  `zw_id` bigint NOT NULL AUTO_INCREMENT COMMENT '项目id',
  `zw_tid` bigint NULL DEFAULT NULL COMMENT '所属教师id(创建人)',
  `zw_project_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '项目名称',
  `zw_project_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '项目类型',
  `zw_completion_date` date NULL DEFAULT NULL COMMENT '完成发布日期',
  `zw_team_size` int NULL DEFAULT NULL COMMENT '团队人数',
  `zw_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附件路径',
  `zw_audit_status` int NULL DEFAULT NULL COMMENT '审核状态 0:待审核 1:通过 2:驳回',
  `zw_create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `zw_audit_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `zw_aid` bigint NULL DEFAULT NULL COMMENT '审核员id',
  `zw_teacher_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '教师备注',
  `zw_auditor_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核员备注',
  `zw_is_show` int NULL DEFAULT NULL COMMENT '是否展示 0:不展示 1:展示',
  `zw_delete_roles` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除角色',
  `zw_deleted` int NULL DEFAULT 0 COMMENT '逻辑删除 0:未删除 1:删除',
  `test2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test2',
  `test3` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test3',
  `tset4` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test4',
  PRIMARY KEY (`zw_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '项目审核' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of project_audit
-- ----------------------------
INSERT INTO `project_audit` VALUES (1, 100001, '<项目名称>', '数学', '2023-06-01', 3, NULL, 1, '2023-07-15 13:50:48', '2023-09-15 14:46:35', 200006, '123', '可以', 1, ',', 0, NULL, NULL, NULL);
INSERT INTO `project_audit` VALUES (2, 100011, '语文', '语文', '2024-01-16', 2, '', 0, '2024-01-09 20:31:25', NULL, NULL, '嘿嘿嘿', NULL, 0, ',', 0, NULL, NULL, NULL);
INSERT INTO `project_audit` VALUES (3, 100011, '学生管理系统', '数学', '2024-01-01', 3, '', 0, '2024-01-09 20:35:26', NULL, NULL, '哈哈哈', NULL, 0, ',', 0, NULL, NULL, NULL);
INSERT INTO `project_audit` VALUES (4, 100011, '基于XXX', '计算机', '2023-12-31', 3, '', 0, '2024-01-10 15:13:18', NULL, NULL, '嘿嘿嘿', NULL, 0, ',', 0, NULL, NULL, NULL);
INSERT INTO `project_audit` VALUES (5, 100001, '计算机科研', '计算机', '2024-04-17', 3, NULL, 0, '2024-04-25 20:35:16', NULL, NULL, '团队赛', NULL, 0, ',', 0, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for request_college_change
-- ----------------------------
DROP TABLE IF EXISTS `request_college_change`;
CREATE TABLE `request_college_change`  (
  `zw_id` bigint NOT NULL AUTO_INCREMENT COMMENT '转学院申请id',
  `zw_tid` bigint NULL DEFAULT NULL COMMENT '教师id',
  `zw_old_aid` bigint NULL DEFAULT NULL COMMENT '原管理员id',
  `zw_old_cid` bigint NULL DEFAULT NULL COMMENT '原学院id',
  `zw_new_aid` bigint NULL DEFAULT NULL COMMENT '新管理员id',
  `zw_new_cid` bigint NULL DEFAULT NULL COMMENT '新学院id',
  `zw_old_admin_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '原管理员备注',
  `zw_new_admin_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '新管理员备注',
  `zw_create_time` datetime NULL DEFAULT NULL COMMENT '申请时间',
  `zw_audit_time` datetime NULL DEFAULT NULL COMMENT '审核时间',
  `zw_audit_status` int NULL DEFAULT NULL COMMENT '申请状态 0待审核 1通过 2未通过',
  `zw_delete_roles` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除角色',
  `zw_deleted` int NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`zw_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '变更学院申请' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of request_college_change
-- ----------------------------
INSERT INTO `request_college_change` VALUES (2, 100002, 200006, 1, 200005, 2, NULL, '哈哈你通过了，恭喜你，以后你就是我们电气学院的教师了', '2023-12-21 17:18:20', '2023-12-21 20:39:07', 1, ',', 0);
INSERT INTO `request_college_change` VALUES (3, 100003, 200006, 1, 200005, 2, '123', '通过', '2023-12-21 17:20:21', '2024-01-11 17:41:37', 1, ',200005,', 0);
INSERT INTO `request_college_change` VALUES (4, 100001, 200006, 1, NULL, 2, 'test', NULL, '2024-01-09 21:29:26', NULL, 0, ',', 1);
INSERT INTO `request_college_change` VALUES (5, 100011, 200006, 1, NULL, 2, NULL, NULL, '2024-01-11 09:27:05', NULL, 0, ',', 1);
INSERT INTO `request_college_change` VALUES (6, 100002, 200005, 2, 200006, 1, '12', '通过', '2024-01-11 14:53:23', '2024-01-11 17:42:56', 1, ',', 0);
INSERT INTO `request_college_change` VALUES (7, 100006, 200005, 2, NULL, 4, '213', NULL, '2024-01-11 18:15:10', NULL, 0, ',', 0);
INSERT INTO `request_college_change` VALUES (8, 100044, 200006, 1, 200005, 2, '一月', '请进', '2024-01-11 18:28:32', '2024-01-11 18:28:40', 1, ',', 0);
INSERT INTO `request_college_change` VALUES (9, 100044, 200005, 2, 200006, 1, '你好', '欢迎', '2024-01-11 18:29:38', '2024-01-11 18:30:10', 1, ',', 0);
INSERT INTO `request_college_change` VALUES (10, 100044, 200006, 1, NULL, 2, '一月申请', NULL, '2024-01-11 18:57:12', NULL, 0, ',', 1);
INSERT INTO `request_college_change` VALUES (11, 100015, 200006, 1, NULL, 2, '122', NULL, '2024-04-26 19:38:49', NULL, 0, ',', 1);
INSERT INTO `request_college_change` VALUES (12, 100019, 200006, 1, NULL, 4, '123', NULL, '2024-04-28 06:57:52', NULL, 0, ',', 1);
INSERT INTO `request_college_change` VALUES (13, 100045, 200006, 1, NULL, 2, '123', NULL, '2024-04-28 15:31:44', NULL, 0, ',', 0);
INSERT INTO `request_college_change` VALUES (14, 100023, 200006, 1, 200005, 2, '123', '已知晓相关情况，同意转入！', '2024-04-28 20:42:27', '2024-04-29 21:04:15', 1, ',', 0);
INSERT INTO `request_college_change` VALUES (15, 100016, 200006, 1, NULL, 2, '申请换入电气！', NULL, '2024-04-29 16:44:02', NULL, 0, ',', 0);
INSERT INTO `request_college_change` VALUES (16, 100001, 200006, 1, NULL, 2, NULL, NULL, '2024-05-11 08:58:51', NULL, 0, ',', 1);

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `zw_id` bigint NOT NULL AUTO_INCREMENT COMMENT '职务id',
  `zw_role_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '职务名称',
  `test1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test1',
  `test2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test2',
  PRIMARY KEY (`zw_id`) USING BTREE
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
  `zw_id` bigint NOT NULL AUTO_INCREMENT COMMENT '软件著作id',
  `zw_tid` bigint NULL DEFAULT NULL COMMENT '所属教师id(创建人)',
  `zw_software_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '软件名称',
  `zw_stage` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '软著阶段',
  `zw_status` int NULL DEFAULT NULL COMMENT '软著状态',
  `zw_completion_date` date NULL DEFAULT NULL COMMENT '完成/发布日期',
  `zw_team_size` int NULL DEFAULT NULL COMMENT '团队人数',
  `zw_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附件路径',
  `zw_audit_status` int NULL DEFAULT NULL COMMENT '审核状态 0:待审核 1:通过 2:驳回',
  `zw_create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `zw_audit_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `zw_aid` bigint NULL DEFAULT NULL COMMENT '审核员id',
  `zw_teacher_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '教师备注',
  `zw_auditor_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核员备注',
  `zw_is_show` int NULL DEFAULT NULL COMMENT '是否展示 0:不展示 1:展示',
  `zw_delete_roles` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除角色',
  `zw_deleted` int NULL DEFAULT 0 COMMENT '逻辑删除 0:未删除 1:删除',
  `test3` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test3',
  `tset4` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test4',
  PRIMARY KEY (`zw_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '软件著作审核' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of software_audit
-- ----------------------------
INSERT INTO `software_audit` VALUES (1, 100001, '<软件著作名称>', '已发布', 1, '2023-06-28', 3, NULL, 1, '2024-03-01 14:18:32', '2024-04-17 14:18:42', 200006, '教师备注000', '管理员备注111', 1, ',', 0, NULL, NULL);
INSERT INTO `software_audit` VALUES (2, 100011, '11', '1', 1, '2024-01-09', 1111, NULL, 0, '2024-01-11 00:58:06', NULL, NULL, '111', NULL, 0, ',', 0, NULL, NULL);
INSERT INTO `software_audit` VALUES (3, 100001, 'rjzz', '申请准备阶段', 0, '2024-02-06', 3, NULL, 0, '2024-05-10 15:41:07', NULL, NULL, '1', NULL, 0, ',', 0, NULL, NULL);

-- ----------------------------
-- Table structure for super_admin
-- ----------------------------
DROP TABLE IF EXISTS `super_admin`;
CREATE TABLE `super_admin`  (
  `zw_id` bigint NOT NULL AUTO_INCREMENT COMMENT '超级管理员id',
  `zw_superadmin_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '超级管理员名称',
  `zw_username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录账号',
  `zw_password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登陆密码',
  `zw_deleted` int NULL DEFAULT 0 COMMENT '逻辑删除 0:未删除 1:删除',
  `test1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test1',
  `test2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test2',
  PRIMARY KEY (`zw_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 300002 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '超级管理员' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of super_admin
-- ----------------------------
INSERT INTO `super_admin` VALUES (300001, '小天超级管理员', 'xiaotian', '$2a$10$aKLOQt0s9kH/zAyAije3iektNYjny3lZY4T54pWGYVN.qMdHAyY6y', 0, NULL, NULL);

-- ----------------------------
-- Table structure for teacher
-- ----------------------------
DROP TABLE IF EXISTS `teacher`;
CREATE TABLE `teacher`  (
  `zw_id` bigint NOT NULL AUTO_INCREMENT COMMENT '教师id',
  `zw_desk_id` bigint NULL DEFAULT NULL COMMENT '工位号',
  `zw_teacher_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `zw_username` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录账号',
  `zw_password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录密码',
  `zw_gender` int NULL DEFAULT NULL COMMENT '性别   0:女 1:男',
  `zw_ethnic` enum('汉族','阿昌族','白族','保安族','布朗族','布依族','朝鲜族','达干尔族','傣族','德昂族','东乡族','侗族','独龙族','俄罗斯族',' 鄂伦春人','鄂温克族','高山族','仡佬族','哈尼族','哈萨克族','赫哲族','回族','基诺族','京族','景颇族','柯尔克孜族','拉祜族','黎族','僳僳族','珞巴族','满族','毛南族','门巴族','蒙古族','苗族','仫佬族族','纳西族','怒族','普米族','羌族','撒拉族','畲族','水族','塔塔尔族','塔吉克族','土家族','土族','佤族','维吾尔族','乌孜别克族','锡伯族','瑶族','彝族','裕固族','藏族','壮族') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '\r\n民族',
  `zw_native_place` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '籍贯',
  `zw_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '住址',
  `zw_phone` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电话号码',
  `zw_cid` bigint NULL DEFAULT NULL COMMENT '所属学院id',
  `zw_oid` bigint NULL DEFAULT NULL COMMENT '所属教研室id',
  `zw_education_degree` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文化程度',
  `zw_politics_status` enum('群众','共青团员','中共预备党员','中共党员','其他') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '政治面貌',
  `zw_id_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `zw_is_auditor` int NULL DEFAULT NULL COMMENT '是否审核员 0:不是 1:是',
  `zw_create_date` date NULL DEFAULT NULL COMMENT '生效日期',
  `zw_start_date` date NULL DEFAULT NULL COMMENT '入校日期',
  `zw_deleted` int NULL DEFAULT 0 COMMENT '逻辑删除 0:未删除 1:删除',
  `test3` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备用3',
  `test4` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备用4',
  PRIMARY KEY (`zw_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100053 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '教师(用户)' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of teacher
-- ----------------------------
INSERT INTO `teacher` VALUES (100001, 20210300000, 'xiaotian12', 'xiaotian', '$2a$10$ty14IGZsGOsqutpbJk9DL.ArXzqXhNBvXbe5IF8FVx6kKeJfpZxCK', 1, '汉族', '湖南', '湖南长沙天心区', '15345678121', 1, 1, '研究生', '中共党员', '430000000000000000', 1, '2023-10-31', '2023-04-06', 0, NULL, NULL);
INSERT INTO `teacher` VALUES (100002, 20210311111, '3老师', '3老师', '$2a$10$oXJF38ma673Rqod75EwBZeH94pvwRoPdgBapzxuVApFssp8.HRHM2', 1, '汉族', '广东', '广东广州', '15757994567', 1, 2, '大专', '群众', '430000000000000004', 1, '2024-01-11', '2022-08-01', 0, NULL, NULL);
INSERT INTO `teacher` VALUES (100003, NULL, 'xiaotian111', 'xiaotian111', '$2a$10$VXtCuwtbYyWZLV5PqwtD6ObdNfgL4NxsgFfejzah0zYzYGg/yGjRO', 0, '汉族', '湖南省长沙市', '湖南省长沙市', '15757994568', 1, 1, '大专', '群众', '430000000000000001', 1, '2022-12-09', '2023-05-17', 0, NULL, NULL);
INSERT INTO `teacher` VALUES (100006, NULL, '小天222', 'xiaotian222', '$2a$10$OLzkSlDk.uEd3B3uAmhLvOSQFbrSYVnWTYyp.nC1yBcrKe7w80IEC', 1, '汉族', '汉族', '湖南', '15777773123', 2, 3, '本科', '群众', '430000000000000002', 1, '2022-12-09', '2022-10-12', 0, NULL, NULL);
INSERT INTO `teacher` VALUES (100008, NULL, '大哥', 'user1', '$2a$10$9hVplSi.GB4/wq6ckz1HBeQgZ6wJt/UwicKazzbftMHs9.rUfkMKe', 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '430000000000000003', 1, '2023-03-02', '2023-04-12', 0, NULL, NULL);
INSERT INTO `teacher` VALUES (100010, NULL, '卧龙', 'user2', '$2a$10$GrLLD6BaBGqy..A/agxXf..NTo7hAbwcBSxBO80mssPkI5iQqdJHm', 1, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', 1, '2023-10-20', '2022-06-15', 0, NULL, NULL);
INSERT INTO `teacher` VALUES (100011, NULL, '小王', 'xiaowang', '$2a$10$mvr5fx9ObV0UMFa7eVY5VuqJRxQX.Z0CbM8Z7u03e9HURap2LPxQy', 1, '塔吉克族', '湖南永州', '湖南长沙', '15711111111', 1, 1, '博士', '共青团员', '', 0, '2024-04-29', '2023-01-18', 0, NULL, NULL);
INSERT INTO `teacher` VALUES (100012, NULL, NULL, '15711111111', '$2a$10$Ds0F8/wPV6Vsg2EfRBSlMup7xTflPShEqBmNIKqS1rWK477DvK0ti', NULL, NULL, NULL, NULL, '15712334566', 2, 3, NULL, NULL, NULL, 0, '2024-01-08', NULL, 1, NULL, NULL);
INSERT INTO `teacher` VALUES (100013, NULL, '小张', '15722222222', '$2a$10$Gx8xR2QPhA/dnUeNaqJb5OBYRKs80CXb2i3uir.Ej3Zg2RRe8TSIm', 1, '汉族', '广东', '长沙', '15722222222', 1, 1, '研究生', '共青团员', NULL, 0, '2024-01-08', '2023-05-07', 0, NULL, NULL);
INSERT INTO `teacher` VALUES (100014, NULL, '小赵', '18721122232', '$2a$10$Gx8xR2QPhA/dnUeNaqJb5OBYRKs80CXb2i3uir.Ej3Zg2RRe8TSIm', 1, '汉族', NULL, NULL, '18721122232', 1, 2, '本科', NULL, NULL, 0, '2024-01-09', '2023-05-07', 0, NULL, NULL);
INSERT INTO `teacher` VALUES (100015, NULL, 'wxq', '18721122289', '$2a$10$Gx8xR2QPhA/dnUeNaqJb5OBYRKs80CXb2i3uir.Ej3Zg2RRe8TSIm', 1, '汉族', '湖南湘潭', '湖南长沙', '18721122289', 1, 2, '研究生', NULL, NULL, 0, '2024-01-09', '2022-05-18', 0, NULL, NULL);
INSERT INTO `teacher` VALUES (100016, NULL, 'abc', '18721122292', '$2a$10$Gx8xR2QPhA/dnUeNaqJb5OBYRKs80CXb2i3uir.Ej3Zg2RRe8TSIm', NULL, '汉族', '湖南', '福建', '18721122292', 1, 7, '研究生', NULL, NULL, 1, '2024-04-29', '2022-05-18', 0, NULL, NULL);
INSERT INTO `teacher` VALUES (100017, NULL, NULL, '18721122299', '$2a$10$S9SPf2ygc5m23fz1Y51KxuUrTmc5q9vBWWWQMovhBSbB72exJwxEq', NULL, NULL, NULL, NULL, '18721122299', 1, 7, NULL, NULL, NULL, 0, '2024-01-09', NULL, 1, NULL, NULL);
INSERT INTO `teacher` VALUES (100018, NULL, 'wj', '15321122289', '$2a$10$Gx8xR2QPhA/dnUeNaqJb5OBYRKs80CXb2i3uir.Ej3Zg2RRe8TSIm', 1, '汉族', '湖南长沙', '湖南益阳', '15321122289', 1, 5, '研究生', NULL, NULL, 1, '2024-05-07', '2022-05-18', 0, NULL, NULL);
INSERT INTO `teacher` VALUES (100019, NULL, 'zaq', '15321122282', '$2a$10$Gx8xR2QPhA/dnUeNaqJb5OBYRKs80CXb2i3uir.Ej3Zg2RRe8TSIm', 1, '汉族', '湖南湘潭', '湖南长沙', '15321122182', 1, 7, '高中', '共青团员', NULL, 0, '2024-01-09', '2022-05-18', 0, NULL, NULL);
INSERT INTO `teacher` VALUES (100020, NULL, 'qqqq', '15321122289', '$2a$10$Gx8xR2QPhA/dnUeNaqJb5OBYRKs80CXb2i3uir.Ej3Zg2RRe8TSIm', 0, '汉族', '湖南张家界', '湖南长沙', '15321122389', 1, 7, '研究生', NULL, NULL, 0, '2024-01-09', NULL, 1, NULL, NULL);
INSERT INTO `teacher` VALUES (100021, NULL, NULL, '15321562289', '$2a$10$fuVM61J2rC.2iz7yCldp8eV2uURIpGwKtvLheBWXfYKjKpJiVFPGi', NULL, NULL, NULL, NULL, '15321562289', 1, 2, NULL, NULL, NULL, 0, '2024-01-11', NULL, 1, NULL, NULL);
INSERT INTO `teacher` VALUES (100022, NULL, NULL, '15321562290', '$2a$10$p3ii4bKVGegcIZ.R.V92euK28NQ0yNoVXa7s2ohQJN6wzQXhWjtuK', NULL, NULL, NULL, NULL, '15321562290', 1, 7, NULL, NULL, NULL, 0, '2024-01-11', NULL, 1, NULL, NULL);
INSERT INTO `teacher` VALUES (100023, NULL, '1月11', '15321562291', '$2a$10$y8yRA0ZQWOLTFU8prYx/qe/XlyXO8rPHBb51C8AKKNhgXvFKO.TNC', 1, '汉族', '湖南长沙', '湖南长沙', '15321562291', 2, 3, '大专', '共青团员', NULL, 0, '2024-04-28', NULL, 0, NULL, NULL);
INSERT INTO `teacher` VALUES (100024, NULL, NULL, '18121562289', '$2a$10$9MGWbJU3kbBUUirFfC2JHO15WmyGK1lG1vdw7DeRvi9GnNm4SZMmG', NULL, NULL, NULL, NULL, '18121562289', 1, 2, NULL, NULL, NULL, 0, '2024-01-11', NULL, 1, NULL, NULL);
INSERT INTO `teacher` VALUES (100025, NULL, NULL, '18121562290', '$2a$10$w8JY1YEJLfz41b8mhecLp.VGGKppC7e9qg2UBpkb5JEWygO2TOebi', NULL, NULL, NULL, NULL, '18121562290', 1, 7, NULL, NULL, NULL, 0, '2024-01-11', NULL, 1, NULL, NULL);
INSERT INTO `teacher` VALUES (100026, NULL, NULL, '18121562291', '$2a$10$LULbPdO5IQ1j4.0MtBlKIeUTalYROQh/guTvreavkCiT18hP0a51a', NULL, NULL, NULL, NULL, '18121562291', 1, 7, NULL, NULL, '430000000000000006', 0, '2024-01-11', NULL, 1, NULL, NULL);
INSERT INTO `teacher` VALUES (100027, NULL, NULL, '15124179241', '$2a$10$mrDb7Esl2qqBdQ2Tguh8.OJJ1qa5Jc13fR2nN9m/9tvXf8340/j1K', NULL, NULL, NULL, NULL, '15124179241', 1, 2, NULL, '群众', NULL, 0, '2024-01-11', NULL, 1, NULL, NULL);
INSERT INTO `teacher` VALUES (100028, NULL, NULL, '15124179242', '$2a$10$V8FmhfwZda8IOs9mvFBcceqBB2BUf1a7/vadvWTIL2z3ms0Mb46dW', NULL, NULL, NULL, NULL, '15124179242', 1, 7, NULL, NULL, NULL, 0, '2024-01-11', NULL, 1, NULL, NULL);
INSERT INTO `teacher` VALUES (100029, NULL, NULL, '15124179243', '$2a$10$vwPnkH3s9eADO9j.MehMb.EtNy6kAEC4i4YiTskH9iX6UA8MSbZHi', NULL, NULL, NULL, NULL, '15124179243', 1, 7, NULL, NULL, NULL, 0, '2024-01-11', NULL, 1, NULL, NULL);
INSERT INTO `teacher` VALUES (100030, NULL, NULL, '15112379241', '$2a$10$tEV21zyVbUho5CV6ujKqKuQqbbSUb6RmFGzxhcrNb/pGUnfuEjZYq', NULL, NULL, NULL, NULL, '15112379241', 1, 2, NULL, NULL, NULL, 0, '2024-01-11', NULL, 1, NULL, NULL);
INSERT INTO `teacher` VALUES (100031, NULL, NULL, '15189179242', '$2a$10$wHlE57rJ9EvcFIWbfwquNenVUyGXUkKqYIP9ZmG.JndkH27kaMT2C', NULL, NULL, NULL, NULL, '15189179242', 1, 7, NULL, NULL, NULL, 0, '2024-01-11', NULL, 1, NULL, NULL);
INSERT INTO `teacher` VALUES (100032, NULL, NULL, '15124179984', '$2a$10$ZXhVVBLZiUXTdKOHIg8AWOwGnkfj5eFhGrG7sx1Mml7XJVa1aD6ZC', NULL, NULL, NULL, NULL, '15124179984', 1, 7, NULL, NULL, NULL, 0, '2024-01-11', NULL, 1, NULL, NULL);
INSERT INTO `teacher` VALUES (100033, NULL, NULL, '18123792418', '$2a$10$OzxSLzry63sFGIb4lUUUweE7V/yDMgOLDi9fhY0DwGT3R2FByuw/K', NULL, NULL, NULL, NULL, '18123792418', 1, 2, NULL, NULL, NULL, 0, '2024-01-11', NULL, 1, NULL, NULL);
INSERT INTO `teacher` VALUES (100034, NULL, NULL, '18123792419', '$2a$10$ESrWlEvvEwbVnCHYFLyWPuYiifhY6PR5gvoEtr.aLvyy77A2wwhFe', NULL, NULL, NULL, NULL, '18123792419', 1, 7, NULL, NULL, NULL, 0, '2024-01-11', NULL, 1, NULL, NULL);
INSERT INTO `teacher` VALUES (100035, NULL, NULL, '18123792420', '$2a$10$TFVs0D.YUnpYoKgU/7//EO6TUszsTOBa0TCY7pBrJN2us./yoQnEG', NULL, NULL, NULL, NULL, '18123792420', 1, 7, NULL, NULL, '430000000000000007', 0, '2024-01-11', NULL, 1, NULL, NULL);
INSERT INTO `teacher` VALUES (100036, NULL, NULL, '15623792418', '$2a$10$o7mcqxfNMYGst8qk4GciGOPUG5AGvnE53.XUFuxhC4ML3iAKe1nji', NULL, NULL, NULL, NULL, '15623792418', 1, 2, NULL, NULL, NULL, 0, '2024-01-11', NULL, 1, NULL, NULL);
INSERT INTO `teacher` VALUES (100037, NULL, NULL, '15623792419', '$2a$10$UyAelVCd9Wj58dCOgXut5OySMHKv.ExANODWbKIF8O7zzJJsk2N4C', NULL, NULL, NULL, NULL, '15623792419', 1, 7, NULL, NULL, NULL, 0, '2024-01-11', NULL, 1, NULL, NULL);
INSERT INTO `teacher` VALUES (100038, NULL, NULL, '15623792420', '$2a$10$IU0crcIYN/DKIuFGl.SU9..s1xAQbHFlwAG.sdOQXxfHKlaWZUIty', NULL, NULL, NULL, NULL, '15623792420', 1, 7, NULL, NULL, NULL, 0, '2024-01-11', NULL, 1, NULL, NULL);
INSERT INTO `teacher` VALUES (100039, NULL, NULL, '12423792410', '$2a$10$XTHMujTX74HMuZ619gk5beECEKX8suTCQSPhWaSkX8FPtCr/x6KqK', NULL, NULL, NULL, NULL, '12423792410', 1, 2, NULL, NULL, NULL, 0, '2024-01-11', NULL, 1, NULL, NULL);
INSERT INTO `teacher` VALUES (100040, NULL, NULL, '12423792418', '$2a$10$XnCRTnw5oxY8YRDj0hVMLeID4tZ6RxBArXu0KN3zE0ivgNLS1D6ry', NULL, NULL, NULL, NULL, '12423792418', 1, 7, NULL, NULL, NULL, 0, '2024-01-11', NULL, 1, NULL, NULL);
INSERT INTO `teacher` VALUES (100041, NULL, NULL, '12423792457', '$2a$10$KTEldogMU0f1JYVNjC5a3O6ofFvWOXksPcJrGVPwkh4u4i5RrWzjO', NULL, NULL, NULL, NULL, '12423792457', 1, 2, NULL, NULL, NULL, 0, '2024-01-11', NULL, 1, NULL, NULL);
INSERT INTO `teacher` VALUES (100042, NULL, NULL, '12423792456', '$2a$10$fbjDNsRhEVbly8fwuToPmubH.ufcWXB.sqgW5NrJ.iBq/Y2msKlx6', NULL, NULL, NULL, NULL, '12423792456', 1, 7, NULL, NULL, NULL, 0, '2024-01-11', NULL, 1, NULL, NULL);
INSERT INTO `teacher` VALUES (100043, NULL, '李四', '15711110000', '$2a$10$2tNk0ZWGgNazkFDPYmNyTePGE7JY0ToJJQ3K2ui1aZuBd5rIHpv6a', 0, '布朗族', '湖南省长沙市', '湖南省长沙市', '15711110000', 1, 1, '本科', '群众', '430000000000000011', 0, '2024-01-11', '2022-06-30', 0, NULL, NULL);
INSERT INTO `teacher` VALUES (100044, NULL, '张三', '15711110001', '$2a$10$HKS/1SBywoYO7D2efwTQWOlmSF0KcsPlhJJFNMFpub6vkHTzwAa8e', 0, '阿昌族', '湖南省长沙市', '湖南省长沙市', '15711110001', 1, 1, '高中', '群众', '430000000000000012', 1, '2023-05-17', '2022-05-24', 0, NULL, NULL);
INSERT INTO `teacher` VALUES (100045, 202103160111, '曾老师', '15711111122', '$2a$10$LsYgDn0aPW1a3BH8V6.y2OS0YOwTgHGaAREZbcEPzp4GGn1ocmCI6', 1, '柯尔克孜族', '湖南省长沙市', '湖南省长沙市', '15711114444', 1, 1, '大专', '共青团员', NULL, 0, '2024-04-28', '2023-05-07', 0, NULL, NULL);
INSERT INTO `teacher` VALUES (100046, 202103160112, '小天', '15722222211', '$2a$10$ravt/VZ5S9DrmrQHaLGRC.pLztXLMG0Ry4HbxPYfc9HK.ppVasHqK', NULL, NULL, NULL, NULL, '15722222211', 1, 2, NULL, NULL, NULL, 0, '2024-04-28', NULL, 1, NULL, NULL);
INSERT INTO `teacher` VALUES (100047, 202103161234, '曾老师', '15711111234', '$2a$10$0GRO3tBlemYlQDLaySPZsuw98XXIHTBaMfCkv3U90f/33noTEPrXa', 0, '景颇族', '广东省广州市', '湖南省长沙', '15711111234', 1, 1, '大专', '群众', NULL, 0, '2024-04-29', '2023-05-07', 0, NULL, NULL);
INSERT INTO `teacher` VALUES (100048, 202103161235, '小天', '15711111235', '$2a$10$kWs/bppmJFTwKNn7SZyMhuPYYUa696htJ4RWHVW6Vzx.TlTAYuXDK', NULL, NULL, NULL, NULL, '15711111235', 1, 2, NULL, NULL, NULL, 0, '2024-04-28', NULL, 1, NULL, NULL);
INSERT INTO `teacher` VALUES (100049, 20210312347, 'x老师', '18888888871', '$2a$10$Xmf7W6/2PDuRvgeLLdXTQueNK.nrvNfE5c3hDI.5TAIyKMJDqvqge', 1, '汉族', '湖南省长沙市', '湖南省长沙市', '18888888871', 1, 1, '本科', '群众', '430000000000000013', 0, '2024-05-10', '2023-05-31', 0, NULL, NULL);
INSERT INTO `teacher` VALUES (100050, 20210312348, 'y老师', '18888888891', '$2a$10$pOeSPBtSk2/slE6NqxaYpeu7Vp2FSpowUBCumqTIeWAmRZ1TLVAHa', NULL, NULL, NULL, NULL, '18888888891', 1, 2, NULL, NULL, NULL, 0, '2024-05-10', NULL, 0, NULL, NULL);
INSERT INTO `teacher` VALUES (100051, 20210312349, 'x老师', '18888888887', '$2a$10$CaO6YwRvq1TP2UIbzrInp.ldppITG7Y19jWFPG3YiVC9s.m5ErAlW', NULL, NULL, NULL, NULL, '18888888887', 1, 23, NULL, NULL, NULL, 0, '2024-05-11', NULL, 0, NULL, NULL);
INSERT INTO `teacher` VALUES (100052, 20210312350, 'y老师', '18888888889', '$2a$10$cy5JMU4m3gnwgMoQaX5ZX.ZWGzg5Dim93j4/VkKDboZltmti86Gr2', NULL, NULL, NULL, NULL, '18888888889', 1, 2, NULL, NULL, NULL, 0, '2024-05-11', NULL, 0, NULL, NULL);

-- ----------------------------
-- Table structure for teacher_role
-- ----------------------------
DROP TABLE IF EXISTS `teacher_role`;
CREATE TABLE `teacher_role`  (
  `zw_id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色_职务_关系id',
  `zw_tid` bigint NULL DEFAULT NULL COMMENT '教师id',
  `zw_rid` bigint NULL DEFAULT NULL COMMENT '职务id',
  `deleted` int NULL DEFAULT 0 COMMENT '逻辑删除 0:未删除 1:删除',
  PRIMARY KEY (`zw_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 347 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '教师-职务关系表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of teacher_role
-- ----------------------------
INSERT INTO `teacher_role` VALUES (31, 100027, 4, 0);
INSERT INTO `teacher_role` VALUES (32, 100027, 3, 0);
INSERT INTO `teacher_role` VALUES (47, 100002, 3, 0);
INSERT INTO `teacher_role` VALUES (90, 100012, 3, 0);
INSERT INTO `teacher_role` VALUES (98, 100006, 3, 0);
INSERT INTO `teacher_role` VALUES (193, 100020, 2, 0);
INSERT INTO `teacher_role` VALUES (252, 100048, 3, 0);
INSERT INTO `teacher_role` VALUES (282, 100045, 2, 0);
INSERT INTO `teacher_role` VALUES (283, 100047, 3, 0);
INSERT INTO `teacher_role` VALUES (284, 100014, 2, 0);
INSERT INTO `teacher_role` VALUES (285, 100015, 2, 0);
INSERT INTO `teacher_role` VALUES (286, 100018, 2, 0);
INSERT INTO `teacher_role` VALUES (287, 100016, 1, 0);
INSERT INTO `teacher_role` VALUES (288, 100019, 2, 0);
INSERT INTO `teacher_role` VALUES (329, 100049, 2, 0);
INSERT INTO `teacher_role` VALUES (330, 100051, 1, 0);
INSERT INTO `teacher_role` VALUES (331, 100001, 1, 0);
INSERT INTO `teacher_role` VALUES (332, 100001, 2, 0);
INSERT INTO `teacher_role` VALUES (333, 100003, 1, 0);
INSERT INTO `teacher_role` VALUES (334, 100003, 2, 0);
INSERT INTO `teacher_role` VALUES (338, 100043, 2, 0);
INSERT INTO `teacher_role` VALUES (339, 100044, 1, 0);
INSERT INTO `teacher_role` VALUES (340, 100044, 2, 0);
INSERT INTO `teacher_role` VALUES (341, 100013, 1, 0);
INSERT INTO `teacher_role` VALUES (342, 100013, 2, 0);
INSERT INTO `teacher_role` VALUES (343, 100011, 1, 0);
INSERT INTO `teacher_role` VALUES (344, 100011, 2, 0);
INSERT INTO `teacher_role` VALUES (345, 100023, 1, 0);
INSERT INTO `teacher_role` VALUES (346, 100023, 2, 0);

-- ----------------------------
-- Table structure for topic_audit
-- ----------------------------
DROP TABLE IF EXISTS `topic_audit`;
CREATE TABLE `topic_audit`  (
  `zw_id` bigint NOT NULL AUTO_INCREMENT COMMENT '课题id',
  `zw_tid` bigint NULL DEFAULT NULL COMMENT '所属教师id(创建人)',
  `zw_topic_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '课题名称',
  `zw_topic_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '课题类型',
  `zw_sta_date` date NULL DEFAULT NULL COMMENT '开始日期',
  `zw_end_date` date NULL DEFAULT NULL COMMENT '结束日期',
  `zw_team_size` int NULL DEFAULT NULL COMMENT '团队人数',
  `zw_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附件路径',
  `zw_audit_status` int NULL DEFAULT NULL COMMENT '审核状态 0:待审核 1:通过 2:驳回',
  `zw_create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `zw_audit_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `zw_aid` bigint NULL DEFAULT NULL COMMENT '审核员id',
  `zw_teacher_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '教师备注',
  `zw_auditor_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核员备注',
  `zw_is_show` int NULL DEFAULT NULL COMMENT '是否展示 0:不展示 1:展示',
  `zw_delete_roles` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除角色',
  `zw_deleted` int NULL DEFAULT 0 COMMENT '逻辑删除 0:未删除 1:删除',
  `test1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test1',
  `test2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test2',
  `test3` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test3',
  `tset4` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test4',
  PRIMARY KEY (`zw_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '课题审核' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of topic_audit
-- ----------------------------
INSERT INTO `topic_audit` VALUES (1, 100001, '<课题名称>', '天文类型', '2023-06-08', '2023-08-08', 1, NULL, 1, '2023-07-15 13:52:32', '2024-01-01 14:43:10', 200006, '希望通过审核', '审核通过！', 1, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `topic_audit` VALUES (2, 100011, 'JAVA程序开发', '计算机基础语言', '2024-01-07', '2024-01-16', 3, '', 0, '2024-01-10 15:32:20', NULL, NULL, '11111', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `topic_audit` VALUES (3, 100011, 'C语言程序设计', '计算机语言', '2023-12-31', '2024-01-15', 3, '', 0, '2024-01-10 15:33:56', NULL, NULL, '1111', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `topic_audit` VALUES (4, 100011, '1111', '1111', '2023-11-14', '2023-12-27', 10, NULL, 0, '2024-01-11 00:23:40', NULL, NULL, '111111111', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `topic_audit` VALUES (5, 100011, '关于xx的课题', '天文', '2024-04-15', '2024-04-16', 10, NULL, 0, '2024-01-11 10:01:12', NULL, NULL, '123', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `topic_audit` VALUES (6, 100001, '123', '123', '2022-05-02', '2022-09-08', 321, NULL, 0, '2024-05-08 15:24:46', NULL, NULL, '3', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for work_experience_audit
-- ----------------------------
DROP TABLE IF EXISTS `work_experience_audit`;
CREATE TABLE `work_experience_audit`  (
  `zw_id` bigint NOT NULL AUTO_INCREMENT COMMENT '工作经历审核id',
  `zw_tid` bigint NULL DEFAULT NULL COMMENT '所属教师id(创建人)',
  `zw_sta_date` date NULL DEFAULT NULL COMMENT '开始日期',
  `zw_end_date` date NULL DEFAULT NULL COMMENT '结束日期',
  `zw_company_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单位名称',
  `zw_position` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '职务',
  `zw_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '附件路径',
  `zw_audit_status` int NULL DEFAULT NULL COMMENT '审核状态 0:待审核 1:通过 2:驳回',
  `zw_create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `zw_audit_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `zw_aid` bigint NULL DEFAULT NULL COMMENT '审核员id',
  `zw_teacher_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '教师备注',
  `zw_auditor_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核员备注',
  `zw_is_show` int NULL DEFAULT NULL COMMENT '是否展示 0:不展示 1:展示',
  `zw_delete_roles` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '删除角色',
  `zw_deleted` int NULL DEFAULT 0 COMMENT '逻辑删除 0:未删除 1:删除',
  `test1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test1',
  `test2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test2',
  `test3` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test3',
  `test4` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'test4',
  PRIMARY KEY (`zw_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '工作经历审核' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of work_experience_audit
-- ----------------------------
INSERT INTO `work_experience_audit` VALUES (1, 100001, '2023-06-01', '2023-07-01', 'xxx学院', '教师', NULL, 1, '2023-07-15 13:54:31', '2023-08-17 14:47:21', 200006, '打工', '通过1', 1, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `work_experience_audit` VALUES (2, 100001, '2023-10-17', '2023-12-13', 'xxxxx大学', '教师', '', 1, '2024-01-10 15:45:04', '2024-04-30 19:52:43', 200006, '那天', 'test', 1, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `work_experience_audit` VALUES (3, 100011, '2023-12-31', '2024-01-01', '111111111111111111', '111111111111111111111', NULL, 1, '2024-01-11 00:28:03', '2024-04-28 22:37:34', 100001, '111111111111111111', '', 1, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `work_experience_audit` VALUES (4, 100001, '2023-06-01', '2023-07-19', '公司2', '实习生', NULL, 0, '2024-04-28 18:59:00', NULL, NULL, '1111', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `work_experience_audit` VALUES (5, 100001, '2024-04-03', '2024-05-12', '公司1', '职务1', NULL, 1, '2024-04-28 19:00:22', '2024-04-30 19:53:13', 200006, '123', 'test2', 1, ',', 0, NULL, NULL, NULL, NULL);
INSERT INTO `work_experience_audit` VALUES (6, 100001, '2024-05-06', '2025-06-18', 'dw', 'zw', NULL, 0, '2024-05-08 15:24:24', NULL, NULL, '12', NULL, 0, ',', 0, NULL, NULL, NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;
