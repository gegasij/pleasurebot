drop table if exists Bundle;

create table if not exists Bundle
(
    id                  integer primary key generated always as identity            not null,
    "bundle_config_id"  integer           not null,
    "message"           varchar(1024)     ,
    "order"        integer default 0 not null,
    "used_count"        integer default 0 not null,
    "last_request_time" timestamp
);


