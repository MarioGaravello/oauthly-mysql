# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table allow (
  id                            bigint auto_increment not null,
  user_id                       bigint,
  client_id                     bigint,
  constraint pk_allow primary key (id)
);

create table client (
  id                            bigint auto_increment not null,
  client                        varchar(255),
  secret                        varchar(255),
  name                          varchar(255),
  logo_url                      varchar(255),
  trusted                       tinyint(1) default 0 not null,
  redirect_uri                  varchar(255),
  owner_id                      bigint,
  allowed_origin                varchar(255),
  constraint pk_client primary key (id)
);

create table event (
  id                            bigint auto_increment not null,
  timestamp                     bigint not null,
  event_type                    integer,
  user_id                       bigint,
  ip_address                    varchar(255),
  user_agent                    varchar(255),
  constraint ck_event_event_type check ( event_type in (0,1,2,3,4,5,6,7,8,9,10)),
  constraint pk_event primary key (id)
);

create table provider_link (
  id                            bigint auto_increment not null,
  user_id                       bigint,
  provider_key                  varchar(255),
  remote_user_id                bigint,
  remote_user_email             varchar(255),
  remote_user_name              varchar(255),
  constraint pk_provider_link primary key (id)
);

create table scope (
  id                            bigint auto_increment not null,
  client_id                     bigint,
  scope                         varchar(255),
  description                   varchar(255),
  constraint pk_scope primary key (id)
);

create table user (
  id                            bigint auto_increment not null,
  username                      varchar(255),
  username_normalized           varchar(255),
  email                         varchar(255),
  password                      varchar(255),
  admin                         tinyint(1) default 0 not null,
  last_update_time              bigint not null,
  creation_time                 bigint not null,
  email_verified                tinyint(1) default 0 not null,
  disabled_reason               varchar(255),
  constraint pk_user primary key (id)
);

create index ix_allow_user_id on allow (user_id);
alter table allow add constraint fk_allow_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;

create index ix_allow_client_id on allow (client_id);
alter table allow add constraint fk_allow_client_id foreign key (client_id) references client (id) on delete restrict on update restrict;

create index ix_client_owner_id on client (owner_id);
alter table client add constraint fk_client_owner_id foreign key (owner_id) references user (id) on delete restrict on update restrict;

create index ix_scope_client_id on scope (client_id);
alter table scope add constraint fk_scope_client_id foreign key (client_id) references client (id) on delete restrict on update restrict;


# --- !Downs

alter table allow drop foreign key fk_allow_user_id;
drop index ix_allow_user_id on allow;

alter table allow drop foreign key fk_allow_client_id;
drop index ix_allow_client_id on allow;

alter table client drop foreign key fk_client_owner_id;
drop index ix_client_owner_id on client;

alter table scope drop foreign key fk_scope_client_id;
drop index ix_scope_client_id on scope;

drop table if exists allow;

drop table if exists client;

drop table if exists event;

drop table if exists provider_link;

drop table if exists scope;

drop table if exists user;

