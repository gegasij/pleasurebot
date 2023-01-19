drop table if exists Bundle;

create table if not exists Bundle
(
    id                  serial            not null,
    "bundle_config_id"  integer           not null,
    "message"           varchar(1024)     not null,
    "order"        integer default 0 not null,
    "used_count"        integer default 0 not null,
    "last_request_time" timestamp,
    constraint Bundle_PK primary key (id)
);
