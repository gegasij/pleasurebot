drop table if exists Bundle_Config;
create table if not exists Bundle_Config
(
    id                  integer primary key generated always as identity not null,
    "owner_user_id"     integer                                          not null,
    "attached_user_id"  integer                                          not null,
    frequency           int8 default 0                                   not null,
    "is_random_order"   bool default true                                not null,
    "is_always_new"     bool default false                               not null,
    "last_request_time" timestamp
);