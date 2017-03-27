/*
Navicat MySQL Data Transfer

Source Server         : 45.78.59.214
Source Server Version : 50173
Source Host           : 45.78.59.214:3306
Source Database       : hq

Target Server Type    : MYSQL
Target Server Version : 50173
File Encoding         : 65001

Date: 2017-03-27 22:40:02
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for hq_answer
-- ----------------------------
DROP TABLE IF EXISTS `hq_answer`;
CREATE TABLE `hq_answer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `courseid` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `type` int(11) NOT NULL COMMENT '0:单选，1：多选，2：判断',
  `answera` varchar(255) DEFAULT NULL,
  `answerb` varchar(255) DEFAULT NULL,
  `answerc` varchar(255) DEFAULT NULL,
  `answerd` varchar(255) DEFAULT NULL,
  `answere` varchar(255) DEFAULT NULL,
  `answerf` varchar(255) DEFAULT NULL,
  `answerletter` varchar(255) DEFAULT NULL,
  `createtime` datetime NOT NULL,
  `time` int(11) NOT NULL DEFAULT '0' COMMENT '出现次数',
  `subjectid` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for hq_course
-- ----------------------------
DROP TABLE IF EXISTS `hq_course`;
CREATE TABLE `hq_course` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `type` int(11) NOT NULL DEFAULT '0' COMMENT '题目类型，默认为0（全为选择题）',
  `sum` int(11) NOT NULL DEFAULT '0',
  `radiosum` int(11) NOT NULL DEFAULT '0',
  `checkboxsum` int(11) NOT NULL DEFAULT '0',
  `truefalsesum` int(11) NOT NULL DEFAULT '0',
  `createtime` datetime NOT NULL,
  `updatetime` datetime NOT NULL,
  `uuid` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for hq_parse
-- ----------------------------
DROP TABLE IF EXISTS `hq_parse`;
CREATE TABLE `hq_parse` (
  `id` int(11) NOT NULL,
  `courseid` int(11) NOT NULL,
  `parsetime` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for hq_pointrecord
-- ----------------------------
DROP TABLE IF EXISTS `hq_pointrecord`;
CREATE TABLE `hq_pointrecord` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userid` int(11) NOT NULL,
  `change` int(11) NOT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `createtime` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for hq_task
-- ----------------------------
DROP TABLE IF EXISTS `hq_task`;
CREATE TABLE `hq_task` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userid` int(11) NOT NULL,
  `courseid` int(11) NOT NULL,
  `createtime` datetime NOT NULL,
  `updatetime` datetime NOT NULL,
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '0：未执行，1：执行中，2：执行成功，3：执行失败',
  `comment` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for hq_ucrelation
-- ----------------------------
DROP TABLE IF EXISTS `hq_ucrelation`;
CREATE TABLE `hq_ucrelation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userid` int(11) NOT NULL,
  `courseid` int(11) NOT NULL,
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '0:未做，1：已做',
  `createtime` datetime NOT NULL,
  `updatetime` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for hq_user
-- ----------------------------
DROP TABLE IF EXISTS `hq_user`;
CREATE TABLE `hq_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键Id',
  `uid` varchar(255) NOT NULL COMMENT '登录名',
  `password` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL COMMENT '用户名',
  `createtime` datetime NOT NULL,
  `updatetime` datetime NOT NULL,
  `lastlogintime` datetime DEFAULT NULL,
  `point` int(11) NOT NULL DEFAULT '4',
  `status` int(11) NOT NULL DEFAULT '0',
  `autoFlag` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
