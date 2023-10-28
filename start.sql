/*
 Navicat MySQL Data Transfer

 Source Server         : Demo
 Source Server Type    : MySQL
 Source Server Version : 80019
 Source Host           : localhost:3306
 Source Schema         : demo

 Target Server Type    : MySQL
 Target Server Version : 80019
 File Encoding         : 65001

 Date: 05/05/2023 15:05:15
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_admin
-- ----------------------------
DROP TABLE IF EXISTS `t_admin`;
CREATE TABLE `t_admin`  (
  `data_id` bigint NOT NULL AUTO_INCREMENT COMMENT '数据id',
  `admin_id` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '管理员账号',
  `admin_password` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '管理员密码',
  `admin_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '管理员名称',
  `email_number` char(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱号码',
  `password_err_count` int UNSIGNED NOT NULL COMMENT '当天密码错误次数',
  `admin_status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '管理员状态\r\nA：活动\r\nC：已删除\r\nD：停用',
  `admin_role` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '管理员角色信息',
  `spec_code` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '冗余信息（1-6：邮箱验证码）',
  PRIMARY KEY (`data_id`) USING BTREE,
  UNIQUE INDEX `index_1`(`admin_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_admin
-- ----------------------------
INSERT INTO `t_admin` VALUES (1, 'admin', '0a9', 'admin', NULL, 0, 'A', 'admin', ' ');

-- ----------------------------
-- Table structure for t_cardholder
-- ----------------------------
DROP TABLE IF EXISTS `t_cardholder`;
CREATE TABLE `t_cardholder`  (
  `ci_tr_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '支付ID',
  `ci_mer_no` char(6) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户账户号',
  `ci_mer_order_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户订单号',
  `ci_card_number` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '卡号（后四位）',
  `ci_billing_firstname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'billing firstname',
  `ci_billing_lastname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'billing lastname',
  `ci_billing_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'billing address',
  `ci_billing_city` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'billing city',
  `ci_billing_state` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'billing state',
  `ci_billing_zip` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'billing zip',
  `ci_billing_country` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'billing country',
  `ci_billing_phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'billing phone',
  `ci_billing_email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'billing email',
  `ci_shipping_firstname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'shipping firstname',
  `ci_shipping_lastname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'shipping lastname',
  `ci_shipping_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'shipping address',
  `ci_shipping_city` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'shipping city',
  `ci_shipping_state` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'shipping state',
  `ci_shipping_zip` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'shipping zip',
  `ci_shipping_country` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'shipping country',
  `ci_shipping_phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'ci_shipping_phone',
  `ci_shipping_email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'shipping email',
  `ci_customer_ip` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '消费者IP',
  `ci_create_datetime` char(17) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建时间',
  `ci_update_datetime` char(17) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '修改时间',
  `spec_code` varchar(400) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '冗余信息',
  `maintain_api` char(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '维护APINAME',
  `maintain_time` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '维护时间',
  `maintain_version` int NOT NULL COMMENT '维护版本',
  `maintain_database` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '维护数据库',
  PRIMARY KEY (`ci_tr_no`) USING BTREE,
  UNIQUE INDEX `index_1`(`ci_tr_no`) USING BTREE COMMENT '订单号'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_cardholder
-- ----------------------------

-- ----------------------------
-- Table structure for t_channel
-- ----------------------------
DROP TABLE IF EXISTS `t_channel`;
CREATE TABLE `t_channel`  (
  `channel_id` bigint NOT NULL AUTO_INCREMENT COMMENT '通道ID',
  `channel_name` char(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '通道名称',
  `payment_bank_id` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '支付银行',
  `payment_method_id` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '支付方式',
  `channel_day_limit` decimal(10, 2) NOT NULL COMMENT '通道每日总交易限额',
  `channel_balance_threshold` decimal(10, 2) NOT NULL COMMENT '提醒额度',
  `channel_status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '通道状态',
  PRIMARY KEY (`channel_id`) USING BTREE,
  UNIQUE INDEX `index_1`(`channel_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_channel
-- ----------------------------

-- ----------------------------
-- Table structure for t_merchant
-- ----------------------------
DROP TABLE IF EXISTS `t_merchant`;
CREATE TABLE `t_merchant`  (
  `merchant_id` bigint NOT NULL AUTO_INCREMENT,
  `company_name` char(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `website_type` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `channel_id` int NOT NULL,
  `channel_mid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `channel_key` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `channel_secure_key` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `merchant_sign_key` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`merchant_id`) USING BTREE,
  UNIQUE INDEX `index_1`(`merchant_id`) USING BTREE,
  INDEX `index_2`(`company_name`, `website_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_merchant
-- ----------------------------

-- ----------------------------
-- Table structure for t_token
-- ----------------------------
DROP TABLE IF EXISTS `t_token`;
CREATE TABLE `t_token`  (
  `data_id` bigint NOT NULL AUTO_INCREMENT COMMENT '数据自增ID',
  `admin_id` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户账号',
  `admin_token` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '登录token',
  `admin_login_time` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '上次登录时间',
  `admin_expire_time` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '登录失效时间',
  PRIMARY KEY (`data_id`) USING BTREE,
  UNIQUE INDEX `index_1`(`admin_id`, `admin_token`) USING BTREE COMMENT '唯一索引'
) ENGINE = InnoDB AUTO_INCREMENT = 1906 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_token
-- ----------------------------

-- ----------------------------
-- Table structure for t_transaction_block
-- ----------------------------
DROP TABLE IF EXISTS `t_transaction_block`;
CREATE TABLE `t_transaction_block`  (
  `trans_id` int NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `trans_no` varchar(21) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '支付id',
  `trans_mer_order_no` char(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户订单号',
  `trans_mer_account` varchar(6) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户账户号',
  `trans_amount` decimal(20, 2) NOT NULL COMMENT '交易金额',
  `trans_currency` varchar(3) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '交易币种',
  `trans_result_code` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '交易响应代码',
  `trans_result_info` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '交易响应说明',
  `trans_cha_id` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '通道id',
  `trans_datetime` char(17) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '交易时间',
  `trans_bank_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '银行订单号',
  `trans_notice_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '交易推送地址',
  `spec_code` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '冗余信息',
  `maintain_api` char(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '维护APINAME',
  `maintain_time` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '维护时间',
  `maintain_version` int NOT NULL COMMENT '维护版本',
  `maintain_database` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '维护数据库',
  PRIMARY KEY (`trans_id`) USING BTREE,
  INDEX `index_1`(`trans_mer_order_no`, `trans_mer_account`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 40791 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_transaction_block
-- ----------------------------

-- ----------------------------
-- Table structure for t_transaction_normal
-- ----------------------------
DROP TABLE IF EXISTS `t_transaction_normal`;
CREATE TABLE `t_transaction_normal`  (
  `trans_id` int NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `trans_no` varchar(21) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '支付id',
  `trans_mer_order_no` char(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户订单号',
  `trans_mer_account` varchar(6) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商户账户号',
  `trans_amount` decimal(20, 2) NOT NULL COMMENT '交易金额',
  `trans_currency` varchar(3) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '交易币种',
  `trans_result_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '交易响应代码',
  `trans_result_info` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '交易响应说明',
  `trans_status` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '交易状态：A.成功 F.失败 P.待处理',
  `trans_cha_id` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '通道id',
  `trans_datetime` char(17) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '交易时间',
  `trans_bank_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '银行订单号',
  `trans_notice_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '交易推送地址',
  `spec_code` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '冗余信息',
  `maintain_api` char(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '维护APINAME',
  `maintain_time` char(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '维护时间',
  `maintain_version` int NOT NULL COMMENT '维护版本',
  `maintain_database` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '维护数据库',
  PRIMARY KEY (`trans_id`) USING BTREE,
  UNIQUE INDEX `index_1`(`trans_mer_order_no`, `trans_mer_account`) USING BTREE,
  UNIQUE INDEX `index_2`(`trans_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 46882 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_transaction_normal
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
