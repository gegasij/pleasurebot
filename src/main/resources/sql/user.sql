drop table if exists "user";
create table if not exists "user"
(
    id            integer primary key generated always as identity not null,
    telegram_id   bigint,
    role          integer,
    bot_state     integer default 0,
    password      varchar,
    login         varchar,
    creation_date date
);
insert into public."user"(telegram_id, role, bot_state, password, login, creation_date)
values (538273546, 1, 0, 'test', 'clientTest', now());

insert into public."user"(telegram_id, role, bot_state, password, login, creation_date)
values (538273546, 1, 0, 'test', 'consumerTest', now());
