-- ============================================================
-- 二手车平台数据库初始化脚本
-- 数据库: usedcar
-- 字符集: utf8mb4
-- ============================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- -----------------------------------------------------------
-- 1. 系统用户与权限相关表
-- -----------------------------------------------------------

-- 系统用户表 (管理后台用户)
CREATE TABLE IF NOT EXISTS `sys_user` (
    `id`          BIGINT       PRIMARY KEY AUTO_INCREMENT,
    `username`    VARCHAR(50)  NOT NULL UNIQUE COMMENT '用户名',
    `password`    VARCHAR(200) NOT NULL COMMENT '密码(BCrypt加密)',
    `real_name`   VARCHAR(50)  DEFAULT NULL COMMENT '真实姓名',
    `phone`       VARCHAR(20)  DEFAULT NULL COMMENT '手机号',
    `email`       VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `avatar`      VARCHAR(500) DEFAULT NULL COMMENT '头像URL',
    `status`      TINYINT      NOT NULL DEFAULT 1 COMMENT '状态:0禁用 1启用',
    `last_login_at` DATETIME   DEFAULT NULL COMMENT '最后登录时间',
    `created_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted`     TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除:0未删除 1已删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统用户表';

-- 角色表
CREATE TABLE IF NOT EXISTS `sys_role` (
    `id`          BIGINT       PRIMARY KEY AUTO_INCREMENT,
    `role_code`   VARCHAR(50)  NOT NULL UNIQUE COMMENT '角色编码',
    `role_name`   VARCHAR(50)  NOT NULL COMMENT '角色名称',
    `description` VARCHAR(200) DEFAULT NULL COMMENT '角色描述',
    `status`      TINYINT      NOT NULL DEFAULT 1 COMMENT '状态:0禁用 1启用',
    `created_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted`     TINYINT      NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

-- 用户角色关联表
CREATE TABLE IF NOT EXISTS `sys_user_role` (
    `id`          BIGINT  PRIMARY KEY AUTO_INCREMENT,
    `user_id`     BIGINT  NOT NULL COMMENT '用户ID',
    `role_id`     BIGINT  NOT NULL COMMENT '角色ID',
    UNIQUE KEY `uk_user_role` (`user_id`, `role_id`),
    INDEX `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联表';

-- 权限表
CREATE TABLE IF NOT EXISTS `sys_permission` (
    `id`          BIGINT       PRIMARY KEY AUTO_INCREMENT,
    `parent_id`   BIGINT       DEFAULT 0 COMMENT '父权限ID',
    `perm_code`   VARCHAR(100) NOT NULL UNIQUE COMMENT '权限编码',
    `perm_name`   VARCHAR(100) NOT NULL COMMENT '权限名称',
    `perm_type`   TINYINT      NOT NULL DEFAULT 1 COMMENT '类型:1菜单 2按钮 3接口',
    `path`        VARCHAR(200) DEFAULT NULL COMMENT '路由路径',
    `icon`        VARCHAR(100) DEFAULT NULL COMMENT '图标',
    `sort_order`  INT          DEFAULT 0 COMMENT '排序',
    `status`      TINYINT      NOT NULL DEFAULT 1 COMMENT '状态:0禁用 1启用',
    `created_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='权限表';

-- 角色权限关联表
CREATE TABLE IF NOT EXISTS `sys_role_permission` (
    `id`            BIGINT PRIMARY KEY AUTO_INCREMENT,
    `role_id`       BIGINT NOT NULL COMMENT '角色ID',
    `permission_id` BIGINT NOT NULL COMMENT '权限ID',
    UNIQUE KEY `uk_role_perm` (`role_id`, `permission_id`),
    INDEX `idx_permission_id` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色权限关联表';

-- -----------------------------------------------------------
-- 2. 微信小程序用户表
-- -----------------------------------------------------------

CREATE TABLE IF NOT EXISTS `wx_user` (
    `id`            BIGINT       PRIMARY KEY AUTO_INCREMENT,
    `openid`        VARCHAR(64)  NOT NULL UNIQUE COMMENT '微信openid',
    `unionid`       VARCHAR(64)  DEFAULT NULL COMMENT '微信unionid',
    `phone`         VARCHAR(20)  NOT NULL COMMENT '手机号',
    `nickname`      VARCHAR(100) DEFAULT NULL COMMENT '昵称',
    `avatar_url`    VARCHAR(500) DEFAULT NULL COMMENT '头像URL',
    `gender`        TINYINT      DEFAULT NULL COMMENT '性别:0未知 1男 2女',
    `city`          VARCHAR(50)  DEFAULT NULL COMMENT '城市',
    `province`      VARCHAR(50)  DEFAULT NULL COMMENT '省份',
    `last_login_at` DATETIME     DEFAULT NULL COMMENT '最后登录时间',
    `status`        TINYINT      NOT NULL DEFAULT 1 COMMENT '状态:0禁用 1启用',
    `created_at`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted`       TINYINT      NOT NULL DEFAULT 0,
    INDEX `idx_phone` (`phone`),
    INDEX `idx_unionid` (`unionid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='微信小程序用户表';

-- -----------------------------------------------------------
-- 3. 品牌与车型字典表
-- -----------------------------------------------------------

CREATE TABLE IF NOT EXISTS `brand` (
    `id`          BIGINT       PRIMARY KEY AUTO_INCREMENT,
    `name`        VARCHAR(50)  NOT NULL COMMENT '品牌名称',
    `name_en`     VARCHAR(50)  DEFAULT NULL COMMENT '品牌英文名',
    `logo_url`    VARCHAR(500) DEFAULT NULL COMMENT '品牌Logo',
    `first_letter` CHAR(1)     DEFAULT NULL COMMENT '首字母',
    `sort_order`  INT          DEFAULT 0 COMMENT '排序',
    `status`      TINYINT      NOT NULL DEFAULT 1 COMMENT '状态:0禁用 1启用',
    `created_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX `idx_first_letter` (`first_letter`),
    INDEX `idx_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='品牌表';

CREATE TABLE IF NOT EXISTS `car_model` (
    `id`          BIGINT       PRIMARY KEY AUTO_INCREMENT,
    `brand_id`    BIGINT       NOT NULL COMMENT '品牌ID',
    `name`        VARCHAR(100) NOT NULL COMMENT '车型名称',
    `category`    VARCHAR(20)  DEFAULT NULL COMMENT '车辆类型:轿车/SUV/MPV/跑车/皮卡',
    `sort_order`  INT          DEFAULT 0 COMMENT '排序',
    `status`      TINYINT      NOT NULL DEFAULT 1 COMMENT '状态:0禁用 1启用',
    `created_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX `idx_brand_id` (`brand_id`),
    INDEX `idx_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='车型表';

-- -----------------------------------------------------------
-- 4. 车辆信息相关表
-- -----------------------------------------------------------

CREATE TABLE IF NOT EXISTS `vehicle` (
    `id`                BIGINT        PRIMARY KEY AUTO_INCREMENT,
    `vin`               VARCHAR(17)   DEFAULT NULL UNIQUE COMMENT 'VIN码',
    `brand_id`          BIGINT        NOT NULL COMMENT '品牌ID',
    `model_id`          BIGINT        NOT NULL COMMENT '车型ID',
    `year`              SMALLINT      NOT NULL COMMENT '年款',
    `price`             DECIMAL(12,2) NOT NULL COMMENT '售价(元)',
    `original_price`    DECIMAL(12,2) DEFAULT NULL COMMENT '新车指导价(元)',
    `mileage`           INT           NOT NULL COMMENT '里程(km)',
    `displacement`      VARCHAR(10)   DEFAULT NULL COMMENT '排量',
    `transmission`      TINYINT       DEFAULT NULL COMMENT '变速箱:1手动 2自动 3双离合',
    `fuel_type`         TINYINT       DEFAULT NULL COMMENT '燃料类型:1汽油 2柴油 3油电混合 4纯电动',
    `color`             VARCHAR(20)   DEFAULT NULL COMMENT '车身颜色',
    `interior_color`    VARCHAR(20)   DEFAULT NULL COMMENT '内饰颜色',
    `seats`             TINYINT       DEFAULT NULL COMMENT '座位数',
    `emission_standard` VARCHAR(20)   DEFAULT NULL COMMENT '排放标准',
    `registration_date` DATE          DEFAULT NULL COMMENT '上牌日期',
    `registration_city` VARCHAR(50)   DEFAULT NULL COMMENT '上牌城市',
    `inspection_date`   DATE          DEFAULT NULL COMMENT '年检到期日',
    `insurance_date`    DATE          DEFAULT NULL COMMENT '保险到期日',
    `description`       TEXT          DEFAULT NULL COMMENT '车主描述',
    `cover_image_url`   VARCHAR(500)  DEFAULT NULL COMMENT '封面图URL',
    `status`            TINYINT       NOT NULL DEFAULT 1 COMMENT '1草稿 2在售 3已下架 4已售',
    `seller_id`         BIGINT        DEFAULT NULL COMMENT '负责销售人员ID',
    `view_count`        INT           NOT NULL DEFAULT 0 COMMENT '浏览次数',
    `favorite_count`    INT           NOT NULL DEFAULT 0 COMMENT '收藏次数',
    `created_at`        DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`        DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted`           TINYINT       NOT NULL DEFAULT 0,
    INDEX `idx_brand_model` (`brand_id`, `model_id`),
    INDEX `idx_price` (`price`),
    INDEX `idx_status` (`status`),
    INDEX `idx_seller` (`seller_id`),
    INDEX `idx_created` (`created_at`),
    INDEX `idx_year` (`year`),
    INDEX `idx_mileage` (`mileage`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='车辆信息表';

-- 车辆图片表
CREATE TABLE IF NOT EXISTS `vehicle_image` (
    `id`          BIGINT       PRIMARY KEY AUTO_INCREMENT,
    `vehicle_id`  BIGINT       NOT NULL COMMENT '车辆ID',
    `image_url`   VARCHAR(500) NOT NULL COMMENT '原图URL',
    `thumb_url`   VARCHAR(500) DEFAULT NULL COMMENT '缩略图URL',
    `image_type`  TINYINT      DEFAULT 1 COMMENT '图片类型:1外观 2内饰 3细节 4检测报告',
    `sort_order`  INT          DEFAULT 0 COMMENT '排序',
    `created_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_vehicle_id` (`vehicle_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='车辆图片表';

-- -----------------------------------------------------------
-- 5. 客户管理相关表
-- -----------------------------------------------------------

CREATE TABLE IF NOT EXISTS `customer` (
    `id`               BIGINT        PRIMARY KEY AUTO_INCREMENT,
    `wx_user_id`       BIGINT        DEFAULT NULL COMMENT '关联小程序用户ID',
    `name`             VARCHAR(50)   DEFAULT NULL COMMENT '姓名',
    `phone`            VARCHAR(20)   DEFAULT NULL COMMENT '手机号',
    `wechat_id`        VARCHAR(50)   DEFAULT NULL COMMENT '微信号',
    `gender`           TINYINT       DEFAULT NULL COMMENT '性别:0未知 1男 2女',
    `budget_min`       DECIMAL(12,2) DEFAULT NULL COMMENT '预算下限',
    `budget_max`       DECIMAL(12,2) DEFAULT NULL COMMENT '预算上限',
    `preferred_brands` VARCHAR(500)  DEFAULT NULL COMMENT '偏好品牌(JSON数组)',
    `preferred_types`  VARCHAR(200)  DEFAULT NULL COMMENT '偏好车型类别(JSON数组)',
    `intent_level`     TINYINT       DEFAULT 0 COMMENT '购买意向度:1-5',
    `stage`            TINYINT       NOT NULL DEFAULT 1 COMMENT '1首次接触 2需求确认 3方案提供 4谈判 5成交 6流失',
    `source`           VARCHAR(50)   DEFAULT NULL COMMENT '客户来源:小程序/线下/转介绍',
    `assigned_to`      BIGINT        DEFAULT NULL COMMENT '负责销售人员ID',
    `next_followup_at` DATETIME      DEFAULT NULL COMMENT '下次跟进时间',
    `remark`           VARCHAR(500)  DEFAULT NULL COMMENT '备注',
    `created_at`       DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`       DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted`          TINYINT       NOT NULL DEFAULT 0,
    INDEX `idx_wx_user` (`wx_user_id`),
    INDEX `idx_assigned` (`assigned_to`),
    INDEX `idx_stage` (`stage`),
    INDEX `idx_next_followup` (`next_followup_at`),
    INDEX `idx_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='客户表';

-- 客户行为记录表
CREATE TABLE IF NOT EXISTS `customer_behavior` (
    `id`          BIGINT       PRIMARY KEY AUTO_INCREMENT,
    `wx_user_id`  BIGINT       NOT NULL COMMENT '小程序用户ID',
    `event_type`  VARCHAR(30)  NOT NULL COMMENT '事件类型:page_view/search/favorite/share/consult',
    `target_type` VARCHAR(20)  DEFAULT NULL COMMENT '目标类型:vehicle/brand/page',
    `target_id`   VARCHAR(50)  DEFAULT NULL COMMENT '目标ID',
    `extra_data`  JSON         DEFAULT NULL COMMENT '扩展数据(搜索词/停留时长等)',
    `created_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_wx_user_event` (`wx_user_id`, `event_type`),
    INDEX `idx_created` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='客户行为记录表';

-- 客户跟进记录表
CREATE TABLE IF NOT EXISTS `customer_followup` (
    `id`               BIGINT       PRIMARY KEY AUTO_INCREMENT,
    `customer_id`      BIGINT       NOT NULL COMMENT '客户ID',
    `operator_id`      BIGINT       NOT NULL COMMENT '操作人(销售人员ID)',
    `content`          TEXT         NOT NULL COMMENT '跟进内容',
    `followup_type`    TINYINT      DEFAULT NULL COMMENT '跟进方式:1电话 2微信 3到店 4其他',
    `result`           VARCHAR(200) DEFAULT NULL COMMENT '跟进结果',
    `next_followup_at` DATETIME     DEFAULT NULL COMMENT '下次跟进时间',
    `created_at`       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_customer` (`customer_id`),
    INDEX `idx_operator` (`operator_id`),
    INDEX `idx_created` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='客户跟进记录表';

-- -----------------------------------------------------------
-- 6. 订单与交易表
-- -----------------------------------------------------------

CREATE TABLE IF NOT EXISTS `order_info` (
    `id`              BIGINT        PRIMARY KEY AUTO_INCREMENT,
    `order_no`        VARCHAR(32)   NOT NULL UNIQUE COMMENT '订单编号',
    `vehicle_id`      BIGINT        NOT NULL COMMENT '车辆ID',
    `buyer_id`        BIGINT        NOT NULL COMMENT '买家(小程序用户ID)',
    `seller_id`       BIGINT        DEFAULT NULL COMMENT '销售人员ID',
    `deal_price`      DECIMAL(12,2) NOT NULL COMMENT '成交价格',
    `deposit`         DECIMAL(12,2) DEFAULT NULL COMMENT '定金',
    `status`          TINYINT       NOT NULL DEFAULT 1 COMMENT '1待确认 2待付款 3已付款 4已交付 5已完成 6已取消',
    `payment_method`  TINYINT       DEFAULT NULL COMMENT '支付方式:1微信支付 2线下支付 3银行转账',
    `remark`          VARCHAR(500)  DEFAULT NULL COMMENT '备注',
    `deal_at`         DATETIME      DEFAULT NULL COMMENT '成交时间',
    `created_at`      DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`      DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted`         TINYINT       NOT NULL DEFAULT 0,
    INDEX `idx_order_no` (`order_no`),
    INDEX `idx_buyer` (`buyer_id`),
    INDEX `idx_vehicle` (`vehicle_id`),
    INDEX `idx_seller` (`seller_id`),
    INDEX `idx_status` (`status`),
    INDEX `idx_created` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单表';

-- 订单状态变更日志
CREATE TABLE IF NOT EXISTS `order_status_log` (
    `id`          BIGINT       PRIMARY KEY AUTO_INCREMENT,
    `order_id`    BIGINT       NOT NULL COMMENT '订单ID',
    `from_status` TINYINT      DEFAULT NULL COMMENT '原状态',
    `to_status`   TINYINT      NOT NULL COMMENT '新状态',
    `operator_id` BIGINT       DEFAULT NULL COMMENT '操作人ID',
    `remark`      VARCHAR(200) DEFAULT NULL COMMENT '备注',
    `created_at`  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_order` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单状态变更日志';

-- -----------------------------------------------------------
-- 7. 用户行为相关表
-- -----------------------------------------------------------

-- 收藏表
CREATE TABLE IF NOT EXISTS `favorite` (
    `id`          BIGINT   PRIMARY KEY AUTO_INCREMENT,
    `wx_user_id`  BIGINT   NOT NULL COMMENT '小程序用户ID',
    `vehicle_id`  BIGINT   NOT NULL COMMENT '车辆ID',
    `created_at`  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY `uk_user_vehicle` (`wx_user_id`, `vehicle_id`),
    INDEX `idx_vehicle` (`vehicle_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='收藏表';

-- 浏览历史表
CREATE TABLE IF NOT EXISTS `browse_history` (
    `id`          BIGINT   PRIMARY KEY AUTO_INCREMENT,
    `wx_user_id`  BIGINT   NOT NULL COMMENT '小程序用户ID',
    `vehicle_id`  BIGINT   NOT NULL COMMENT '车辆ID',
    `duration`    INT      DEFAULT 0 COMMENT '停留时长(秒)',
    `created_at`  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_user_time` (`wx_user_id`, `created_at` DESC),
    INDEX `idx_vehicle` (`vehicle_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='浏览历史表';

-- -----------------------------------------------------------
-- 8. 消息通知表
-- -----------------------------------------------------------

CREATE TABLE IF NOT EXISTS `notification` (
    `id`           BIGINT       PRIMARY KEY AUTO_INCREMENT,
    `wx_user_id`   BIGINT       NOT NULL COMMENT '接收用户ID',
    `title`        VARCHAR(100) NOT NULL COMMENT '通知标题',
    `content`      VARCHAR(500) NOT NULL COMMENT '通知内容',
    `notify_type`  TINYINT      NOT NULL COMMENT '类型:1价格变动 2新车推荐 3系统通知 4订单通知',
    `target_type`  VARCHAR(20)  DEFAULT NULL COMMENT '关联目标类型:vehicle/order',
    `target_id`    BIGINT       DEFAULT NULL COMMENT '关联目标ID',
    `is_read`      TINYINT      NOT NULL DEFAULT 0 COMMENT '是否已读:0未读 1已读',
    `read_at`      DATETIME     DEFAULT NULL COMMENT '阅读时间',
    `created_at`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_user_read` (`wx_user_id`, `is_read`),
    INDEX `idx_created` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='消息通知表';

-- -----------------------------------------------------------
-- 9. 系统管理表
-- -----------------------------------------------------------

-- 系统配置表
CREATE TABLE IF NOT EXISTS `sys_config` (
    `id`           BIGINT       PRIMARY KEY AUTO_INCREMENT,
    `config_key`   VARCHAR(100) NOT NULL UNIQUE COMMENT '配置键',
    `config_value` TEXT         NOT NULL COMMENT '配置值',
    `config_desc`  VARCHAR(200) DEFAULT NULL COMMENT '配置描述',
    `created_at`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统配置表';

-- 操作日志表
CREATE TABLE IF NOT EXISTS `operation_log` (
    `id`            BIGINT       PRIMARY KEY AUTO_INCREMENT,
    `operator_id`   BIGINT       DEFAULT NULL COMMENT '操作人ID',
    `operator_name` VARCHAR(50)  DEFAULT NULL COMMENT '操作人姓名',
    `module`        VARCHAR(50)  NOT NULL COMMENT '操作模块',
    `action`        VARCHAR(50)  NOT NULL COMMENT '操作类型',
    `target_type`   VARCHAR(50)  DEFAULT NULL COMMENT '操作目标类型',
    `target_id`     VARCHAR(50)  DEFAULT NULL COMMENT '操作目标ID',
    `detail`        TEXT         DEFAULT NULL COMMENT '操作详情',
    `ip`            VARCHAR(50)  DEFAULT NULL COMMENT '操作IP',
    `user_agent`    VARCHAR(500) DEFAULT NULL COMMENT '浏览器UA',
    `status`        TINYINT      NOT NULL DEFAULT 1 COMMENT '操作结果:0失败 1成功',
    `error_msg`     VARCHAR(500) DEFAULT NULL COMMENT '错误信息',
    `created_at`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_operator` (`operator_id`),
    INDEX `idx_module` (`module`),
    INDEX `idx_created` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表';

-- -----------------------------------------------------------
-- 10. 初始数据
-- -----------------------------------------------------------

-- 初始角色
INSERT INTO `sys_role` (`role_code`, `role_name`, `description`) VALUES
    ('SUPER_ADMIN', '超级管理员', '拥有所有权限'),
    ('ADMIN', '普通管理员', '负责车辆审核和数据管理'),
    ('SALES', '销售人员', '管理自己负责的客户和车辆');

-- 初始超级管理员 (密码: admin123, BCrypt加密)
INSERT INTO `sys_user` (`username`, `password`, `real_name`, `phone`, `status`) VALUES
    ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '超级管理员', '13800000000', 1);

-- 管理员绑定超级管理员角色
INSERT INTO `sys_user_role` (`user_id`, `role_id`) VALUES (1, 1);

-- 初始权限数据
INSERT INTO `sys_permission` (`parent_id`, `perm_code`, `perm_name`, `perm_type`, `path`, `icon`, `sort_order`) VALUES
    (0, 'dashboard',        'Dashboard',    1, '/dashboard',        'Odometer',      1),
    (0, 'vehicle',          '车辆管理',      1, '/vehicle',          'Van',           2),
    (0, 'customer',         '客户管理',      1, '/customer',         'User',          3),
    (0, 'order',            '订单管理',      1, '/order',            'Document',      4),
    (0, 'statistics',       '数据统计',      1, '/statistics',       'TrendCharts',   5),
    (0, 'system',           '系统管理',      1, '/system',           'Setting',       6),
    -- 车辆管理子菜单
    (2, 'vehicle:list',     '车辆列表',      1, '/vehicle/list',     NULL, 1),
    (2, 'vehicle:create',   '新增车辆',      2, NULL,                NULL, 2),
    (2, 'vehicle:edit',     '编辑车辆',      2, NULL,                NULL, 3),
    (2, 'vehicle:delete',   '删除车辆',      2, NULL,                NULL, 4),
    (2, 'vehicle:publish',  '上架/下架',     2, NULL,                NULL, 5),
    -- 客户管理子菜单
    (3, 'customer:list',    '客户列表',      1, '/customer/list',    NULL, 1),
    (3, 'customer:detail',  '客户详情',      2, NULL,                NULL, 2),
    (3, 'customer:followup','添加跟进',      2, NULL,                NULL, 3),
    -- 订单管理子菜单
    (4, 'order:list',       '订单列表',      1, '/order/list',       NULL, 1),
    (4, 'order:detail',     '订单详情',      2, NULL,                NULL, 2),
    (4, 'order:export',     '导出订单',      2, NULL,                NULL, 3),
    -- 系统管理子菜单
    (6, 'system:user',      '用户管理',      1, '/system/user',      NULL, 1),
    (6, 'system:role',      '角色管理',      1, '/system/role',      NULL, 2),
    (6, 'system:brand',     '品牌型号管理',   1, '/system/brand',     NULL, 3),
    (6, 'system:config',    '系统配置',      1, '/system/config',    NULL, 4),
    (6, 'system:log',       '操作日志',      1, '/system/log',       NULL, 5);

-- 超级管理员拥有所有权限
INSERT INTO `sys_role_permission` (`role_id`, `permission_id`)
SELECT 1, `id` FROM `sys_permission`;

-- 普通管理员权限 (除系统管理外)
INSERT INTO `sys_role_permission` (`role_id`, `permission_id`)
SELECT 2, `id` FROM `sys_permission` WHERE `perm_code` NOT LIKE 'system:%' AND `perm_code` != 'system';

-- 销售人员权限 (仅Dashboard、车辆查看、客户管理)
INSERT INTO `sys_role_permission` (`role_id`, `permission_id`)
SELECT 3, `id` FROM `sys_permission` WHERE `perm_code` IN ('dashboard', 'vehicle', 'vehicle:list', 'customer', 'customer:list', 'customer:detail', 'customer:followup');

-- 初始品牌数据
INSERT INTO `brand` (`name`, `name_en`, `first_letter`, `sort_order`) VALUES
    ('奥迪',     'Audi',           'A', 1),
    ('宝马',     'BMW',            'B', 2),
    ('奔驰',     'Mercedes-Benz',  'B', 3),
    ('别克',     'Buick',          'B', 4),
    ('本田',     'Honda',          'B', 5),
    ('比亚迪',   'BYD',            'B', 6),
    ('大众',     'Volkswagen',     'D', 7),
    ('丰田',     'Toyota',         'F', 8),
    ('福特',     'Ford',           'F', 9),
    ('吉利',     'Geely',          'J', 10),
    ('凯迪拉克', 'Cadillac',       'K', 11),
    ('雷克萨斯', 'Lexus',          'L', 12),
    ('路虎',     'Land Rover',     'L', 13),
    ('马自达',   'Mazda',          'M', 14),
    ('日产',     'Nissan',         'R', 15),
    ('特斯拉',   'Tesla',          'T', 16),
    ('沃尔沃',   'Volvo',          'W', 17),
    ('现代',     'Hyundai',        'X', 18),
    ('雪佛兰',   'Chevrolet',      'X', 19),
    ('长安',     'Changan',        'Z', 20);

-- 初始车型数据 (部分热门车型)
INSERT INTO `car_model` (`brand_id`, `name`, `category`) VALUES
    -- 奥迪
    (1, 'A3',  '轿车'), (1, 'A4L', '轿车'), (1, 'A6L', '轿车'), (1, 'Q3', 'SUV'), (1, 'Q5L', 'SUV'),
    -- 宝马
    (2, '3系', '轿车'), (2, '5系', '轿车'), (2, 'X1', 'SUV'), (2, 'X3', 'SUV'), (2, 'X5', 'SUV'),
    -- 奔驰
    (3, 'C级', '轿车'), (3, 'E级', '轿车'), (3, 'GLA', 'SUV'), (3, 'GLC', 'SUV'), (3, 'GLE', 'SUV'),
    -- 本田
    (5, '思域', '轿车'), (5, '雅阁', '轿车'), (5, 'CR-V', 'SUV'), (5, '缤智', 'SUV'),
    -- 比亚迪
    (6, '秦PLUS', '轿车'), (6, '汉', '轿车'), (6, '宋PLUS', 'SUV'), (6, '唐', 'SUV'), (6, '元PLUS', 'SUV'),
    -- 大众
    (7, '朗逸', '轿车'), (7, '帕萨特', '轿车'), (7, '途观L', 'SUV'), (7, '探岳', 'SUV'),
    -- 丰田
    (8, '卡罗拉', '轿车'), (8, '凯美瑞', '轿车'), (8, 'RAV4', 'SUV'), (8, '汉兰达', 'SUV'),
    -- 特斯拉
    (16, 'Model 3', '轿车'), (16, 'Model Y', 'SUV'), (16, 'Model S', '轿车'), (16, 'Model X', 'SUV');

-- 初始系统配置
INSERT INTO `sys_config` (`config_key`, `config_value`, `config_desc`) VALUES
    ('price_ranges',     '[{"label":"1-5万","min":10000,"max":50000},{"label":"5-10万","min":50000,"max":100000},{"label":"10-20万","min":100000,"max":200000},{"label":"20-30万","min":200000,"max":300000},{"label":"30万以上","min":300000,"max":null}]', '价格区间配置'),
    ('mileage_ranges',   '[{"label":"1万公里以下","min":0,"max":10000},{"label":"1-3万公里","min":10000,"max":30000},{"label":"3-5万公里","min":30000,"max":50000},{"label":"5万公里以上","min":50000,"max":null}]', '里程区间配置'),
    ('age_ranges',       '[{"label":"1年内","min":0,"max":1},{"label":"1-3年","min":1,"max":3},{"label":"3-5年","min":3,"max":5},{"label":"5年以上","min":5,"max":null}]', '车龄区间配置'),
    ('page_size_default','20', '默认分页大小'),
    ('upload_max_size',  '10485760', '图片上传最大大小(字节)=10MB');

SET FOREIGN_KEY_CHECKS = 1;
