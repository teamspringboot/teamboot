/*
Navicat MySQL Data Transfer

Source Server         : pp
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : manager

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2019-03-06 14:20:19
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for employee
-- ----------------------------
DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `classes` varchar(255) DEFAULT NULL,
  `office` varchar(255) DEFAULT NULL,
  `salary` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of employee
-- ----------------------------
INSERT INTO `employee` VALUES ('1', '一年一班级', '三层', '5962.02');
INSERT INTO `employee` VALUES ('2', '三年级三班', '3层', '56.02');
INSERT INTO `employee` VALUES ('3', '大神', '发送', '56.25');
INSERT INTO `employee` VALUES ('4', '发个', '热额外', '127.56');
INSERT INTO `employee` VALUES ('5', '爱迪生', '娃儿', '895.66');
INSERT INTO `employee` VALUES ('6', '二年级', '三年级', '895.56');
INSERT INTO `employee` VALUES ('7', '二年级', '三年级', '815.56');
INSERT INTO `employee` VALUES ('8', '3年级', '4年级', '8115.56');
