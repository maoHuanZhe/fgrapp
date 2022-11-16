create table if not exists func_class
(
    id bigint auto_increment comment '主键'
        primary key,
    name varchar(128) null comment '问题',
    lastUpdateTime timestamp null comment '修改时间'
)
    comment '分类表';

create table if not exists func_comment
(
    id bigint auto_increment comment '主键'
        primary key,
    contextId varchar(32) not null comment '内容编号',
    userId bigint not null comment '评论用户编号',
    upUserId bigint null comment '上级评论用户编号',
    content text not null comment '内容',
    lastUpdateTime timestamp null comment '修改时间'
)
    comment '评论表';

create table if not exists func_topic
(
    id varchar(32) not null comment '主键'
        primary key,
    problem text null comment '问题',
    summary varchar(128) null,
    answer text null comment '答案',
    classId bigint not null,
    lastUpdateTime timestamp null comment '修改时间'
)
    comment '问题表';

create table if not exists sys_user
(
    id bigint auto_increment comment '主键'
        primary key,
    nickName varchar(128) null comment '用户昵称',
    email varchar(32) null comment '用户邮箱',
    phone varchar(16) null comment '手机号码',
    openId varchar(128) null comment '微信编号',
    sex varchar(8) null comment '用户性别',
    avatar varchar(128) null comment '用户头像',
    isAdmin bigint default 0 null,
    lastUpdateTime timestamp null comment '修改时间'
)
    comment '用户表';