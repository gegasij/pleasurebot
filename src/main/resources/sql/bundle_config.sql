drop table if exists Bundle_Config;
create table if not exists Bundle_Config
(
    id                  serial             not null,
    "owner_user_id"     integer            not null,
    "attached_user_id"  integer,
    frequency           int8 default 0     not null,
    "is_random_order"   bool default true  not null,
    "is_always_new"     bool default false not null,
    "last_request_time" date,
    constraint Bundle_Config_PK primary key (id)
);
insert into bundle_config (owner_user_id, frequency, is_random_order, is_always_new)
values (1, 15, false, false);



