CREATE TABLE `ana_account` (
  `sid` bigint(15) NOT NULL,
  `login_name` varchar(64) DEFAULT NULL,
  `nick_name` varchar(90) DEFAULT NULL,
  `real_name` varchar(60) DEFAULT NULL,
  `mobile` varchar(15) DEFAULT NULL,
  `email` varchar(128) DEFAULT NULL,
  `password` varchar(256) DEFAULT NULL,
  `gender` varchar(20) DEFAULT NULL,
  `head_img_url` varchar(256) DEFAULT NULL,
  `type` varchar(20) DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `company_info_sid` bigint(15) DEFAULT '-1',
  `created_by` varchar(64) NOT NULL,
  `created_time` datetime NOT NULL,
  `updated_by` varchar(64) DEFAULT NULL,
  `updated_time` datetime DEFAULT NULL,
  `version_num` int(9) NOT NULL,
  PRIMARY KEY (`sid`),
  UNIQUE KEY `email_UNIQUE` (`email`),
  UNIQUE KEY `mobile_UNIQUE` (`mobile`),
  UNIQUE KEY `login_name_UNIQUE` (`login_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `ana_account_r2_role` (
  `sid` bigint(15) NOT NULL AUTO_INCREMENT,
  `account_sid` bigint(15) DEFAULT '-1',
  `role_sid` bigint(15) DEFAULT '-1',
  `created_by` varchar(30) NOT NULL,
  `created_time` datetime NOT NULL,
  `updated_by` varchar(30) DEFAULT NULL,
  `updated_time` datetime DEFAULT NULL,
  `version_num` int(9) NOT NULL,
  PRIMARY KEY (`sid`),
  UNIQUE KEY `UNIQUE` (`account_sid`,`role_sid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `ana_employee` (
  `sid` bigint(15) NOT NULL AUTO_INCREMENT,
  `login_name` varchar(30) NOT NULL,
  `real_name` varchar(60) DEFAULT NULL,
  `password` varchar(256) DEFAULT NULL,
  `email` varchar(128) DEFAULT NULL,
  `mobile` varchar(15) DEFAULT NULL,
  `tel` varchar(15) DEFAULT NULL,
  `gender` varchar(20) DEFAULT NULL,
  `id_card` varchar(18) DEFAULT NULL,
  `title` varchar(90) DEFAULT NULL,
  `head_img_url` varchar(256) DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `created_by` varchar(30) NOT NULL,
  `created_time` datetime NOT NULL,
  `updated_by` varchar(30) DEFAULT NULL,
  `updated_time` datetime DEFAULT NULL,
  `version_num` int(9) NOT NULL,
  PRIMARY KEY (`sid`),
  UNIQUE KEY `login_name_UNIQUE` (`login_name`),
  UNIQUE KEY `email_UNIQUE` (`email`),
  UNIQUE KEY `mobile_UNIQUE` (`mobile`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `ana_employee_r2_role` (
  `sid` bigint(15) NOT NULL AUTO_INCREMENT,
  `employee_sid` bigint(15) DEFAULT NULL,
  `role_sid` bigint(15) DEFAULT NULL,
  `created_by` varchar(30) NOT NULL,
  `created_time` datetime NOT NULL,
  `updated_by` varchar(30) DEFAULT NULL,
  `updated_time` datetime DEFAULT NULL,
  `version_num` int(9) NOT NULL,
  PRIMARY KEY (`sid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `ana_permission` (
  `sid` bigint(15) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  `title` varchar(90) DEFAULT NULL,
  `path` varchar(256) DEFAULT NULL,
  `redirect_path` varchar(256) DEFAULT NULL,
  `component_url` varchar(256) DEFAULT NULL,
  `no_cache_flag` char(1) DEFAULT NULL,
  `hidden_flag` char(1) DEFAULT NULL,
  `always_show_flag` char(1) DEFAULT NULL,
  `icon` varchar(64) DEFAULT NULL,
  `level` int(5) DEFAULT NULL,
  `type` varchar(20) NOT NULL,
  `status` varchar(20) NOT NULL,
  `parent_sid` bigint(15) DEFAULT NULL,
  `created_by` varchar(30) NOT NULL,
  `created_time` datetime NOT NULL,
  `updated_by` varchar(30) DEFAULT NULL,
  `updated_time` datetime DEFAULT NULL,
  `version_num` int(9) NOT NULL,
  PRIMARY KEY (`sid`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `ana_role` (
  `sid` bigint(15) NOT NULL AUTO_INCREMENT,
  `code` varchar(30) NOT NULL,
  `name` varchar(60) DEFAULT NULL,
  `category` varchar(20) DEFAULT NULL,
  `type` varchar(20) DEFAULT NULL,
  `company_info_sid` bigint(15) DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `created_by` varchar(30) NOT NULL,
  `created_time` datetime NOT NULL,
  `updated_by` varchar(30) DEFAULT NULL,
  `updated_time` datetime DEFAULT NULL,
  `version_num` int(9) NOT NULL,
  PRIMARY KEY (`sid`),
  UNIQUE KEY `code_UNIQUE` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `ana_role_r2_permission` (
  `sid` bigint(15) NOT NULL AUTO_INCREMENT,
  `role_sid` bigint(15) DEFAULT NULL,
  `permission_sid` bigint(15) DEFAULT NULL,
  `created_by` varchar(30) NOT NULL,
  `created_time` datetime NOT NULL,
  `updated_by` varchar(30) DEFAULT NULL,
  `updated_time` datetime DEFAULT NULL,
  `version_num` int(9) NOT NULL,
  PRIMARY KEY (`sid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `hhplms`.`ana_company_info` (
  `sid` BIGINT(15) NOT NULL AUTO_INCREMENT,
  `code` VARCHAR(64) NULL,
  `name` VARCHAR(90) NULL,
  `type` VARCHAR(20) NULL,
  `address` VARCHAR(150) NULL,
  `contact_name` VARCHAR(60) NULL,
  `contact_mobile` VARCHAR(11) NULL,
  `status` VARCHAR(20) NULL,
  `created_by` VARCHAR(30) NOT NULL,
  `created_time` DATETIME NOT NULL,
  `updated_by` VARCHAR(30) NULL DEFAULT NULL,
  `updated_time` DATETIME NULL DEFAULT NULL,
  `version_num` INT(9) NOT NULL,
  UNIQUE INDEX `code_UNIQUE` (`code` ASC),
  PRIMARY KEY (`sid`));

CREATE TABLE `hhplms`.`sys_feedback` (
  `sid` BIGINT(15) NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(160) NOT NULL,
  `content` VARCHAR(600) NULL,
  `contact` VARCHAR(60) NOT NULL,
  `contact_mobile` VARCHAR(11) NOT NULL,
  `created_by` VARCHAR(30) NOT NULL,
  `created_time` DATETIME NOT NULL,
  `updated_by` VARCHAR(30) NULL DEFAULT NULL,
  `updated_time` DATETIME NULL DEFAULT NULL,
  `version_num` INT(9) NOT NULL,
  PRIMARY KEY (`sid`));

CREATE TABLE `hhplms`.`pm_preferential_msg` (
  `sid` bigint(15) NOT NULL AUTO_INCREMENT,
  `title` varchar(90) NOT NULL,
  `content` varchar(1000) NOT NULL,
  `status` varchar(20) NOT NULL,
  `img_url` varchar(256) DEFAULT NULL,
  `company_info_sid` bigint(15) DEFAULT NULL,
  `created_by` varchar(30) NOT NULL,
  `created_time` datetime NOT NULL,
  `updated_by` varchar(30) DEFAULT NULL,
  `updated_time` datetime DEFAULT NULL,
  `version_num` int(9) NOT NULL,
  PRIMARY KEY (`sid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;