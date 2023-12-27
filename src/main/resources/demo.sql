create table t_admin
(
    data_id            bigint auto_increment comment '数据id'
        primary key,
    admin_id           char(10)     not null comment '管理员账号',
    admin_password     char(20)     not null comment '管理员密码',
    admin_name         varchar(200) not null comment '管理员名称',
    email_number       char(100)    null comment '邮箱号码',
    password_err_count int unsigned not null comment '当天密码错误次数',
    admin_status       char         not null comment '管理员状态
A：活动
C：已删除
D：停用',
    admin_role         varchar(500) not null comment '管理员角色信息',
    spec_code          varchar(200) not null comment '冗余信息（1-6：邮箱验证码）',
    constraint index_1
        unique (admin_id)
)
    charset = utf8;

create table t_cardholder
(
    ci_tr_no              varchar(50)  not null comment '支付ID',
    ci_mer_no             char(6)      not null comment '商户账户号',
    ci_mer_order_no       varchar(50)  not null comment '商户订单号',
    ci_card_number        char(20)     not null comment '卡号（后四位）',
    ci_billing_firstname  varchar(255) not null comment 'billing firstname',
    ci_billing_lastname   varchar(255) not null comment 'billing lastname',
    ci_billing_address    varchar(255) not null comment 'billing address',
    ci_billing_city       varchar(255) not null comment 'billing city',
    ci_billing_state      varchar(255) not null comment 'billing state',
    ci_billing_zip        varchar(255) not null comment 'billing zip',
    ci_billing_country    varchar(255) not null comment 'billing country',
    ci_billing_phone      varchar(255) not null comment 'billing phone',
    ci_billing_email      varchar(255) not null comment 'billing email',
    ci_shipping_firstname varchar(255) not null comment 'shipping firstname',
    ci_shipping_lastname  varchar(255) not null comment 'shipping lastname',
    ci_shipping_address   varchar(255) not null comment 'shipping address',
    ci_shipping_city      varchar(255) not null comment 'shipping city',
    ci_shipping_state     varchar(255) not null comment 'shipping state',
    ci_shipping_zip       varchar(255) not null comment 'shipping zip',
    ci_shipping_country   varchar(255) not null comment 'shipping country',
    ci_shipping_phone     varchar(255) not null comment 'ci_shipping_phone',
    ci_shipping_email     varchar(255) not null comment 'shipping email',
    ci_customer_ip        varchar(60)  null comment '消费者IP',
    ci_create_datetime    char(17)     not null comment '创建时间',
    ci_update_datetime    char(17)     not null comment '修改时间',
    spec_code             varchar(400) not null comment '冗余信息',
    maintain_api          char(50)     not null comment '维护APINAME',
    maintain_time         char(20)     not null comment '维护时间',
    maintain_version      int          not null comment '维护版本',
    maintain_database     char(10)     not null comment '维护数据库',
    constraint index_1
        unique (ci_tr_no) comment '订单号'
)
    charset = utf8;

alter table t_cardholder
    add primary key (ci_tr_no) comment '订单号';

create table t_channel
(
    channel_id                bigint auto_increment comment '通道ID',
    channel_name              varchar(255)   not null comment '通道名称',
    payment_bank              varchar(255)   not null comment '支付银行',
    payment_method            varchar(255)   not null comment '支付方式',
    channel_day_limit         decimal(10, 2) not null comment '通道每日总交易限额',
    channel_balance_threshold decimal(10, 2) not null comment '提醒额度',
    channel_status            char           not null comment '通道状态',
    channel_direct_url        varchar(255)   null comment '支付跳转Url',
    constraint index_1
        unique (channel_id)
)
    charset = utf8;

alter table t_channel
    add primary key (channel_id);

create table t_channel_pay
(
    id            bigint auto_increment comment '数据id'
        primary key,
    channel_id    bigint         not null,
    pay_name      char(100)      not null,
    pay_email     char(100)      not null,
    pay_bank_name char(20)       not null,
    pay_limit_day decimal(20, 2) not null,
    pay_count_day int            not null,
    pay_image_url char(100)      not null,
    pay_url       varchar(100)   null
);

create table t_email_authorize_t
(
    data_id                  bigint auto_increment comment '数据ID'
        primary key,
    email_number             char(100) not null comment '邮箱号码',
    email_authorize_password char(50)  not null comment '邮箱授权码',
    email_host               char(50)  not null comment '邮箱主机',
    email_protocol           char(50)  not null comment '协议'
)
    charset = utf8;

create table t_merchant
(
    merchant_id        bigint auto_increment,
    company_name       char(50)     not null,
    website_type       varchar(255) not null,
    channel_id         int          not null,
    channel_mid        varchar(255) not null,
    channel_key        varchar(255) not null,
    channel_secure_key varchar(255) not null,
    merchant_sign_key  varchar(255) not null,
    constraint index_1
        unique (merchant_id)
)
    charset = utf8;

create index index_2
    on t_merchant (company_name, website_type);

alter table t_merchant
    add primary key (merchant_id);

create table t_token
(
    data_id           bigint auto_increment comment '数据自增ID'
        primary key,
    admin_id          char(10)     not null comment '用户账号',
    admin_token       varchar(255) not null comment '登录token',
    admin_login_time  char(20)     not null comment '上次登录时间',
    admin_expire_time char(20)     not null comment '登录失效时间',
    constraint index_1
        unique (admin_id, admin_token) comment '唯一索引'
)
    charset = utf8;

create table t_transaction_block
(
    trans_id           int auto_increment comment '主键id'
        primary key,
    trans_no           varchar(21)    not null comment '支付id',
    trans_mer_order_no char(50)       not null comment '商户订单号',
    trans_mer_account  varchar(6)     not null comment '商户账户号',
    trans_amount       decimal(20, 2) not null comment '交易金额',
    trans_currency     varchar(3)     not null comment '交易币种',
    trans_result_code  varchar(10)    not null comment '交易响应代码',
    trans_result_info  varchar(100)   not null comment '交易响应说明',
    trans_cha_id       char(10)       not null comment '通道id',
    trans_datetime     char(17)       not null comment '交易时间',
    trans_bank_no      varchar(50)    not null comment '银行订单号',
    trans_notice_url   varchar(255)   not null comment '交易推送地址'
)
    charset = utf8;

create index index_1
    on t_transaction_block (trans_mer_order_no, trans_mer_account);

create table t_transaction_normal
(
    trans_id           int auto_increment comment '主键id'
        primary key,
    trans_no           varchar(21)    not null comment '支付id',
    trans_mer_order_no char(50)       not null comment '商户订单号',
    trans_mer_account  varchar(6)     not null comment '商户账户号',
    trans_amount       decimal(20, 2) not null comment '交易金额',
    trans_currency     varchar(3)     not null comment '交易币种',
    trans_result_code  varchar(50)    not null comment '交易响应代码',
    trans_result_info  varchar(100)   not null comment '交易响应说明',
    trans_status       char           not null comment '交易状态：A.成功 F.失败 P.待处理',
    trans_cha_id       char(10)       not null comment '通道id',
    trans_datetime     char(17)       not null comment '交易时间',
    trans_bank_no      varchar(50)    not null comment '银行订单号',
    trans_notice_url   varchar(255)   not null comment '交易推送地址',
    trans_notice_status   int            null,
    trans_notice_datetime char(18)       null,
    trans_pay_image_url   varchar(255)   null comment '收款二维码链接',
    trans_pay_id          varchar(50)    null comment '收款支付ID',
    trans_pay_url         varchar(255)   null,
    trans_pay_name        varchar(255)   null comment '收款人名',
    trans_pay_email       varchar(255)   null comment '收款人邮箱',
    constraint index_1
        unique (trans_mer_order_no, trans_mer_account),
    constraint index_2
        unique (trans_no)
)
    charset = utf8;

create table t_unique_check_t
(
    id       bigint auto_increment comment '主键'
        primary key,
    un_value varchar(255) not null comment '唯一值',
    un_type  tinyint      not null comment '类型 1. venmo-paymentID',
    un_date  char(20)     not null comment '添加日期',
    constraint index_1
        unique (un_value, un_type) comment '唯一索引'
);

create index index_2
    on t_unique_check_t (un_value, un_type, un_date)
    comment '查询索引';

