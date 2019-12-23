/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 50728
 Source Host           : localhost:3306
 Source Schema         : satan-admin

 Target Server Type    : MySQL
 Target Server Version : 50728
 File Encoding         : 65001

 Date: 23/12/2019 17:57:32
*/

SET NAMES utf8mb4;$$
SET FOREIGN_KEY_CHECKS = 0;$$

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;$$
CREATE TABLE `menu`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `data_status` int(11) NOT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `scope` int(11) NULL DEFAULT NULL,
  `update_date` datetime(0) NULL DEFAULT NULL,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `display` bit(1) NULL DEFAULT NULL,
  `icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `permission` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `router` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `sort` int(11) NULL DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `creator_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `updator_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `parent_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKh3gqj3lncchrmgd8pgvi2q0t3`(`creator_id`) USING BTREE,
  INDEX `FKn0wwa61q2djfeh6djsllunibe`(`updator_id`) USING BTREE,
  INDEX `FKgeupubdqncc1lpgf2cn4fqwbc`(`parent_id`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;$$

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES ('0', NULL, 0, NULL, NULL, '2019-12-11 19:50:48', 'SystemManager', b'1', 'el-icon-third-xinzengdaohangliebiao', 'SystemManager', '/sys', 10, '系统管理', '1', '1', NULL);$$
INSERT INTO `menu` VALUES ('1', NULL, 0, NULL, NULL, '2019-10-31 11:34:17', 'MenuManager', b'1', 'el-icon-third-xinzengdaohangliebiao', 'MuneManager', '/sys/menu', 30, '菜单管理', NULL, '1', '0');$$
INSERT INTO `menu` VALUES ('11', NULL, 0, NULL, NULL, NULL, 'CreateMenu', b'0', NULL, 'CreateMenu', '/sys/menu/create', 10, '创建菜单', NULL, NULL, '1');$$
INSERT INTO `menu` VALUES ('12', '2019-10-31 11:24:18', 0, NULL, NULL, '2019-10-31 11:24:18', 'UpdateMenu', b'0', NULL, NULL, '/sys/menu/update', 20, '编辑菜单', '1', '1', '1');$$
INSERT INTO `menu` VALUES ('13', '2019-10-31 11:24:32', 0, NULL, NULL, '2019-10-31 11:24:32', 'DeleteMenu', b'0', NULL, NULL, '/sys/menu', 30, '删除菜单', '1', '1', '1');$$
INSERT INTO `menu` VALUES ('2', '2019-10-31 11:24:55', 0, NULL, NULL, '2019-10-31 11:34:29', 'RoleManager', b'1', 'el-icon-third-zhuti_yifu', NULL, '/sys/role', 30, '角色管理', NULL, '1', '1');$$
INSERT INTO `menu` VALUES ('21', '2019-10-31 11:25:09', 0, NULL, NULL, '2019-10-31 11:25:09', 'CreateRole', b'0', NULL, NULL, '/sys/role/create', 10, '创建角色', '1', '1', '22');$$
INSERT INTO `menu` VALUES ('22', '2019-10-31 11:25:26', 0, NULL, NULL, '2019-10-31 11:25:26', 'UpdateRole', b'0', NULL, NULL, '/sys/role/update', 20, '编辑角色', '1', '1', '22');$$
INSERT INTO `menu` VALUES ('23', '2019-10-31 11:25:40', 0, NULL, NULL, '2019-10-31 11:25:40', 'DeleteRole', b'0', NULL, NULL, '/sys/role', 30, '删除角色', '1', '1', '22');$$
INSERT INTO `menu` VALUES ('-1', NULL, 0, NULL, NULL, NULL, 'Home', b'1', NULL, 'Home', '/home', 0, '首页', NULL, NULL, NULL);$$
INSERT INTO `menu` VALUES ('3', '2019-10-31 11:35:00', 0, NULL, NULL, '2019-10-31 11:35:00', 'UserManager', b'1', 'el-icon-third-qunzu', NULL, '/sys/user', 30, '用户管理', '1', '1', '1');$$
INSERT INTO `menu` VALUES ('31', '2019-10-31 11:35:14', 0, NULL, NULL, '2019-10-31 11:35:14', 'CreateUser', b'0', NULL, NULL, '/sys/user/create', 10, '添加用户', '1', '1', '3');$$
INSERT INTO `menu` VALUES ('32', '2019-10-31 11:35:30', 0, NULL, NULL, '2019-10-31 11:35:30', 'UpdateUser', b'0', NULL, NULL, '/sys/user/update', 20, '编辑用户', '1', '1', '3');$$
INSERT INTO `menu` VALUES ('33', '2019-10-31 11:35:44', 0, NULL, NULL, '2019-10-31 11:35:44', 'DeleteUser', b'0', NULL, NULL, '/sys/user', 30, '删除用户', '1', '1', '3');$$
INSERT INTO `menu` VALUES ('4', '2019-10-31 11:36:39', 0, NULL, NULL, '2019-10-31 11:36:39', 'DictionaryManager', b'1', 'el-icon-third-wuliudanao', NULL, '/sys/dict', 40, '字典管理', '1', '1', '1');$$
INSERT INTO `menu` VALUES ('41', '2019-10-31 11:37:00', 0, NULL, NULL, '2019-10-31 11:37:00', 'CreateDictionary', b'0', NULL, NULL, '/sys/dict/create', 10, '添加字典', '1', '1', '4');$$
INSERT INTO `menu` VALUES ('42', '2019-10-31 11:37:20', 0, NULL, NULL, '2019-10-31 11:37:20', 'UpdateDictionary', b'0', NULL, NULL, '/sys/dict/update', 20, '编辑字典', '1', '1', '4');$$
INSERT INTO `menu` VALUES ('43', '2019-10-31 11:37:34', 0, NULL, NULL, '2019-10-31 11:37:34', 'DeleteDictionary', b'0', NULL, NULL, '/sys/dict', 30, '删除字典', '1', '1', '4');$$
INSERT INTO `menu` VALUES ('5', '2019-10-31 11:38:30', 0, NULL, NULL, '2019-10-31 11:38:30', 'OrgManager', b'1', 'el-icon-third-shengyin', NULL, '/sys/org', 50, '组织机构管理', '1', '1', '1');$$
INSERT INTO `menu` VALUES ('51', '2019-10-31 11:38:45', 0, NULL, NULL, '2019-10-31 11:38:45', 'CreateOrg', b'0', NULL, NULL, '/sys/org/create', 10, '添加组织机构', '1', '1', '5');$$
INSERT INTO `menu` VALUES ('52', '2019-10-31 11:39:04', 0, NULL, NULL, '2019-10-31 11:39:04', 'UpdateOrg', b'0', NULL, NULL, '/sys/org/update', 20, '编辑组织机构', '1', '1', '5');$$
INSERT INTO `menu` VALUES ('53', '2019-10-31 11:39:20', 0, NULL, NULL, '2019-10-31 11:39:20', 'DeleteOrg', b'0', NULL, NULL, '/sys/org', 30, '删除组织机构', '1', '1', '5');$$
INSERT INTO `menu` VALUES ('6', NULL, 0, NULL, NULL, '2019-12-12 17:21:16', 'ResourceManager', b'1', 'el-icon-third-wenjianjia', NULL, '/sys/file', 70, '资源管理', NULL, '1', '1');$$
INSERT INTO `menu` VALUES ('61', '2019-10-31 11:40:08', 0, NULL, NULL, '2019-10-31 11:40:08', 'CreateResource', b'0', NULL, NULL, '/sys/file/create', 10, '添加资源', '1', '1', '6');$$
INSERT INTO `menu` VALUES ('62', '2019-10-31 11:40:29', 0, NULL, NULL, '2019-10-31 11:40:29', 'UpdateResource', b'0', NULL, NULL, '/sys/file/update', 20, '编辑资源', '1', '1', '6');$$
INSERT INTO `menu` VALUES ('63', '2019-10-31 11:40:42', 0, NULL, NULL, '2019-10-31 11:40:42', 'DeleteResource', b'0', NULL, NULL, '/sys/file', 30, '删除资源', '1', '1', '6');$$

-- ----------------------------
-- Table structure for r_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `r_role_menu`;$$
CREATE TABLE `r_role_menu`  (
  `role_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `menus_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`role_id`, `menus_id`) USING BTREE,
  INDEX `FKam7dej0ruo4yrrap5vongqnnj`(`menus_id`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;$$

-- ----------------------------
-- Records of r_role_menu
-- ----------------------------
INSERT INTO `r_role_menu` VALUES ('1', '-1');$$
INSERT INTO `r_role_menu` VALUES ('1', '0');$$
INSERT INTO `r_role_menu` VALUES ('1', '1');$$
INSERT INTO `r_role_menu` VALUES ('1', '11');$$
INSERT INTO `r_role_menu` VALUES ('1', '12');$$
INSERT INTO `r_role_menu` VALUES ('1', '13');$$
INSERT INTO `r_role_menu` VALUES ('1', '2');$$
INSERT INTO `r_role_menu` VALUES ('1', '21');$$
INSERT INTO `r_role_menu` VALUES ('1', '22');$$
INSERT INTO `r_role_menu` VALUES ('1', '23');$$
INSERT INTO `r_role_menu` VALUES ('1', '3');$$
INSERT INTO `r_role_menu` VALUES ('1', '31');$$
INSERT INTO `r_role_menu` VALUES ('1', '32');$$
INSERT INTO `r_role_menu` VALUES ('1', '33');$$
INSERT INTO `r_role_menu` VALUES ('1', '4');$$
INSERT INTO `r_role_menu` VALUES ('1', '41');$$
INSERT INTO `r_role_menu` VALUES ('1', '42');$$
INSERT INTO `r_role_menu` VALUES ('1', '43');$$
INSERT INTO `r_role_menu` VALUES ('1', '5');$$
INSERT INTO `r_role_menu` VALUES ('1', '51');$$
INSERT INTO `r_role_menu` VALUES ('1', '52');$$
INSERT INTO `r_role_menu` VALUES ('1', '53');$$
INSERT INTO `r_role_menu` VALUES ('1', '6');$$
INSERT INTO `r_role_menu` VALUES ('1', '61');$$
INSERT INTO `r_role_menu` VALUES ('1', '62');$$
INSERT INTO `r_role_menu` VALUES ('1', '63');$$

-- ----------------------------
-- Table structure for r_user_role
-- ----------------------------
DROP TABLE IF EXISTS `r_user_role`;$$
CREATE TABLE `r_user_role`  (
  `user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `role_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE,
  INDEX `FKqtburc3pvgt0jbgwm07etp7jk`(`role_id`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;$$

-- ----------------------------
-- Records of r_user_role
-- ----------------------------
INSERT INTO `r_user_role` VALUES ('1', '1');$$

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;$$
CREATE TABLE `role`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `data_status` int(11) NOT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `scope` int(11) NULL DEFAULT NULL,
  `update_date` datetime(0) NULL DEFAULT NULL,
  `code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `creator_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `updator_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKgiycbu32u3ohnrmlp1xpl7bh4`(`creator_id`) USING BTREE,
  INDEX `FK8jyuy60r3ft63m8br3y5v18vt`(`updator_id`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;$$

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('1', '2019-12-23 17:48:19', 0, NULL, NULL, NULL, 'Administrator', '超级管理员', '1', NULL);$$

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;$$
CREATE TABLE `user`  (
  `id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `data_status` int(11) NOT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `scope` int(11) NULL DEFAULT NULL,
  `update_date` datetime(0) NULL DEFAULT NULL,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `birthday` datetime(0) NULL DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `login_date` datetime(0) NULL DEFAULT NULL,
  `mobile_phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `open_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `sex` int(11) NULL DEFAULT NULL,
  `tel_phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `creator_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `updator_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `org_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FKrlj77ymttuppjd3f7xvuk7x1q`(`creator_id`) USING BTREE,
  INDEX `FKqln1nqcyytknnthdei41nilv1`(`updator_id`) USING BTREE,
  INDEX `FK9peig4uqx28xcq0gy893px0f9`(`org_id`) USING BTREE
) ENGINE = MyISAM CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;$$

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, '2019-12-23 17:44:20', NULL, NULL, NULL, 'e10adc3949ba59abbe56e057f20f883e', NULL, NULL, 'admin', NULL, NULL, NULL);$$

SET FOREIGN_KEY_CHECKS = 1;$$
