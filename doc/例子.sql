DROP TABLE IF EXISTS `config_system`;
CREATE TABLE `config_system` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL COMMENT '系统名字',
  `nameDesc` varchar(100) NOT NULL COMMENT '系统描述',
  `createDate` datetime NOT NULL COMMENT '创建时间',
  `updateTime` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `config_system_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=0  DEFAULT CHARSET=utf8 COMMENT = "系统,标识接入的业务系统";
 

drop TABLE if EXISTS `config_data`;
create TABLE `config_data`(
  `id` bigint(20) not NULL AUTO_INCREMENT,
  `systemName` varchar(20) NOT null COMMENT '系统名字',
  `dataKey` varchar(20) NOT NULL COMMENT '数据唯一标识',
  `dataDesc` varchar(100) NOT NULL COMMENT '数据描述',
  `data` text NOT NULL COMMENT '序列化数据',
  `createDate` datetime NOT NULL COMMENT '创建时间',
  `updateTime` datetime NOT NULL COMMENT '更新时间',
  PRIMARY key (`id`),
  unique key `key_systemname_data` (`systemName`,`dataKey`)，
   KEY `index_systemname` (`systemName`), 
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT="数据配置，根据系统和标识区分唯一一条数据";


drop TABLE if EXISTS `config_htmlelement`;
create TABLE `config_htmlelement`(
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `systemName` varchar(20) NOT NULL COMMENT '系统名字',
    `elementName` varchar(20) NOT NULL COMMENT 'html组件name',
    `elementDisplay` varchar(100) NOT NULL COMMENT 'html组件展示名字',
    `elementType` varchar(30) NOT NULL COMMENT 'html组件类型',
    `elementValueType` varchar(30) NOT NULL COMMENT 'html值的类型',
    `elementValueCheck` bit NOT NULL COMMENT 'html组件是否必填。0：不必，1：必填',
    `elementInitValue` text NULL COMMENT 'html初始值',
    `status` bit null COMMENT '状态0：无效，1：有效',
  `createDate` datetime NOT NULL COMMENT '创建时间',
  `updateTime` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
   KEY `index_systemname` (`systemName`), 
  unique key `uq_systemname_elementname` (`systemName`,`elementName`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COMMENT="html组件配置，每个系统的序列化数据通过html组件图像化"
