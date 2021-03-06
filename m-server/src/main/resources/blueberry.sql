/*
 Navicat Premium Data Transfer

 Source Server         : localhost mysql5.7
 Source Server Type    : MySQL
 Source Server Version : 50734
 Source Host           : localhost:3306
 Source Schema         : blueberry

 Target Server Type    : MySQL
 Target Server Version : 50734
 File Encoding         : 65001

 Date: 22/09/2021 10:41:45
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for qrtz_blob_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_blob_triggers`;
CREATE TABLE `qrtz_blob_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `BLOB_DATA` blob NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_calendars
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_calendars`;
CREATE TABLE `qrtz_calendars`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `CALENDAR_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `CALENDAR` blob NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `CALENDAR_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_cron_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_cron_triggers`;
CREATE TABLE `qrtz_cron_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `CRON_EXPRESSION` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `TIME_ZONE_ID` varchar(80) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_cron_triggers
-- ----------------------------
INSERT INTO `qrtz_cron_triggers` VALUES ('clusteredScheduler', '??????????????????', 'stream', '0 0/3 * * * ? ', 'Asia/Shanghai');
INSERT INTO `qrtz_cron_triggers` VALUES ('clusteredScheduler', '????????????????????????', 'stream', '0 0/5 * * * ? ', 'Asia/Shanghai');
INSERT INTO `qrtz_cron_triggers` VALUES ('clusteredScheduler', '????????????????????????', 'device', '0 0/1 * * * ? ', 'Asia/Shanghai');

-- ----------------------------
-- Table structure for qrtz_fired_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_fired_triggers`;
CREATE TABLE `qrtz_fired_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `ENTRY_ID` varchar(95) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `INSTANCE_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `FIRED_TIME` bigint(13) NOT NULL,
  `SCHED_TIME` bigint(13) NOT NULL,
  `PRIORITY` int(11) NOT NULL,
  `STATE` varchar(16) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `JOB_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `JOB_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `IS_NONCONCURRENT` varchar(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `REQUESTS_RECOVERY` varchar(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `ENTRY_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_job_details
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_job_details`;
CREATE TABLE `qrtz_job_details`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `JOB_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `JOB_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `DESCRIPTION` varchar(250) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `JOB_CLASS_NAME` varchar(250) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `IS_DURABLE` varchar(1) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `IS_NONCONCURRENT` varchar(1) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `IS_UPDATE_DATA` varchar(1) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `REQUESTS_RECOVERY` varchar(1) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `JOB_DATA` blob NULL,
  PRIMARY KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_job_details
-- ----------------------------
INSERT INTO `qrtz_job_details` VALUES ('clusteredScheduler', '??????????????????', 'stream', '????????????????????????????????????????????????????????????????????????', 'fun.bookish.blueberry.server.schedule.VideoNoReaderCheckTask', '0', '0', '0', '0', 0xACED0005737200156F72672E71756172747A2E4A6F62446174614D61709FB083E8BFA9B0CB020000787200266F72672E71756172747A2E7574696C732E537472696E674B65794469727479466C61674D61708208E8C3FBC55D280200015A0013616C6C6F77735472616E7369656E74446174617872001D6F72672E71756172747A2E7574696C732E4469727479466C61674D617013E62EAD28760ACE0200025A000564697274794C00036D617074000F4C6A6176612F7574696C2F4D61703B787000737200116A6176612E7574696C2E486173684D61700507DAC1C31660D103000246000A6C6F6164466163746F724900097468726573686F6C6478703F40000000000010770800000010000000007800);
INSERT INTO `qrtz_job_details` VALUES ('clusteredScheduler', '????????????????????????', 'stream', '???????????????????????????????????????????????????????????????????????????????????????', 'fun.bookish.blueberry.server.schedule.VideoQualityDetectTask', '0', '0', '0', '0', 0xACED0005737200156F72672E71756172747A2E4A6F62446174614D61709FB083E8BFA9B0CB020000787200266F72672E71756172747A2E7574696C732E537472696E674B65794469727479466C61674D61708208E8C3FBC55D280200015A0013616C6C6F77735472616E7369656E74446174617872001D6F72672E71756172747A2E7574696C732E4469727479466C61674D617013E62EAD28760ACE0200025A000564697274794C00036D617074000F4C6A6176612F7574696C2F4D61703B787000737200116A6176612E7574696C2E486173684D61700507DAC1C31660D103000246000A6C6F6164466163746F724900097468726573686F6C6478703F40000000000010770800000010000000007800);
INSERT INTO `qrtz_job_details` VALUES ('clusteredScheduler', '????????????????????????', 'device', '????????????????????????????????????????????????????????????????????????', 'fun.bookish.blueberry.server.schedule.DeviceOnlineCheckTask', '0', '0', '0', '0', 0xACED0005737200156F72672E71756172747A2E4A6F62446174614D61709FB083E8BFA9B0CB020000787200266F72672E71756172747A2E7574696C732E537472696E674B65794469727479466C61674D61708208E8C3FBC55D280200015A0013616C6C6F77735472616E7369656E74446174617872001D6F72672E71756172747A2E7574696C732E4469727479466C61674D617013E62EAD28760ACE0200025A000564697274794C00036D617074000F4C6A6176612F7574696C2F4D61703B787000737200116A6176612E7574696C2E486173684D61700507DAC1C31660D103000246000A6C6F6164466163746F724900097468726573686F6C6478703F40000000000010770800000010000000007800);

-- ----------------------------
-- Table structure for qrtz_locks
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_locks`;
CREATE TABLE `qrtz_locks`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `LOCK_NAME` varchar(40) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `LOCK_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_locks
-- ----------------------------
INSERT INTO `qrtz_locks` VALUES ('clusteredScheduler', 'TRIGGER_ACCESS');

-- ----------------------------
-- Table structure for qrtz_paused_trigger_grps
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;
CREATE TABLE `qrtz_paused_trigger_grps`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_GROUP`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_scheduler_state
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_scheduler_state`;
CREATE TABLE `qrtz_scheduler_state`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `INSTANCE_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `LAST_CHECKIN_TIME` bigint(13) NOT NULL,
  `CHECKIN_INTERVAL` bigint(13) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `INSTANCE_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_simple_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simple_triggers`;
CREATE TABLE `qrtz_simple_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `REPEAT_COUNT` bigint(7) NOT NULL,
  `REPEAT_INTERVAL` bigint(12) NOT NULL,
  `TIMES_TRIGGERED` bigint(10) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_simprop_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simprop_triggers`;
CREATE TABLE `qrtz_simprop_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `STR_PROP_1` varchar(512) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `STR_PROP_2` varchar(512) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `STR_PROP_3` varchar(512) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `INT_PROP_1` int(11) NULL DEFAULT NULL,
  `INT_PROP_2` int(11) NULL DEFAULT NULL,
  `LONG_PROP_1` bigint(20) NULL DEFAULT NULL,
  `LONG_PROP_2` bigint(20) NULL DEFAULT NULL,
  `DEC_PROP_1` decimal(13, 4) NULL DEFAULT NULL,
  `DEC_PROP_2` decimal(13, 4) NULL DEFAULT NULL,
  `BOOL_PROP_1` varchar(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `BOOL_PROP_2` varchar(1) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_triggers`;
CREATE TABLE `qrtz_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `JOB_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `JOB_GROUP` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `DESCRIPTION` varchar(250) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `NEXT_FIRE_TIME` bigint(13) NULL DEFAULT NULL,
  `PREV_FIRE_TIME` bigint(13) NULL DEFAULT NULL,
  `PRIORITY` int(11) NULL DEFAULT NULL,
  `TRIGGER_STATE` varchar(16) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `TRIGGER_TYPE` varchar(8) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `START_TIME` bigint(13) NOT NULL,
  `END_TIME` bigint(13) NULL DEFAULT NULL,
  `CALENDAR_NAME` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `MISFIRE_INSTR` smallint(2) NULL DEFAULT NULL,
  `JOB_DATA` blob NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  INDEX `SCHED_NAME`(`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) USING BTREE,
  CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `qrtz_job_details` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_triggers
-- ----------------------------
INSERT INTO `qrtz_triggers` VALUES ('clusteredScheduler', '??????????????????', 'stream', '??????????????????', 'stream', '????????????????????????????????????????????????????????????????????????', 1632278520000, 1632278340000, 5, 'PAUSED', 'CRON', 1622701755000, 0, NULL, 2, '');
INSERT INTO `qrtz_triggers` VALUES ('clusteredScheduler', '????????????????????????', 'stream', '????????????????????????', 'stream', '???????????????????????????????????????????????????????????????????????????????????????', 1632278400000, 1626082500000, 5, 'PAUSED', 'CRON', 1626082487000, 0, NULL, 2, '');
INSERT INTO `qrtz_triggers` VALUES ('clusteredScheduler', '????????????????????????', 'device', '????????????????????????', 'device', '????????????????????????????????????????????????????????????????????????', 1632278460000, 1632278400000, 5, 'WAITING', 'CRON', 1623403820000, 0, NULL, 2, '');

-- ----------------------------
-- Table structure for t_channel
-- ----------------------------
DROP TABLE IF EXISTS `t_channel`;
CREATE TABLE `t_channel`  (
  `id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '??????id',
  `device_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '??????/??????/????????????',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '??????/??????/????????????',
  `manufacturer` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '????????????',
  `model` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '????????????',
  `owner` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '????????????',
  `civil_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '????????????',
  `block` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '??????',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '????????????',
  `parental` int(1) NOT NULL COMMENT '??????????????????',
  `parent_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '?????????/??????/?????? ID',
  `parent_channel_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '?????????ID',
  `safety_way` int(1) NULL DEFAULT NULL COMMENT '??????????????????',
  `register_way` int(1) NULL DEFAULT NULL COMMENT '????????????',
  `cert_num` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '???????????????',
  `certifiable` int(1) NULL DEFAULT NULL COMMENT '??????????????????',
  `err_code` int(11) NULL DEFAULT NULL COMMENT '???????????????',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '?????????????????????',
  `secrecy` int(1) NULL DEFAULT NULL COMMENT '????????????',
  `ip_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '??????/??????/?????? IP ??????',
  `port` int(11) NULL DEFAULT NULL COMMENT '??????/??????/????????????',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '????????????',
  `status` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '????????????',
  `longitude` double NULL DEFAULT NULL COMMENT '??????',
  `latitude` double NULL DEFAULT NULL COMMENT '??????',
  `ptz_type` int(11) NULL DEFAULT NULL COMMENT '?????????????????????',
  `position_type` int(11) NULL DEFAULT NULL COMMENT '???????????????????????????',
  `room_type` int(1) NULL DEFAULT NULL COMMENT '????????????',
  `use_type` int(1) NULL DEFAULT NULL COMMENT '???????????????',
  `supply_light_type` int(1) NULL DEFAULT NULL COMMENT '???????????????',
  `direction_type` int(1) NULL DEFAULT NULL COMMENT '?????????????????????',
  `resolution` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '???????????????????????????',
  `business_group_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '????????????????????????????????? ID',
  `download_speed` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '??????????????????',
  `svc_space_support_mode` int(1) NULL DEFAULT NULL COMMENT '??????????????????',
  `svc_time_support_mode` int(1) NULL DEFAULT NULL COMMENT '??????????????????',
  `rtsp` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'rtsp????????????',
  `created_at` datetime(0) NULL DEFAULT NULL COMMENT '????????????',
  `updated_at` datetime(0) NULL DEFAULT NULL COMMENT '????????????',
  PRIMARY KEY (`id`, `device_id`) USING BTREE,
  INDEX `idx_t_channels_name`(`name`) USING BTREE,
  INDEX `idx_t_channels_parent_id`(`parent_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '???????????????' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for t_device
-- ----------------------------
DROP TABLE IF EXISTS `t_device`;
CREATE TABLE `t_device`  (
  `id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '????????????',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '????????????',
  `manufacturer` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '???????????????',
  `model` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '????????????',
  `firmware` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '??????????????????',
  `type` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '????????????',
  `command_transport` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'SIP??????????????????????????????GB?????????',
  `remote_ip` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '??????ip??????',
  `remote_port` int(11) NOT NULL DEFAULT 0 COMMENT '???????????????',
  `online` tinyint(1) NOT NULL DEFAULT 0 COMMENT '???????????????0?????????1?????????',
  `sip_server_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'sip???????????????????????????GB?????????',
  `expires` int(11) NULL DEFAULT 3600 COMMENT '?????????????????????????????????????????????GB?????????',
  `last_register_at` datetime(0) NULL DEFAULT NULL COMMENT '????????????????????????????????????GB?????????',
  `last_keepalive_at` datetime(0) NULL DEFAULT NULL COMMENT '????????????????????????????????????GB?????????',
  `created_at` datetime(0) NOT NULL COMMENT '????????????',
  `updated_at` datetime(0) NOT NULL COMMENT '????????????',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_t_devices_name`(`name`) USING BTREE,
  INDEX `idx_t_devices_type`(`type`) USING BTREE,
  INDEX `idx_t_devices_online`(`online`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '?????????' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for t_video_quality_detect_arithmetic
-- ----------------------------
DROP TABLE IF EXISTS `t_video_quality_detect_arithmetic`;
CREATE TABLE `t_video_quality_detect_arithmetic`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '??????',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '??????',
  `settings` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '????????????',
  `priority` int(11) NOT NULL COMMENT '?????????',
  `enable` tinyint(1) NOT NULL DEFAULT 1 COMMENT '???????????????0?????????1?????????',
  `apply_all` tinyint(1) NOT NULL DEFAULT 1 COMMENT '??????????????????????????????0?????????1?????????',
  `created_at` datetime(0) NOT NULL COMMENT '????????????',
  `updated_at` datetime(0) NOT NULL COMMENT '????????????',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '????????????????????????' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_video_quality_detect_arithmetic
-- ----------------------------
INSERT INTO `t_video_quality_detect_arithmetic` VALUES (12, 'NOISE', '????????????', '{\"threshold\":30.0}', 2, 1, 1, '2021-06-25 10:09:09', '2021-07-07 11:22:45');
INSERT INTO `t_video_quality_detect_arithmetic` VALUES (13, 'STRIPE', '????????????', '{\"threshold\":0.008}', 3, 1, 1, '2021-06-25 10:09:36', '2021-06-30 17:18:43');
INSERT INTO `t_video_quality_detect_arithmetic` VALUES (14, 'COLOR_CAST', '????????????', '{\"threshold\":2.0}', 4, 1, 1, '2021-06-25 10:09:49', '2021-06-30 17:18:47');
INSERT INTO `t_video_quality_detect_arithmetic` VALUES (15, 'BRIGHTNESS', '????????????', '{\"threshold\":0}', 5, 1, 1, '2021-06-25 10:09:58', '2021-06-30 17:18:50');
INSERT INTO `t_video_quality_detect_arithmetic` VALUES (16, 'SHARPNESS', '???????????????', '{\"threshold\":10}', 6, 1, 1, '2021-06-25 10:10:03', '2021-06-30 17:18:55');
INSERT INTO `t_video_quality_detect_arithmetic` VALUES (17, 'ANGLE_CHANGE', '??????????????????', '{\"threshold\":30}', 7, 1, 0, '2021-06-25 10:10:14', '2021-06-30 17:18:59');
INSERT INTO `t_video_quality_detect_arithmetic` VALUES (18, 'COVER', '????????????', '{\"threshold\":0.30}', 1, 1, 1, '2021-06-30 17:18:32', '2021-07-12 14:36:22');

-- ----------------------------
-- Table structure for t_video_quality_detect_arithmetic_apply_device
-- ----------------------------
DROP TABLE IF EXISTS `t_video_quality_detect_arithmetic_apply_device`;
CREATE TABLE `t_video_quality_detect_arithmetic_apply_device`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `arithmetic_id` int(11) NOT NULL COMMENT '??????id',
  `device_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '??????id',
  `channel_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '??????id',
  `created_at` datetime(0) NOT NULL COMMENT '????????????',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '???????????????????????????????????????' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_video_quality_detect_record
-- ----------------------------
DROP TABLE IF EXISTS `t_video_quality_detect_record`;
CREATE TABLE `t_video_quality_detect_record`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `arithmetic_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '????????????',
  `arithmetic_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '????????????',
  `device_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '??????id',
  `channel_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '??????id',
  `image_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '??????????????????',
  `has_error` tinyint(1) NOT NULL COMMENT '????????????',
  `error` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '????????????',
  `has_quality_error` tinyint(1) NULL DEFAULT NULL COMMENT '????????????????????????',
  `quality_error` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '??????????????????',
  `detail` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT '??????????????????',
  `created_at` datetime(0) NOT NULL COMMENT '????????????',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_t_devices_name`(`channel_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '???????????????????????????' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for t_video_stream
-- ----------------------------
DROP TABLE IF EXISTS `t_video_stream`;
CREATE TABLE `t_video_stream`  (
  `id` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `device_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '??????id',
  `channel_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '??????id',
  `type` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '??????',
  `ssrc` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'ssrc',
  `flv` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'flv??????',
  `stop_params` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '????????????',
  `status` tinyint(1) NOT NULL COMMENT '???????????????0???????????????1???????????????',
  `created_at` datetime(0) NOT NULL COMMENT '????????????',
  `updated_at` datetime(0) NULL DEFAULT NULL COMMENT '????????????',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_t_devices_name`(`channel_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '????????????' ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
