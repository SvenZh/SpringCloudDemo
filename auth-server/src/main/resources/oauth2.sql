CREATE TABLE `oauth_client_details` (
  `client_id` varchar(255) NOT NULL COMMENT '客户端标识',
  `resource_ids` varchar(255) DEFAULT NULL COMMENT '接入资源列表',
  `client_secret` varchar(255) DEFAULT NULL COMMENT '客户端秘钥',
  `scope` varchar(255) DEFAULT NULL COMMENT '授权范围',
  `authorized_grant_types` varchar(255) DEFAULT NULL COMMENT '允许授权类型',
  `web_server_redirect_uri` varchar(255) DEFAULT NULL COMMENT '客户端的重定向URI',
  `authorities` varchar(255) DEFAULT NULL COMMENT '客户端所拥有的权限值',
  `access_token_validity` int(11) DEFAULT NULL COMMENT '设定客户端的access_token的有效时间值(单位:秒)',
  `refresh_token_validity` int(11) DEFAULT NULL COMMENT '设定客户端的refresh_token的有效时间值(单位:秒',
  `additional_information` text COMMENT '这是一个预留的字段,在Oauth的流程中没有实际的使用,可选,但若设置值,必须是JSON格式的数据,',
  `create_time` timestamp NULL DEFAULT NULL,
  `archived` tinyint(1) DEFAULT NULL COMMENT '用于标识客户端是否已存档(即实现逻辑删除),默认值为’0’(即未存档)',
  `trusted` tinyint(1) DEFAULT NULL COMMENT '设置客户端是否为受信任的,默认为’0’(即不受信任的,1为受信任的)',
  `autoapprove` varchar(255) DEFAULT NULL COMMENT '设置用户是否自动Approval操作, 默认值为 ‘false’, 可选值包括 ‘true’,‘false’, ‘read’,‘write’. ',
  PRIMARY KEY (`client_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `oauth_code` (
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '数据的创建时间',
  `code` varchar(255) DEFAULT NULL COMMENT '存储服务端系统生成的code的值(未加密)',
  `authentication` blob COMMENT '存储将AuthorizationRequestHolder.java对象序列化后的二进制数据.',
  KEY `code_index` (`code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `oauth_access_token`  (
  `token_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `token` blob NULL,
  `authentication_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `client_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `authentication` blob NULL,
  `refresh_token` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `oauth_refresh_token`  (
  `token_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `token` blob NULL,
  `authentication` blob NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

