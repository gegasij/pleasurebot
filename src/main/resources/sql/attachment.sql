drop table if exists Attachment;

create table if not exists Attachment
(
    id               integer primary key generated always as identity not null,
    bundle_id        integer                                          not null,
    telegram_file_id varchar(255)                                     not null,
    attachment_type integer
);


