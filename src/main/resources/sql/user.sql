create table "user"
(
    telegram_id bigint,
    id          serial primary key,
    role        integer
);