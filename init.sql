create table knowlede_dictionary_custom
(
  uuid int auto_increment
    primary key,
  word varchar(40) null
)
  charset = gb2312;

create table knowledge_category
(
  id            varchar(64)  not null comment '主键'
    primary key,
  create_by     varchar(64)  null comment '创建者',
  create_date   datetime     null comment '创建时间',
  update_by     varchar(64)  null comment '更新者',
  update_date   datetime     null comment '更新时间',
  remarks       varchar(255) null comment '备注信息',
  del_flag      varchar(64)  null comment '逻辑删除标记（0：显示；1：隐藏）',
  category_name varchar(64)  null comment '类别名'
)
  comment '知识库类别表';

create table knowledge_dictionary_abbreviation
(
  id          varchar(64)  not null comment '主键'
    primary key,
  create_by   varchar(64)  null comment '创建者',
  create_date datetime     null comment '创建时间',
  update_by   varchar(64)  null comment '更新者',
  update_date datetime     null comment '更新时间',
  remarks     varchar(255) null comment '备注信息',
  del_flag    varchar(64)  null comment '逻辑删除标记（0：显示；1：隐藏）',
  abbr_name   varchar(64)  null comment '简称',
  full_name   varchar(64)  null comment '全称'
)
  comment '全简称管理';

create table knowledge_dictionary_industry
(
  id            varchar(32)                        not null comment '主键'
    primary key,
  industry_name varchar(50)                        not null comment '行业名称',
  create_time   datetime default CURRENT_TIMESTAMP not null comment '创建时间',
  is_valid      char     default 'Y'               not null comment '有效标志:Y有效,N无效'
)
  comment '行业字典表' charset = gb2312;

create table knowledge_dictionary_stopwords
(
  id   int(10) auto_increment
    primary key,
  word varchar(255) null
)
  collate = utf8_bin;

create table knowledge_dictionary_synonym
(
  id          varchar(64) charset gbk not null comment '主键'
    primary key,
  create_by   varchar(64)             null comment '创建者',
  create_date datetime                null comment '创建时间',
  update_by   varchar(64)             null comment '更新者',
  update_date datetime                null comment '更新时间',
  remarks     varchar(255)            null comment '备注信息',
  del_flag    varchar(64)             not null comment '逻辑删除标记（0：显示；1：隐藏）',
  synonym     varchar(1000)           not null comment '同义词',
  type        varchar(1)              not null comment '类型近似和相等'
)
  comment '同义词词典';

create table knowledge_qa_answer
(
  id           varchar(64)  not null comment '主键'
    primary key,
  create_by    varchar(64)  null comment '创建者',
  create_date  datetime     null comment '创建时间',
  update_by    varchar(64)  null comment '更新者',
  update_date  datetime     null comment '更新时间',
  remarks      varchar(255) null comment '备注信息',
  del_flag     varchar(64)  null comment '逻辑删除标记（0：显示；1：隐藏）',
  answer       longtext     null comment '答案',
  reference_id varchar(64)  null comment '媒体类型引用',
  media_type   varchar(4)   null comment '媒体类型',
  category_id  varchar(64)  null comment '类别id'
)
  comment '问答答案表';

create table knowledge_qa_logs
(
  id          varchar(64)                           not null comment '主键'
    primary key,
  create_by   varchar(64)                           null comment '创建者',
  create_date datetime    default CURRENT_TIMESTAMP null comment '创建时间',
  update_by   varchar(64)                           null comment '更新者',
  update_date datetime                              null comment '更新时间',
  remarks     varchar(255)                          null comment '备注信息',
  del_flag    varchar(64) default '0'               null comment '逻辑删除标记（0：显示；1：隐藏）',
  question    varchar(128)                          null comment '问题',
  score       varchar(64)                           null comment '评分',
  channel_id  varchar(64)                           null comment '授权ID，接入渠道（微信，机器人等）',
  question_id varchar(64)                           null comment '问题ID'
)
  comment '日志表';

create table knowledge_qa_media
(
  MEDIA_ID      int auto_increment comment '素材ID'
    primary key,
  MEDIA_NAME    varchar(100)  not null comment '素材名称',
  MEDIA_SUMMARY varchar(2048) null comment '素材摘要',
  MEDIA_TYPE    varchar(3)    null comment '素材类型(GT:图文 IMG:图片 AU:语音 VI:视频)',
  MEDIA_URL     varchar(512)  null comment '素材链接(若本字段非空，素材文件中存储为素材的封面图片)',
  CREATE_TIME   datetime      not null comment '创建时间',
  UPDATE_TIME   datetime      null comment '更新时间'
)
  comment '素材表';

create table knowledge_qa_question
(
  id          varchar(64)             not null comment '主键'
    primary key,
  create_by   varchar(64)             null comment '创建者',
  create_date datetime                null comment '创建时间',
  update_by   varchar(64)             null comment '更新者',
  update_date datetime                null comment '更新时间',
  remarks     varchar(255)            null comment '备注信息',
  del_flag    varchar(64) default '0' null comment '逻辑删除标记（0：显示；1：隐藏）',
  question    varchar(64)             null comment '问题',
  answer_id   varchar(64)             null comment '答案id'
)
  comment '知识库问答答案表';

